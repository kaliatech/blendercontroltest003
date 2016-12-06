package msgsender;


import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSender {

    DatagramSocket socket;
    InetAddress address;


    public void init() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();

        address = InetAddress.getByName("10.9.22.107");


    }

    public void sendRotation(float x, float y, float z) throws IOException {

        byte[] buf = new byte[4];


        AsyncTask<Float, Void, Void> asyncTask = new AsyncTask<Float, Void, Void>() {

            @Override
            protected Void doInBackground(Float... vals) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(4 * 3);
                byteBuffer.putFloat(vals[0]);
                byteBuffer.putFloat(vals[1]);
                byteBuffer.putFloat(vals[2]);

                DatagramPacket packet = new DatagramPacket(byteBuffer.array(), byteBuffer.position(), address, 6000);
                log.debug("Sending packet...");
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    log.error("Error sending packet.", e);
                }
                return null;
            }
        };

        asyncTask.execute(x, y, z);

    }

    public void shutdown() {
        if (socket != null) {
            socket.close();
        }
    }

}
