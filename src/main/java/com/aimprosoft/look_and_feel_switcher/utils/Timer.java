package com.aimprosoft.look_and_feel_switcher.utils;

/**
 * @author Mikhail Tkachenko
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
