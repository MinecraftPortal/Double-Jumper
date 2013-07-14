package com.hcherndon.dj;

import com.hcherndon.dj.framework.Commandation;
import com.hcherndon.dj.framework.DJP;
import com.hcherndon.dj.framework.Mode;
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

    @Command(name = "mode", desc = "Set or cycle through modes!")
    public static void mode(Player pl, String... args){
        DJP p = DoubleJumper.getInstance().getDJP(pl);
        if(args.length > 0){
            StringBuffer st = new StringBuffer();
            for(String s : args)
                st.append(s + " ");
            String mode = st.toString();
            if(Mode.valueOf(mode.toUpperCase().replaceAll(" ", "_")) != null){
                p.setMode(Mode.valueOf(mode.toUpperCase().replaceAll(" ", "_")));
                p.println("&aMode set to " + mode + "!");
                if(Mode.valueOf(mode.toUpperCase().replaceAll(" ", "_")).equals(Mode.DOUBLE_JUMPING) || Mode.valueOf(mode.toUpperCase().replaceAll(" ", "_")).equals(Mode.FLYING))
                    p.setPlayerAllowCFlight(true);
                else p.setPlayerAllowCFlight(false);
            } else {
                p.println("&cNot a valid mode!");
                return;
            }
        } else {
            p.cycleModes();
        }
    }
}
