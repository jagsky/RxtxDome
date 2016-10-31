package com.jag.listport.serial;

/**
 * Created by wwwch on 2016/10/31.
 */
public class Test {
    public static void main(String[] args) {
        Thread thread = new Thread(new SerialReader());
        thread.setName("Thread-MyThread");
        thread.start();
    }
}
