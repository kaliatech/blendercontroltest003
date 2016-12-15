package bca.jgstech.com.blendercontrollerapp.msgsender;


import android.os.AsyncTask;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

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

    public void sendRotation(Vector3D rotation) throws IOException {

        byte[] buf = new byte[4];


        AsyncTask<Vector3D, Void, Void> asyncTask = new AsyncTask<Vector3D, Void, Void>() {

            @Override
            protected Void doInBackground(Vector3D... vals) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(4 * 3);
                byteBuffer.putFloat((float)vals[0].getX());
                byteBuffer.putFloat((float)vals[0].getY());
                byteBuffer.putFloat((float)vals[0].getZ());

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

        asyncTask.execute(rotation);

    }

    public void shutdown() {
        if (socket != null) {
            socket.close();
        }
    }

}
