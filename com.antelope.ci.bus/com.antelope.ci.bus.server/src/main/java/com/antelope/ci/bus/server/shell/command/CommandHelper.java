// com.antelope.ci.bus.server.shell.command.CommandHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import java.util.HashMap;
import java.util.Map;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-9-2		上午9:14:29 
 */
public class CommandHelper {
	public static class CommandSignPair {
		public String _prefix;
		public String _suffix;
		public CommandSignPair(String _prefix, String _suffix) {
			super();
			this._prefix = _prefix;
			this._suffix = _suffix;
		}
	}
	
	private static final String NUMBER_PREFIX = "<n:";
	private static final String NUMBER_SUFFIX = ">";
	private static Map<String, CommandSignPair> command_sign_map;
	public final static String downCommand = NUMBER_PREFIX + NetVTKey.DOWN + NUMBER_SUFFIX;
	public final static String upCommand = NUMBER_PREFIX + NetVTKey.UP + NUMBER_SUFFIX;
	public final static String leftCommand = NUMBER_PREFIX + NetVTKey.LEFT + NUMBER_SUFFIX;
	public final static String rightCommand = NUMBER_PREFIX + NetVTKey.RIGHT + NUMBER_SUFFIX;
	public final static String tabCommand = NUMBER_PREFIX + NetVTKey.TABULATOR + NUMBER_SUFFIX;
	public final static String enterCommand = NUMBER_PREFIX + NetVTKey.LF + NUMBER_SUFFIX;
	static {
		command_sign_map = new HashMap<String, CommandSignPair>();
		command_sign_map.put("number", new CommandSignPair(NUMBER_PREFIX, NUMBER_SUFFIX));
	}
	
	public static String convertSign(String value) {
		StringBuffer sb = new StringBuffer();
		sb.append(value);
		int index = 0;
		for (String name : command_sign_map.keySet()) {
			String str = sb.toString();
			sb = new StringBuffer();
			String prefix = command_sign_map.get(name)._prefix;
			String suffix = command_sign_map.get(name)._suffix;
			index = 0;
			while (true) {
				int start = str.indexOf(prefix, index);
				if (start != -1) {
					int end = str.indexOf(suffix, start);
					if (end != -1) {
						if (start > index)
							sb.append(str.substring(index, start));
						String command = str.substring(start+prefix.length(), end);
						COMMAND_SIGN csign = COMMAND_SIGN.fromFix(prefix, suffix);
						switch (csign) {
							case NUMBER:
								try {
									sb.append((char)Integer.parseInt(command));
								} catch (Exception e) {
									DevAssistant.errorln(e);
								}
								break;
							default:
								break;
						}
						index = end + 1;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			if (index < str.length())
				sb.append(str.substring(index));
		}
		
		return sb.toString();
	}
	
	public static String genNumberCommand(int cmd) {
		return genSignCommand("number", String.valueOf(cmd));
	}
	
	private static String genSignCommand(String name, String cmd) {
		CommandSignPair pair = command_sign_map.get(name);
		if (pair != null)
			return pair._prefix + cmd + pair._suffix;
		return cmd;
	}
	
	private static boolean signString(String str, String prefix, String suffix) {
		if (str.startsWith(prefix) && str.endsWith(suffix))
			return true;
		return false;
	}
	
	private static String truncate(String str, String prefix, String suffix) {
		if (str.startsWith(prefix) && str.endsWith(suffix))
			return str.substring(prefix.length(), str.length()-suffix.length());
		
		return str;
	}
	
	public enum COMMAND_SIGN {
		STRING("string", null, null),
		NUMBER("number", command_sign_map.get("number")._prefix, command_sign_map.get("number")._suffix);
		
		private String name;
		private String _suffix;
		private String _prefix;
		COMMAND_SIGN(String name, String _prefix, String _suffix) {
			this.name = name;
			this._prefix = _prefix;
			this._suffix = _suffix;
		}
		
		public String getName() {
			return name;
		}
		
		public String get_prefix() {
			return _prefix;
		}

		public String get_suffix() {
			return _suffix;
		}
		
		public String truncate(String command) {
			return CommandHelper.truncate(command,_prefix,  _suffix);
		}
		
		public static COMMAND_SIGN fromFix(String _prefix, String _suffix) {
			for (COMMAND_SIGN csign : COMMAND_SIGN.values()) {
				if (_prefix.equalsIgnoreCase(csign.get_prefix()) && _suffix.equalsIgnoreCase(csign.get_suffix()) )
					return csign;
			}
			
			return STRING;
		}
		
		public static COMMAND_SIGN fromName(String name) throws CIBusException {
			for (COMMAND_SIGN csign : COMMAND_SIGN.values()) {
				if (name.equalsIgnoreCase(csign.getName()))
					return csign;
			}
			
			throw new CIBusException("", "unkown command sing name");
		}
		
		public static COMMAND_SIGN toSign(String command) {
			for (String name : command_sign_map.keySet()) {
				String prefix = command_sign_map.get(name)._prefix;
				String suffix = command_sign_map.get(name)._suffix;
				if (CommandHelper.signString(command, prefix, suffix)) {
					try {
						return fromName(name);
					} catch (CIBusException e) {
						DevAssistant.errorln(e);
					}
				}
			}
			
			return STRING;
		}
	}
}

