// com.antelope.ci.bus.portal.core.shell.command.PortalFormHitContext.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.BusPortalFormHelper;
import com.antelope.ci.bus.portal.core.configuration.xo.Form;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Box;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Component;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Content;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Field;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Group;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Label;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Style;
import com.antelope.ci.bus.portal.core.configuration.xo.form.StyleAlign;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Title;
import com.antelope.ci.bus.portal.core.configuration.xo.form.Widget;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_ComponentType;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Position;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.Margin;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalShellText;
import com.antelope.ci.bus.portal.core.shell.PortalShellUtil;
import com.antelope.ci.bus.portal.core.shell.ShellLineContentSet;
import com.antelope.ci.bus.portal.core.shell.buffer.BusPortalBufferFactory;
import com.antelope.ci.bus.portal.core.shell.buffer.BusPortalInputBuffer;
import com.antelope.ci.bus.server.shell.ShellPalette;
import com.antelope.ci.bus.server.shell.ShellText;
import com.antelope.ci.bus.server.shell.command.Command;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月3日		下午5:45:31 
 */
public class PortalFormContext {
	private final static Logger log = Logger.getLogger(PortalFormContext.class);
	protected Form form;
	protected BusPortalBufferFactory bufferFactory;
	protected BusPortalShell shell;
	protected Properties properties;
	protected String name;
	protected int cursor_x = 0;
	protected int cursor_y = 0;
	
	public PortalFormContext(BusPortalShell shell, Class commandClass) throws CIBusException {
		this.shell = shell;
		loadForm(commandClass);
	}
	
	public String getName() {
		return name;
	}
	
	protected void loadForm(Class commandClass) throws CIBusException {
		shell.enterEdit();
		init(commandClass);
		reset();
		if (form != null) {
			Content content = form.getContent();
			if (content == null)	return;
			if (bufferFactory == null) {
				bufferFactory = new BusPortalBufferFactory(name, shell.getIO());
			} else {
				bufferFactory.resetBuffer();
			}
			
			ShellLineContentSet contentSet = new ShellLineContentSet();
			ShellPalette palette = PortalShellUtil.getContentPalette(shell);
			int width = getContentWidth(palette);
			Style formStyle = form.getStyle();
			// title for shell
			Title title = content.getTitle();
			if (title != null) {
				FormContent titleContent = new FormContent(false, false, "", title.toShellText());
				Style titleStyle = supplyStyle(title, formStyle);
				if (palette != null) {
					XPosition xPosition = renderText(titleContent,  title.getValue(), titleStyle, width);
					if (xPosition != null)
						cursor_y += 1 + xPosition.end / width;
				}
				addShellContent(contentSet, titleContent);
			}
			
			// group for shell
			List<Group> groupList = content.getGroupList();
			List<FormContent> contentList = new ArrayList<FormContent>();
			int widthpercent = 0;
			for (Group group : groupList) {
				componentToContentInProcess(contentSet, contentList, widthpercent);
				List<Component> componentList = group.getComponentList();
				if (componentList != null) {
					for (Component component : componentList) {
						String componentName = component.getName();
						Label label = component.getLabel();
						repairWidgetName(componentName, label);
						Field field = component.getField();
						repairWidgetName(componentName, field);
						try {
							EU_ComponentType ctype = component.toComponentType();
							switch (ctype) {
								case textfield:
									widthpercent = dealTextfield(label, field, contentSet, contentList, palette, formStyle, widthpercent);
									break;
								default:
									break;
							}
							if (bufferFactory.getBuffer(componentName) == null)
								addWidgeBuffer(componentName, new Widget[]{label, field});
						} catch (CIBusException e) {
							DevAssistant.errorln(e);
							log.error(e);
						}
					}
				}
			}
			componentToContentInTail(contentSet, contentList, widthpercent);
			
			shell.drawForm(name, contentSet);
			focus(content);
			bufferFactory.initActiveBuffer();
			shell.enterInputMode();
		}
	}
	
	private void repairWidgetName(String componentName, Widget widget) {
		if (StringUtil.empty(widget.getName()))
			widget.setName(componentName + "." + widget.getTypeName());
	}
	
	public boolean nextWidget() {
		return bufferFactory.next();
	}
	
	public void upWidget() {
		enterNextWidget();
	}
	
	public void downWidget() {
		enterNextWidget();
	}
	
	public void leftWidget() {
		enterNextWidget();
	}
	
	public void rightWidget() {
		enterNextWidget();
	}
	
	private void init(Class clazz) {
		if (clazz.isAnnotationPresent(Command.class)) {
			Command cmd = (Command) clazz.getAnnotation(Command.class);
			if (!StringUtil.empty(cmd.property())) {
				String property = cmd.property();
				properties = BusPortalFormHelper.getProperties(property);
				if (properties == null) {
					try {
						properties = BusPortalFormHelper.loadProperties(property);
					} catch (CIBusException e) {
						DevAssistant.errorln(e);
					}
				}
			}
			
			if (!StringUtil.empty(cmd.form())) {
				String form_xml = cmd.form();
				form = BusPortalFormHelper.getForm(form_xml);
				if (form == null) {
					try {
						form = BusPortalFormHelper.loadForm(form_xml, this.getClass());
						if (properties != null)
							BusPortalFormHelper.convert(form, properties);
					} catch (CIBusException e) {
						DevAssistant.errorln(e);
					}
				}
			}
			
			name = cmd.status() + "." + cmd.mode();
		}
	}
	
	private void reset() {
		cursor_x = 0;
		cursor_y = 0;
	}
	
	private void addWidgeBuffer(String componentName, Widget[] widgets) {
		for (Widget widget : widgets) {
			if (widget.editable()) {
				String bufferName = genWidgetName(componentName, widget);
				widget.setIdentity(bufferName);
				ShellPalette contentPalette = shell.getContentPalette();
				int x = contentPalette.getX() + widget.getX();
				int y = contentPalette.getY() + widget.getY();
				BusPortalInputBuffer buffer = new BusPortalInputBuffer(
						shell, x, y, widget.width(), widget.getRowSize(), bufferName);
				bufferFactory.addBuffer(buffer);
			}
		}
	}
	
	private int dealTextfield(Label label, Field field, ShellLineContentSet contentSet, List<FormContent> contentList, 
			ShellPalette palette, Style formStyle, int widthpercent) throws CIBusException {
		// label
		if (!StringUtil.empty(label.getValue()))
			widthpercent = dealWidget(label, contentSet, contentList, palette, formStyle, label.getValue(), widthpercent);
		// field
		String boxValue = getBoxValue(field);
		if (!StringUtil.empty(boxValue))
			widthpercent = dealWidget(field, contentSet, contentList, palette, formStyle, boxValue, widthpercent);
		return widthpercent;
	}
	
	
	private String getBoxValue(Widget widget) {
		Box box = widget.getBox();
		if (null != box)
			return box.getBottom().getExpression();
		return null;
	}
	
	private XPosition renderText(FormContent content, String value, Style style, int width) {
		StyleAlign align = style.getAlign();
		if (align == null) return null;
		
		Margin margin = align.getMarginObject();
		int before = margin.getBefore();
		int after = margin.getAfter();
		
		int len = StringUtil.lengthVT(value);
		
		EU_Position position = align.getEU_Position();
		int indent = 0;
		switch (position) {
			case CENTER:
				indent = (width - len) / 2;
				break;
			case RIGHT:
				indent = width - len;
				break;
			default:
				break;
		}
		content.text.setIndent(indent);
		
		String fill = align.getFill() != null ? align.getFill() :  " ";
		int n = 0;
		if (before > 0) {
			while (n < before) {
				value = fill + value;
				n++;
			}
		}
		
		if (after > 0) {
			n = 0;
			while (n < after) {
				value += fill;
				n++;
			}
		}
		
		content.text.setText(value);
		
		return new XPosition(before+indent, indent+StringUtil.lengthVT(value));
	}
	
	private Style supplyStyle(Object owner, Style formStyle) throws CIBusException {
		Style curStyle = (Style) ProxyUtil.invokeRet(owner, "getStyle");
		if (curStyle == null) {
			ProxyUtil.invoke(owner, "setStyle", new Object[]{formStyle});
			return formStyle;
		}
		return curStyle;
	}
	
	private int getContentWidth(ShellPalette palette) {
		int width = 0;
		if (palette != null)	width = palette.getWidth();
		
		return width;
	}
	
	private void addShellContent(ShellLineContentSet contentSet, FormContent content) {
		List<String> line = new ArrayList<String>();
		line.add(content.toString());
		contentSet.addLine(line);
	}
	
	private void addShellContent(ShellLineContentSet contentSet, List<FormContent> contentList) {
		List<String> line = new ArrayList<String>();
		line.add(FormContent.fromList(contentList));
		contentSet.addLine(line);
	}
	
	private int dealWidget(Widget widget, ShellLineContentSet contentSet, List<FormContent> contentList, 
			ShellPalette palette, Style formStyle, String str, int widthpercent) throws CIBusException {
		int width = getContentWidth(palette);
		int length = widget.getComponetWidth(width);
		int rowSize = widget.getRowSize();
		FormContent content = new FormContent(widget.editable(), widget.onfocus(), widget.getDisplayName(), widget.toShellText(str));
		Style style = supplyStyle(widget, formStyle);
		XPosition xPosition = null;
		
		if (!StringUtil.empty(str) && palette != null) {
			xPosition = renderText(content,  str, style, length);
			cursor_x += xPosition.start;
			widget.setX(cursor_x);
			cursor_x += xPosition.end - xPosition.start;
			widget.setY(cursor_y);
		}
		
		if (rowSize > 0) {
			componentToContentInProcess(contentSet, contentList, widthpercent);
			addShellContent(contentSet, content);
			widthpercent = 0;
			cursor_x = 0;
			cursor_y += rowSize;
		} else {
			widthpercent += widget.percentForWidth();
			contentList.add(content);
			if (widthpercent >= 100) {
				componentToContentInProcess(contentSet, contentList, widthpercent);
				widthpercent = 0;
				cursor_x = 0;
				cursor_y += 1;
			}
		}
		
		return widthpercent;
	}
	
	private void componentToContentInProcess(ShellLineContentSet contentSet, List<FormContent> contentList, int widthpercent) {
		compoentToContent(contentSet, contentList, widthpercent, false);
	}
	
	private void componentToContentInTail(ShellLineContentSet contentSet, List<FormContent> contentList, int widthpercent) {
		compoentToContent(contentSet, contentList, widthpercent, true);
	}
	
	private void compoentToContent(ShellLineContentSet contentSet, List<FormContent> contentList, int widthpercent, boolean tail) {
		if (!contentList.isEmpty() || tail == true) {
			addShellContent(contentSet, contentList);
			contentList.clear();
			widthpercent = 0;
		}
	}
	
	private void focus(Content content) throws CIBusException  {
		Widget focus_widget = content.getFocusWidget();
		if (focus_widget != null) {
			shell.moveContent();
			shell.move(focus_widget.getX(), focus_widget.getY());
			shell.savePositionFromContent(focus_widget.getX(), focus_widget.getY());
			BusPortalInputBuffer focus_buffer = getWidgetBuffer(focus_widget);
			if (focus_buffer != null)
				shell.replaceBuffer(focus_buffer);
		}
	}
	
	private BusPortalInputBuffer getWidgetBuffer(Widget widget) {
		if (bufferFactory != null && !StringUtil.empty(widget.getIdentity()))
			return bufferFactory.getBuffer(widget.getIdentity());
				
		return null;
	}
	
	private String genWidgetName(String componentName, Widget widget) {
		return componentName + "." + widget.getDisplayName() + "." + widget.getTypeName();
	}
	
	protected void enterNextWidget() {
		try {
			if (bufferFactory.next()) {
				BusPortalInputBuffer nextBuffer = bufferFactory.getActiveBuffer();
				shell.replaceBuffer(nextBuffer);
				shell.shift(nextBuffer.getArea().getPositionx(), nextBuffer.getArea().getPositiony());
			}
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void enterFormCommand() {
		shell.replaceBuffer(bufferFactory.initCommand());
	}
	
	public void exitFormCommand() {
		shell.finishFormCommandMode();
		shell.replaceBuffer(bufferFactory.getActiveBuffer());
	}
		
	private static class XPosition {
		int start;
		int end;
		public XPosition(int start, int end) {
			super();
			this.start = start;
			this.end = end;
		}
	}
	
	private static class FormContent {
		boolean isBlock;
		boolean isFocus;
		String name;
		ShellText text;
		public FormContent(boolean isBlock, boolean isFocus, String name, ShellText text) {
			super();
			this.isBlock = isBlock;
			this.isFocus = isFocus;
			this.name = name;
			this.text = text;
		}
		
		@Override public String toString() {
			String value = text.toString();
			if (isFocus)
				value = PortalShellText.genFocusText(value);
			if (isBlock)
				value = PortalShellText.genBlockText(value, name);
			return value;
		}
		
		public static String fromList(List<FormContent> contentList) {
			StringBuffer buf = new StringBuffer();
			if (contentList != null && !contentList.isEmpty()) {
				buf.append(ShellText.P_PREFIX);
				for (FormContent content : contentList)
					buf.append(content.toString());
				buf.append(ShellText.P_SUFFIX);
			}
			return buf.toString();
		}
	}
	
	public Map<String, String> getFormContents() {
		return bufferFactory.getBufferContents();
	}
}

