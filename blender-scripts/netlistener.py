
import bpy
import math
import time
import traceback
import struct
import sys
import socket

UDP_IP = "0.0.0.0"
UDP_PORT = 6000

class NetListener(bpy.types.Operator):
    """Operator listening for network package using a timer"""
    bl_idname = "wm.modal_netlistener_operator"
    bl_label = "Network Listener Operator"

    sock = None

    _timer = None

    def __init__(self):
        self.sock = socket.socket(socket.AF_INET, # Internet
                         socket.SOCK_DGRAM) # UDP
        #self.sock.setblocking(0)
        self.sock.bind((UDP_IP, UDP_PORT))    

    def modal(self, context, event):
        if event.type == 'ESC':
            return self.cancel(context)

        if event.type == 'TIMER':
            now = time.strftime("%c")
            cube = bpy.data.objects["Cube"]
            location = cube.location
            rotation = cube.rotation_euler
            # location += 1.001

            # print (location)
            try:                        
                #data, addr = self.sock.recvfrom(1024) # buffer size is 1024 bytes
#                print ("about to receive...")
                data = self.doreceive(self.sock)
                # i = int.from_bytes(data, byteorder='big')
                i = struct.unpack('>fff', data)
#                print ("received messagex:", i[0])
                #location.y = i
                
                #rotation.x = math.radians(i[0] * 50) #math.pi * i / 180
                #rotation.y = math.radians(i[1] * 50) #math.pi * i / 180
                #rotation.y = math.radians(i[2] * 50) #math.pi * i / 180
                
                rotation.z = i[0] * -1
                rotation.y = i[1] * -1
                rotation.x = i[2] * -1
                
                
            #except socket.error: 
            #    print ('socket.error') 
            #    pass
            except:
                traceback.print_exc()
                # print ("Unexpected error:", sys.exc_info()[0])   
            
        return {'PASS_THROUGH'}

    def execute(self, context):
        self._timer = context.window_manager.event_timer_add(0.01, context.window)
        context.window_manager.modal_handler_add(self)
        return {'RUNNING_MODAL'}

    def cancel(self, context):
        print ("CANCEL")
        context.window_manager.event_timer_remove(self._timer)
        return {'CANCELLED'}

    def doreceive(self, socket):
        MSGLEN = 4 * 3;
        chunks = []
        bytes_recd = 0
        while bytes_recd < MSGLEN:
            chunk = self.sock.recv(min(MSGLEN - bytes_recd, 1024))
            if chunk == b'':
                raise RuntimeError("socket connection broken")
            chunks.append(chunk)
            bytes_recd = bytes_recd + len(chunk)
        return b''.join(chunks)

def register():
    bpy.utils.register_class(NetListener)


def unregister():
    bpy.utils.unregister_class(NetListener)

if __name__ == "__main__":
    register()
    # test call
    bpy.ops.wm.modal_netlistener_operator()
    