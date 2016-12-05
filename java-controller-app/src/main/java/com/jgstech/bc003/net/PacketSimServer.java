package com.jgstech.bc003.net;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

@Slf4j
public class PacketSimServer {

    DatagramSocket socket = null;

    int count = 0;

    public void init() throws SocketException {
        socket = new DatagramSocket();
    }

    public void send() throws IOException {

        byte[] buf = new byte[4];

        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(count++);

        InetAddress address = InetAddress.getLoopbackAddress();
        DatagramPacket packet = new DatagramPacket(byteBuffer.array(), byteBuffer.position(), address, 6000);

        log.debug("Sending packet...");
        socket.send(packet);

    }

    public void shutdown() {
        if (socket != null) {
            socket.close();
        }
    }

    public void reset() {
        this.count = 0;
    }
}
