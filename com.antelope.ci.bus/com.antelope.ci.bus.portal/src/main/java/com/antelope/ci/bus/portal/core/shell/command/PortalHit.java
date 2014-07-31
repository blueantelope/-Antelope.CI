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
import com.antelope.ci.bus.portal.core.shell.PortalShellUtil;
import com.antelope.ci.bus.portal.core.shell.ShellLineContentSet;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.ShellPalette;
import com.antelope.ci.bus.server.shell.ShellText;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.hit.Hit;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		上午10:44:48 
 */
public abstract class PortalHit extends Hit {
	private final static Logger log = Logger.getLogger(PortalHit.class);
	protected Form form;
	protected Properties properties;
	
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
	
	protected void draw(BusShell shell) throws CIBusException {
		if (form != null) {
			Content content = form.getContent();
			if (content == null)	return;
			
			ShellLineContentSet contentSet = new ShellLineContentSet();
			ShellPalette palette = PortalShellUtil.getContentPalette(shell);
			int width = getContentWidth(palette);
			Style formStyle = form.getStyle();
			// title for shell
			Title title = content.getTitle();
			if (title != null) {
				ShellText text = title.toShellText();
				Style titleStyle = repairStyle(title, formStyle);
				if (palette != null)
					renderText(text,  title.getValue(), titleStyle, width);
				addShellContent(contentSet, text);
			}
			
			// group for shell
			List<Group> groupList = content.getGroupList();
			List<ShellText> componet_textList = new ArrayList<ShellText>();
			int widthpercent = 0;
			for (Group group : groupList) {
				compoentToContentInProcess(contentSet, componet_textList, widthpercent);
				List<Component> componentList = group.getComponentList();
				if (componentList != null) {
					for (Component component : componentList) {
						Label label = component.getLabel();
						Field field = component.getField();
						try {
							EU_ComponentType ctype = component.toComponentType();
							switch (ctype) {
								case textfield:
									dealWidget(label, contentSet, componet_textList, palette, formStyle, width, label.getName(), widthpercent);
									dealWidget(field, contentSet, componet_textList, palette, formStyle, width, field.getValue(), widthpercent);
									break;
								default:
									break;
							}
						} catch (CIBusException e) {
							DevAssistant.errorln(e);
							log.error(e);
						}
					}
				}
			}
			compoentToContentInTail(contentSet, componet_textList, widthpercent);
			
			shell.writeContent(contentSet);
		}
	}
	
	private void renderText(ShellText text, String value, Style style, int width) {
		StyleAlign align = style.getAlign();
		if (align == null) return;
		
		Margin margin = align.getMarginObject();
		int before = margin.getBefore();
		int after = margin.getAfter();
		
		int len = StringUtil.getWordCount(value);
		
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
	
	private void dealWidget(Widget widget, ShellLineContentSet contentSet, List<ShellText> componet_textList, 
			ShellPalette palette, Style formStyle, int width, String str, int widthpercent) throws CIBusException {
		int length = widget.getComponetWidth(width);
		int rowSize = widget.getRowSize();
		ShellText text = widget.toShellText(str);
		Style style = repairStyle(widget, formStyle);
		if (palette != null)
			renderText(text,  str, style, length);
		if (rowSize > 0) {
			compoentToContentInProcess(contentSet, componet_textList, widthpercent);
			addShellContent(contentSet, text);
			widthpercent = 0;
		} else {
			widthpercent += widget.percentForWidth();
			if (widthpercent >= 100) {
				compoentToContentInProcess(contentSet, componet_textList, widthpercent);
			} else {
				componet_textList.add(text);
			}
		}
	}
	
	private void compoentToContentInProcess(ShellLineContentSet contentSet, List<ShellText> componet_textList, int widthpercent) {
		compoentToContent(contentSet, componet_textList, widthpercent, false);
	}
	
	private void compoentToContentInTail(ShellLineContentSet contentSet, List<ShellText> componet_textList, int widthpercent) {
		compoentToContent(contentSet, componet_textList, widthpercent, true);
	}
	
	private void compoentToContent(ShellLineContentSet contentSet, List<ShellText> componet_textList, int widthpercent, boolean tail) {
		if (!componet_textList.isEmpty() || tail == true) {
			addShellContent(contentSet, componet_textList);
			componet_textList.clear();
			widthpercent = 0;
		}
	}
}

