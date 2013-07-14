package com.hcherndon.dj.framework;

import com.hcherndon.dj.DoubleJumper;

/**
 * Created with IntelliJ IDEA.
 * User: HcHerndon
 * Date: 7/13/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Mode {
    FLYING(),

    DOUBLE_JUMPING(),

    JUMP();

    public static String getValue(Mode m){
        if(m.equals(FLYING))
            return DoubleJumper.getInstance().getFlyPerm();
        else if(m.equals(DOUBLE_JUMPING))
            return DoubleJumper.getInstance().getDjPerm();
        else return "";
    }
}
