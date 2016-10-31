package com.jag.listport.serial;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * @Description：串口数据读取类，用于Windows的串口数据都去 Created by wwwch on 2016/10/31.
 */
public class SerialReader implements Runnable, SerialPortEventListener {
    private Enumeration<CommPortIdentifier> portList;
    private CommPortIdentifier portId;
    private SerialPort serialPort;
    private OutputStream outputStream;
    private InputStream inputStream;
    /*
    * @Description  初始化端口的参数
    * */

    public void init() {
        //1.获取所有端口,获取PortId
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = portList.nextElement();
            //2.判断端口号是否是串口，是否是Com1端口，如何说者Open
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM1")) {
                    try {
                        serialPort = (SerialPort) portId.open("SerialPort-Test", 1000);
                        //3.添加监听器，数据事件，设置参数
                        serialPort.addEventListener(this);
                        serialPort.notifyOnDataAvailable(true);
                        serialPort.setSerialPortParams(9600, 8, 1, SerialPort.PARITY_NONE);

                        //4.获取输入 输出流
                        outputStream = serialPort.getOutputStream();
                        inputStream = serialPort.getInputStream();
                    } catch (PortInUseException e) {
                        e.printStackTrace();
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    //5.实现接口SerialPortEventListener中的方法 读取从串口中接收的数据
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.BI://通讯中断
                break;
            case SerialPortEvent.FE://帧错误
                break;
            case SerialPortEvent.CD://溢位错误
                break;
            case SerialPortEvent.CTS://清楚发送数据
                break;
            case SerialPortEvent.PE://奇偶位错误
                break;
            case SerialPortEvent.DSR://数据设备准备好
                break;
            case SerialPortEvent.RI://响铃侦测
                break;
            //接受串口发过来的数据
            case SerialPortEvent.DATA_AVAILABLE://串口中可用数据
                int newDate = 0;
                int i = 0;
                do {
                    try {
                        newDate = inputStream.read();
                        System.out.println(newDate);
                        i++;
                        if (i == 24) {
                            newDate = -1;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                while (newDate != -1);
                //serialPort.close();
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY://输出缓冲区已清空信息
                break;
            default:
                break;

        }
    }

    //6.向串口发送消息
    public  void sendMsg(){

        try {
            outputStream.write(12312123);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        init();
        sendMsg();
    }
}
