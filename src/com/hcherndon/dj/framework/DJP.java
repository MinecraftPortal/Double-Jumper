package com.hcherndon.dj.framework;

import com.hcherndon.dj.DoubleJumper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: HcHerndon
 * Date: 7/13/13
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DJP {
    private Player player;
    private Mode mode;

    private boolean inJump = false;

    private boolean isInCooldown = false;
    private boolean overriddenDJ = false;

    private int currentTaskId = -1;

    public DJP(Player pl){
        this.player = pl;
        setup();
    }

    public void overrideToggleDoubleJump(){
        this.overriddenDJ = !overriddenDJ;
        if(overriddenDJ)
            setMode(Mode.DOUBLE_JUMPING);
        else
            setMode(Mode.JUMP);
    }

    public boolean isOverriddenDJ(){
        return overriddenDJ;
    }

    public void setup(){
        if(canPlayerDoubleJump()){
            setMode(Mode.DOUBLE_JUMPING);
            setPlayerAllowCFlight(true);
            setPlayerCFlying(false);
        } else {
            setMode(Mode.JUMP);
            setPlayerAllowCFlight(false);
            setPlayerCFlying(false);
        }
    }

    public Player getPlayer(){
        return this.player;
    }

    public void println(String msg){
        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void setPlayerAllowCFlight(boolean b){
        getPlayer().setAllowFlight(b);
    }

    public void setPlayerCFlying(boolean b){
        getPlayer().setFlying(b);
    }

    public Mode getMode(){
        return this.mode;
    }

    public boolean canPlayerFly(){
        return hasPermission(DoubleJumper.getInstance().getFlyPerm());
    }

    public boolean canPlayerDoubleJump(){
        return hasPermission(DoubleJumper.getInstance().getDjPerm());
    }

    public void setMode(Mode m){
        this.mode = m;
        if(m.equals(Mode.FLYING)){
            setPlayerAllowCFlight(true);
            return;
        }
        if(m.equals(Mode.DOUBLE_JUMPING)){
            setPlayerAllowCFlight(true);
            setPlayerCFlying(false);
            return;
        }
        if(m.equals(Mode.JUMP)){
            setPlayerAllowCFlight(false);
            setPlayerCFlying(false);
            return;
        }
    }

    @SuppressWarnings("deprecation")
    public boolean isOnGround(){
        return getPlayer().isOnGround();
    }

    public boolean hasPermission(String perm){
        return getPlayer().hasPermission(perm);
    }

    public void setVelocity(Vector velocity){
        getPlayer().setVelocity(velocity);
    }

    public String getName(){
        return getPlayer().getName();
    }

    public void decimate(){
        player = null;
        mode = null;
        DoubleJumper.getInstance().rmDJP(this);
    }

    public void toggleFlight(){
        if(this.getMode().equals(Mode.FLYING)){
            println("&aYou are now Double Jumping!");
            setMode(Mode.DOUBLE_JUMPING);
            return;
        }
        else {
            println("&aYou are now flying!");
            setMode(Mode.FLYING);
            return;
        }
    }

    public boolean invoke(){
        if(this.getMode().equals(Mode.FLYING))
            return false;
        else if(this.getMode().equals(Mode.DOUBLE_JUMPING)){
            if(isInJump())
                return true;
            if(isInCooldown())
                return true;
            doIt();
            return true;
        } else if(this.getMode().equals(Mode.JUMP)){
            setPlayerCFlying(false);
            setPlayerAllowCFlight(false);
            return true;
        }
        else throw new IllegalStateException("Player mode not found! This should not occur!");
    }

    public boolean isInJump(){
        return inJump;
    }

    public boolean isInCooldown(){
        return isInCooldown;
    }

    public void doIt(){
        inJump = true;
        Vector v = player.getLocation().getDirection().clone();
        v.normalize();
        v.divide(new Vector(2, 2, 2));
        v.multiply(DoubleJumper.getInstance().getHorizontalMultiplier());
        v.add(new Vector(0, DoubleJumper.getInstance().getHeightAdditive(), 0));
        v.multiply(DoubleJumper.getInstance().getMultiplier());
        setVelocity(v);
        startWatch();
    }

    public void startCooldown(){
        if(DoubleJumper.getInstance().getCooldown() > 0)
            DoubleJumper.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(DoubleJumper.getInstance(), new Runnable() {
                @Override
                public void run() {
                    isInCooldown = false;
                    println("&aJump cooled down!");
                }
            }, DoubleJumper.getInstance().getCooldown());
    }

    public void startWatch(){
        this.currentTaskId = DoubleJumper.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(DoubleJumper.getInstance(), new Runnable() {
            @Override
            public void run() {
                try{
                    if(isOnGround()){
                        inJump = false;
                        cancelCurrentTask();
                        if(DoubleJumper.getInstance().getCooldown() > 0){
                            startCooldown();
                            isInCooldown = true;
                        } else{
                            isInCooldown = false;
                        }
                        return;
                    }else return;
                }catch (Exception e){
                    cancelCurrentTask();
                }
            }
        }, 2L, 1L);
    }

    private void cancelCurrentTask(){
        try{
            DoubleJumper.getInstance().getServer().getScheduler().cancelTask(currentTaskId);
        }catch (Exception e){
            return;
        }
    }
}
