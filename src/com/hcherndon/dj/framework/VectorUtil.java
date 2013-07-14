package com.hcherndon.dj.framework;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: HcHerndon
 * Date: 7/13/13
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class VectorUtil {
    public static Vector calculateLookVector(Location location) {
        double pitch = Math.toRadians(location.getPitch());
        double yaw = Math.toRadians(location.getYaw());

        Vector normal = new Vector(
                -(Math.cos(pitch) * Math.sin(yaw)),
                -Math.sin(pitch),
                Math.cos(pitch) * Math.cos(yaw)
        );

        return normal;
    }
}
