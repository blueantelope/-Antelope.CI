// com.antelope.ci.bus.portal.core.shell.command.PortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import java.util.ArrayList;
import java.util.List;
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
import com.antelope.ci.bus.portal.core.shell.PortalShellUtil;
import com.antelope.ci.bus.portal.core.shell.ShellLineContentSet;
import com.antelope.ci.bus.portal.core.shell.buffer.BusPortalFormBuffer;
import com.antelope.ci.bus.portal.core.shell.buffer.BusPortalInputBuffer;
import com.antelope.ci.bus.server.shell.ShellPalette;
import com.antelope.ci.bus.server.shell.ShellText;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.hit.Hit;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		上午10:44:48 
 */
public abstract class PortalHit extends Hit {
	private final static Logger log = Logger.getLogger(PortalHit.class);
	protected Form form;
	protected BusPortalFormBuffer formBuffer;
	protected Properties properties;
	protected int cursor_x = 0;
	protected int cursor_y = 0;
	
	public PortalHit() {
		init();
	}
	
	private void init() {
		if (this.getClass().isAnnotationPresent(Command.class)) {
			Command cmd = this.getClass().getAnnotation(Command.class);
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
		}
	}
	
	protected void reset() {
		cursor_x = 0;
		cursor_y = 0;
	}
	
	protected void loadForm(BusPortalShell shell) throws CIBusException {
		reset();
		if (form != null) {
			Content content = form.getContent();
			if (content == null)	return;
			if (formBuffer == null) {
				formBuffer = new BusPortalFormBuffer(getIdentity());
			} else {
				formBuffer.resetBuffer();
			}
			
			ShellLineContentSet contentSet = new ShellLineContentSet();
			ShellPalette palette = PortalShellUtil.getContentPalette(shell);
			int width = getContentWidth(palette);
			Style formStyle = form.getStyle();
			// title for shell
			Title title = content.getTitle();
			if (title != null) {
				ShellText text = title.toShellText();
				Style titleStyle = repairStyle(title, formStyle);
				if (palette != null) {
					XPosition xPosition = renderText(text,  title.getValue(), titleStyle, width);
					if (xPosition != null)
						cursor_y += 1 + xPosition.end / width;
				}
				addShellContent(contentSet, text);
			}
			
			// group for shell
			List<Group> groupList = content.getGroupList();
			List<ShellText> textList = new ArrayList<ShellText>();
			int widthpercent = 0;
			for (Group group : groupList) {
				componentToContentInProcess(contentSet, textList, widthpercent);
				List<Component> componentList = group.getComponentList();
				if (componentList != null) {
					for (Component component : componentList) {
						String componentName = component.getName();
						Label label = component.getLabel();
						Field field = component.getField();
						try {
							EU_ComponentType ctype = component.toComponentType();
							switch (ctype) {
								case textfield:
									widthpercent = dealTextfield(label, field, contentSet, textList, palette, formStyle, widthpercent);
									break;
								default:
									break;
							}
							if (formBuffer.getBuffer(componentName) == null)
								addWidgeBuffer(componentName, shell, new Widget[]{label, field});
						} catch (CIBusException e) {
							DevAssistant.errorln(e);
							log.error(e);
						}
					}
				}
			}
			componentToContentInTail(contentSet, textList, widthpercent);
			
			shell.writeContent(contentSet);
			focus(shell, content);
			shell.enterInputMode();
		}
	}
	
	private void addWidgeBuffer(String componentName, BusPortalShell shell, Widget[] widgets) {
		for (Widget widget : widgets) {
			if (widget.editable()) {
				String bufferName = genWidgetName(componentName, widget);
				widget.setIdentity(bufferName);
				ShellPalette contentPalette = shell.getContentPalette();
				int x = contentPalette.getX() + widget.getX();
				int y = contentPalette.getY() + widget.getY();
				BusPortalInputBuffer buffer = new BusPortalInputBuffer(
						shell, x, y, widget.width(), widget.getRowSize(), bufferName);
				formBuffer.addBuffer(buffer);
			}
		}
	}
	
	private int dealTextfield(Label label, Field field, ShellLineContentSet contentSet, List<ShellText> textList, 
			ShellPalette palette, Style formStyle, int widthpercent) throws CIBusException {
		// label
		if (!StringUtil.empty(label.getValue()))
			widthpercent = dealWidget(label, contentSet, textList, palette, formStyle, label.getValue(), widthpercent);
		// field
		String boxValue = getBoxValue(field);
		if (!StringUtil.empty(boxValue))
			widthpercent = dealWidget(field, contentSet, textList, palette, formStyle, boxValue, widthpercent);
		return widthpercent;
	}
	
	
	private String getBoxValue(Widget widget) {
		Box box = widget.getBox();
		if (null != box)
			return box.getBottom().getExpression();
		return null;
	}
	
	private XPosition renderText(ShellText text, String value, Style style, int width) {
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
		text.setIndent(indent);
		
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
		
		text.setText(value);
		
		return new XPosition(before+indent, indent+StringUtil.lengthVT(value));
	}
	
	private Style repairStyle(Object owner, Style formStyle) throws CIBusException {
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
	
	private void addShellContent(ShellLineContentSet contentSet, ShellText text) {
		List<String> line = new ArrayList<String>();
		line.add(text.toString());
		contentSet.addLine(line);
	}
	
	private void addShellContent(ShellLineContentSet contentSet, List<ShellText> textList) {
		List<String> line = new ArrayList<String>();
		line.add(ShellText.toShellText(textList));
		contentSet.addLine(line);
	}
	
	private int dealWidget(Widget widget, ShellLineContentSet contentSet, List<ShellText> textList, 
			ShellPalette palette, Style formStyle, String str, int widthpercent) throws CIBusException {
		int width = getContentWidth(palette);
		int length = widget.getComponetWidth(width);
		int rowSize = widget.getRowSize();
		ShellText text = widget.toShellText(str);
		Style style = repairStyle(widget, formStyle);
		XPosition xPosition = null;
		
		if (!StringUtil.empty(str) && palette != null) {
			xPosition = renderText(text,  str, style, length);
			cursor_x += xPosition.start;
			widget.setX(cursor_x);
			cursor_x += xPosition.end - xPosition.start;
			widget.setY(cursor_y);
		}
		
		if (rowSize > 0) {
			componentToContentInProcess(contentSet, textList, widthpercent);
			addShellContent(contentSet, text);
			widthpercent = 0;
			cursor_x = 0;
			cursor_y += rowSize;
		} else {
			widthpercent += widget.percentForWidth();
			textList.add(text);
			if (widthpercent >= 100) {
				componentToContentInProcess(contentSet, textList, widthpercent);
				widthpercent = 0;
				cursor_x = 0;
				cursor_y += 1;
			}
		}
		
		return widthpercent;
	}
	
	private void componentToContentInProcess(ShellLineContentSet contentSet, List<ShellText> textList, int widthpercent) {
		compoentToContent(contentSet, textList, widthpercent, false);
	}
	
	private void componentToContentInTail(ShellLineContentSet contentSet, List<ShellText> textList, int widthpercent) {
		compoentToContent(contentSet, textList, widthpercent, true);
	}
	
	private void compoentToContent(ShellLineContentSet contentSet, List<ShellText> textList, int widthpercent, boolean tail) {
		if (!textList.isEmpty() || tail == true) {
			addShellContent(contentSet, textList);
			textList.clear();
			widthpercent = 0;
		}
	}
	
	private void focus(BusPortalShell shell, Content content) throws CIBusException  {
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
		if (formBuffer != null && !StringUtil.empty(widget.getIdentity()))
			return formBuffer.getBuffer(widget.getIdentity());
				
		return null;
	}
	
	private String genWidgetName(String componentName, Widget widget) {
		return componentName + "." + widget.getDisplayName() + "." + widget.getTypeName();
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
}

