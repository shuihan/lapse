package com.lapse.remoting.util;

/**
 * 
 * @author Administrator
 * 
 */
public class IDGenerator {

    private static int id = Integer.MIN_VALUE;


    public static int nextId() {
        if (id > (Integer.MAX_VALUE - 10)) {
            reset();
        }

        return id++;
    }


    public static void reset() {
        id = Integer.MIN_VALUE;
    }
}
