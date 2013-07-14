package com.hcherndon.dj.hook;

import com.hcherndon.dj.DoubleJumper;
import com.hcherndon.dj.framework.DJP;
import com.hcherndon.dj.framework.Mode;
import com.hcherndon.dj.framework.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

/**
 * Created with IntelliJ IDEA.
 * User: HcHerndon
 * Date: 7/13/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Listeners implements Listener{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent e){
        DoubleJumper.getInstance().addDJP(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent e){
        if(DoubleJumper.getInstance().getDJP(e.getPlayer().getName()) != null)
            DoubleJumper.getInstance().getDJP(e.getPlayer().getName()).decimate();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onKick(PlayerKickEvent e){
        if(DoubleJumper.getInstance().getDJP(e.getPlayer().getName()) != null)
            DoubleJumper.getInstance().getDJP(e.getPlayer().getName()).decimate();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onToggleFlight(PlayerToggleFlightEvent e){
        Validate.checkNullDJP(e.getPlayer().getName());
        e.setCancelled(DoubleJumper.getInstance().getDJP(e.getPlayer()).invoke());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamageEvent(final EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            if(DoubleJumper.getInstance().getDJP((Player) e.getEntity()) != null){
                if(DoubleJumper.getInstance().getDJP((Player) e.getEntity()).getMode().equals(Mode.DOUBLE_JUMPING)){
                    if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                        e.setCancelled(true);
                    }
                }
            } else
                return;
        } else
            return;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void commandPreprocess(PlayerCommandPreprocessEvent e){
        String label = e.getMessage().replaceFirst("/", "");
        label = label.split(" ")[0];
        if(label.equalsIgnoreCase(DoubleJumper.getInstance().getFlyCommand())){
            e.setCancelled(true);
            Validate.checkNullDJP(e.getPlayer().getName());
            DJP d = DoubleJumper.getInstance().getDJP(e.getPlayer());
            if(d.canPlayerFly()){
                d.toggleFlight();
                return;
            } else
                d.println("&cYou do not have permission to fly!");
        } else return;
    }
}
