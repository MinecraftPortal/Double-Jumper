package com.hcherndon.dj.framework;

import org.bukkit.plugin.Plugin;

public class CommandRegister {
	private static Commandation cmdReg;
	public CommandRegister(Plugin pl){
		CommandRegister.cmdReg = new Commandation(pl);
	}
	
	public static void registerClass(Class clazz){
		cmdReg.register(clazz);
	}
}
