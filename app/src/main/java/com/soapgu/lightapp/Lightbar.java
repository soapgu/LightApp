package com.soapgu.lightapp;

import java.io.IOException;

public interface Lightbar {
    boolean getOpened();
    void open() throws IOException;
    void close();

    void red(String brightness);
    void green(String brightness);
    void yellow(String brightness);
    void echo();
    void blink();
}
