package com.soapgu.lightapp;

import com.orhanobut.logger.Logger;

public class LightFns {
    private static volatile Lightbar instance;

    public static Lightbar getInstance()
    {
        if( instance == null ) {
            synchronized ( LightFns.class ) {
                if( instance == null ){
                    instance = new LightImplement();
                }
            }
        }
        return instance;
    }
}
