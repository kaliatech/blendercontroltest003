package com.jgstech.bc003.net;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Slf4j
public class PacketReceiveTester {

    DatagramSocket socket = null;

    int count = 0;

    public void init() throws SocketException {
        socket = new DatagramSocket(6000);
        socket.setSoTimeout(1000);
    }

    public void receive() throws IOException {

        byte[] buf = new byte[4];
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        DatagramPacket packet = new DatagramPacket(byteBuffer.array(), byteBuffer.remaining());

        try {
            socket.receive(packet);
            log.debug("Received packet...");
            log.debug("Packet length: " + packet.getLength());

            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            float f = byteBuffer.getFloat();
            log.debug("Received:" + f);
        }
        catch(SocketTimeoutException ste) {
            //log.debug("timeout", ste);
            //poor man's non-blocking read. Better would be thread or NIO. Or look at rhr2 project.
        }
    }

    public void shutdown() {
        if (socket != null) {
            socket.close();
        }
    }
}
