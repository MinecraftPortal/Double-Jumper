package com.hcherndon.dj;

import com.hcherndon.dj.framework.Commandation;
import com.hcherndon.dj.framework.DJP;
import com.hcherndon.dj.framework.Mode;
import com.hcherndon.dj.framework.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.hcherndon.dj.framework.Commandation.Command;

/**
 * Created with IntelliJ IDEA.
 * User: HcHerndon
 * Date: 7/13/13
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Commands {
    @Command(name = "djrl", permission = "doublejumper.reload", desc = "Reloads the Double Jumper config!", sender = Commandation.Sender.EVERYONE)
    public static void rlconf(CommandSender sender, String... args){
        DoubleJumper.getInstance().loadConfigOptions();
        sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
    }

    @Command(name = "mode", desc = "Set your mode!", sender = Commandation.Sender.PLAYER)
    public static void mode(Player pl, String... args){
        DJP p = DoubleJumper.getInstance().getDJP(pl);
        if(!(args.length > 0)){
            p.println("&cMissing argument!");
            p.println("&cModes: Flying, Double_Jumping, Jump");
            return;
        }
        if(Mode.valueOf(args[0].toUpperCase()) == null){
            p.println("&cNot a valid mode!");
            p.println("&cModes: Flying, Double_Jumping, Jump");
            return;
        }
        if(!p.hasPermission(Mode.getValue(Mode.valueOf(args[0].toUpperCase())))){
            p.println("&cYou do not have permission to access mode " + args[0] + "!");
            return;
        }
        p.setMode(Mode.valueOf(args[0].toUpperCase()));


        return;
    }

    @Command(name = "djtp", permission = "doublejumper.tp", desc = "Toggle double jump for the defined player, regardless of perms.", sender = Commandation.Sender.EVERYONE)
    public static void djtp(CommandSender sender, String... args){
        if(!Validate.checkNumberOfArgs(args, 0)){
            sender.sendMessage(ChatColor.RED + "You are missing an argument!");
            return;
        }
        DJP d = DoubleJumper.getInstance().getDJP(args[0]);
        if(d == null){
            sender.sendMessage(ChatColor.RED + "The player has to be online!");
            return;
        }
        d.overrideToggleDoubleJump();
        sender.sendMessage(ChatColor.GREEN + "Player force toggled! Currently: " + d.canPlayerDoubleJump());
        return;
    }

    @Command(name = "djup", permission = "doublejumper.update", desc = "Updates a players basic permissible objects.", sender = Commandation.Sender.EVERYONE)
    public static void djup(CommandSender sender, String... args){
        if(!Validate.checkNumberOfArgs(args, 0)){
            sender.sendMessage(ChatColor.RED + "You are missing an argument!");
            return;
        }
        DJP d = DoubleJumper.getInstance().getDJP(args[0]);
        if(d == null){
            sender.sendMessage(ChatColor.RED + "The player has to be online!");
            return;
        }
        d.setup();
        sender.sendMessage(ChatColor.GREEN + "Player force updated!");
        return;
    }
}
