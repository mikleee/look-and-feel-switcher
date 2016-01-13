package com.aimprosoft.look_and_feel_switcher.utils;

/**
 * crated by m.tkachenko on 12.01.16 18:40
 */
public class Timer {

    private long start = System.currentTimeMillis();

    public long getTime() {
        return System.currentTimeMillis() + start;
    }

    public double getSeconds() {
        return (System.currentTimeMillis() - start) / 1000.;
    }
}
