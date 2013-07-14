package com.hcherndon.dj.framework;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Commandation {
    private CommandExecutor executor;
    private Plugin plugin;
    private HashMap<String, CSFO> cmdType = new HashMap<String, CSFO>();
    public Commandation(final Plugin pl){
    	plugin = pl;
        executor = new CommandExecutor() {
            @Override
            public boolean onCommand(final CommandSender commandSender, org.bukkit.command.Command command, String s, final String[] args) {
            		String commandName = command.getName();
	                final CSFO cs = cmdType.get(commandName);
	                try {
	                     if (Sender.getValue(cs.s) == 0) {
                             try {
                                 cs.m.invoke(null, commandSender, args);
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
	                    } else if(Sender.getValue(cs.s) == 1){
	                    	if(commandSender instanceof Player){
                                try {
                                    cs.m.invoke(null, commandSender, args);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
	                    	} else
	                    		commandSender.sendMessage(ChatColor.RED + "You are not a player, you may not execute this!");
	                    }else if(Sender.getValue(cs.s) == 2){
	                    	if(commandSender instanceof ConsoleCommandSender){
                                try {
                                    cs.m.invoke(null, commandSender, args);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
	                    	} else
	                    		commandSender.sendMessage(ChatColor.RED + "You are not console, you may not execute this!");
	                    } else
	                    	commandSender.sendMessage(ChatColor.RED + "Something bad happened. Like REALLY bad.");
	                    return true;
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    return false;
	                }
	            }
        };
    }
    
    public void register(Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            register(m, m.getAnnotation(Command.class));
        }
    }
    
    public void register(Method m, Command com) {
        if (com == null)
            return;
        Registrator register = new Registrator(plugin, executor);
 
        String name = com.name();
        List<String> alias = new ArrayList<String>();
        alias.add(name);
        if (!ArrayUtils.isEmpty(com.alias()))
            Collections.addAll(alias, com.alias());
        register.register(name, alias.toArray(new String[alias.size()]), com.usage(), com.permission(), com.permissionMessage(), com.desc());
        this.cmdType.put(com.name(), new CSFO(m, com.sender()));
        System.out.println("Registered command: "+ com.name());
    }
    
    public class Registrator {
    	 
        protected final Plugin plugin;
        protected final CommandExecutor executor;
 
        public Registrator(Plugin plugin, CommandExecutor executor) {
            this.plugin = plugin;
            this.executor = executor;
        }
 
        public void register(String name, String[] aliases, String usage, String[] perms, String permMessage, String description) {
        	permMessage = permMessage.replaceAll("&", "§");
        	org.bukkit.command.Command cmd = new DynamicCommand(aliases, name, "Error! Correct usage is: /" + aliases[0] + " " + usage.replace("<command>", name), perms, permMessage, executor, plugin, plugin);
        	cmd.setDescription(description.replaceAll("&", "§"));
            getCommandMap().register(plugin.getDescription().getName(), cmd);
        }
 
        public CommandMap getCommandMap() {
            Field map;
            try {
                map = SimplePluginManager.class.getDeclaredField("commandMap");
                map.setAccessible(true);
                return (CommandMap) map.get(plugin.getServer().getPluginManager());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    
    public class DynamicCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    	 
        public CommandExecutor owner;
        public Object registeredWith;
        public Plugin owningPlugin;
        public String[] permissions = new String[0];
 
        public DynamicCommand(String[] aliases, String name, String usage, String[] perms, String permMessage, CommandExecutor owner, Object registeredWith, Plugin plugin) {
            super(name, name, usage, Arrays.asList(aliases));
            this.owner = owner;
            this.owningPlugin = plugin;
            this.registeredWith = registeredWith;
            if (perms.length > 0)
                setPermissions(perms);
            if (!StringUtils.isEmpty(permMessage))
                setPermissionMessage(ChatColor.RED + permMessage);
        }
 
        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            boolean hasPerm = false;
            if(permissions.length > 0){
            	for (String perm : permissions) {
            		if (sender.hasPermission(perm))
            			hasPerm = true;
            	}
            } else
            	hasPerm = true;
            if (!hasPerm) {
                sender.sendMessage(getPermissionMessage());
                return false;
            }
            return owner.onCommand(sender, this, label, args);
        }
 
        public void setPermissions(String[] permissions) {
            this.permissions = permissions;
            super.setPermission(StringUtils.join(permissions, ";"));
        }
 
        @Override
        public Plugin getPlugin() {
            return owningPlugin;
        }
    }
    
    private class CSFO{
    	public Method m;
    	public Sender s;
    	public CSFO(Method m, Sender s){
    		this.m = m;
    		this.s = s;
    	}
    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Command {
        public String name();
        
        public String desc() default "Just a Command.";
 
        public String usage() default "/<command>";
 
        public String[] alias() default {};
 
        public Sender sender() default Sender.EVERYONE;
 
        public String[] permission() default {};
 
        public String permissionMessage() default "&cAccess. DENIED.";
    }
 
    public enum Sender {
    	EVERYONE(0),
        CONSOLE(2),
        PLAYER(1);
        Sender(int cl){
        }
        
        public static int getValue(Sender s){
        	if(s == Sender.EVERYONE)
        		return 0;
        	if(s == Sender.CONSOLE)
        		return 2;
        	if(s == Sender.PLAYER)
        		return 1;
        	return 0;
        }
    }
}
