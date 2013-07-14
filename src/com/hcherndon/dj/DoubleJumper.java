package com.hcherndon.dj;

import com.hcherndon.dj.framework.CommandRegister;
import com.hcherndon.dj.framework.DJP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: HcHerndon
 * Date: 7/13/13
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleJumper extends JavaPlugin {
    private List<DJP> djps;
    private FileConfiguration config;
    private File conFile;

    private static DoubleJumper instance;

    private double multiplier;
    private double horizontalMultiplier;
    private double heightAdditive;
    private long cooldown;

    private String flyPerm;
    private String djPerm;

    private String flyCommand;

    @Override
    public void onEnable(){
        conFile = new File("plugins/DoubleJumper/config.yml");
        this.djps = new ArrayList<DJP>();
        setDefaults();
        loadConfigOptions();
        config = getConfig();
        instance = this;
        new CommandRegister(this);
        CommandRegister.registerClass(com.hcherndon.dj.Commands.class);
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        setupDJP();
    }

    public static DoubleJumper getInstance(){
        return instance;
    }

    public void setupDJP(){
        if(getServer().getOnlinePlayers().length > 0)
            for(Player pl : getServer().getOnlinePlayers())
                if(getDJP(pl) == null)
                    addDJP(pl);
    }

    public List<DJP> getDJP(){
        return this.djps;
    }

    public DJP getDJP(Player pl){
        return getDJP(pl.getName());
    }

    public DJP getDJP(String s){
        for(DJP d : getDJP())
            if(d.getName().equals(s))
                return d;
        return null;
    }

    public void rmDJP(DJP djp){
        getDJP().remove(djp);
    }

    public void addDJP(Player pl){
        this.djps.add(new DJP(pl));
    }

    public void addDJP(String s){
        if(getServer().getPlayer(s) != null)
            addDJP(getServer().getPlayer(s));
        else
            throw new IllegalStateException("Cannot instantiate a null player!");
    }

    public void setDefaults(){
        if(!conFile.exists()){
            try {
                getLogger().log(Level.INFO, "Creating new config!");
                conFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reloadConfig();
            saveConfig();
            getConfig().set("cooldownInTicks", 200);
            getConfig().set("heightAdditive", 0.5);
            getConfig().set("horizontalMultiplier", 0.25);
            getConfig().set("velocityMultiplier", 2);
            getConfig().set("flyPermission", "doublejumper.fly");
            getConfig().set("doubleJumpPermission", "doublejumper.dj");
            getConfig().set("toggleFlightCommand", "fly");
            saveConfig();
        }
    }

    public String getFlyCommand(){
        return this.flyCommand;
    }

    public String getFlyPerm(){
        return this.flyPerm;
    }

    public String getDjPerm(){
        return this.djPerm;
    }

    public long getCooldown(){
        return cooldown;
    }

    public double getHorizontalMultiplier(){
        return horizontalMultiplier;
    }

    public double getHeightAdditive(){
        return heightAdditive;
    }

    public double getMultiplier(){
        return multiplier;
    }

    public void loadConfigOptions(){
        reloadConfig();
        this.cooldown = getConfig().getLong("cooldownInTicks");
        this.heightAdditive = getConfig().getDouble("heightAdditive");
        this.horizontalMultiplier = getConfig().getDouble("horizontalMultiplier");
        this.multiplier = getConfig().getDouble("velocityMultiplier");
        this.flyPerm = getConfig().getString("flyPermission");
        this.djPerm = getConfig().getString("doubleJumpPermission");
        this.flyCommand = getConfig().getString("toggleFlightCommand");
    }
}
