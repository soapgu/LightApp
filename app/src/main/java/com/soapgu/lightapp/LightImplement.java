package com.soapgu.lightapp;

import android.serialport.SerialPort;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class LightImplement implements Lightbar {
    private SerialPort transport;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean opened;
    private byte[] lastColor;


    public LightImplement() {
        this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x0F};
        opened = false;

    }

    public boolean getOpened() {
        return opened;
    }

    public synchronized void open() throws IOException {
        if( !opened ) {
            transport = new SerialPort(new File("/dev/ttyS1"), 9600);
            inputStream = transport.getInputStream();
            outputStream = transport.getOutputStream();
            opened = true;
            Logger.i("-----Serial port Opened----");
            //this.lastColor = new byte[]{(byte) 0xEE, (byte) 0xFF, (byte) 0x00};
            //this.send(this.lastColor);
        }
    }

    public synchronized void close() {
        if( opened ) {
            opened = false;
            try {
                inputStream.close();
            } catch (IOException e) {
            }
            try {
                outputStream.close();
            } catch (IOException e) {
            }
            transport.close();
            Logger.i("-----Serial port Closed----");
        }
    }

    public void red(String brightness) {
        if (brightness.equals("HIGH"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x00};
        if (brightness.equals("MEDIUM"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x03};
        if (brightness.equals("LOW"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x07};
        this.send(this.lastColor);
    }

    public void yellow(String brightness) {
        if (brightness.equals("HIGH"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x0A};
        if (brightness.equals("MEDIUM"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x0D};
        if (brightness.equals("LOW"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x11};
        this.send(this.lastColor);
    }

    public void green(String brightness) {
        if (brightness.equals("HIGH"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x14};
        if (brightness.equals("MEDIUM"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x17};
        if (brightness.equals("LOW"))
            this.lastColor = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x1B};
        this.send(this.lastColor);
    }

    public void white() {
        send(new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x23});
    }

    public void black() {
        send(new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0xFF});
    }

    public void blink() {
        new Thread(() -> {
            try {
                this.black();
                Thread.sleep(200);
                this.send(this.lastColor);
                Thread.sleep(200);
                this.black();
                Thread.sleep(200);
                this.send(this.lastColor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public synchronized void send(byte[] body) {
        if (transport == null) return;
        if (!opened) return;

        try {
            outputStream.write(body);
        } catch (IOException e) {
            Logger.i("Serial port send FAILED");
        }
    }
}
