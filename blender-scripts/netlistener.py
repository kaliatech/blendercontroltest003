
import bpy
import time
import sys
import traceback
import math

import socket

UDP_IP = "127.0.0.1"
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
        self.sock.setblocking(0)
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
                data, addr = self.sock.recvfrom(1024) # buffer size is 1024 bytes
                i = int.from_bytes(data, byteorder='big')
                print ("received messagex:", i)
                location.y = i
                rotation.y = math.radians(i) #math.pi * i / 180
            except socket.error: 
                # print ('socket.error') 
                pass
            except:
                traceback.print_exc()
                # print ("Unexpected error:", sys.exc_info()[0])   
            
        return {'PASS_THROUGH'}

    def execute(self, context):
        self._timer = context.window_manager.event_timer_add(0.1, context.window)
        context.window_manager.modal_handler_add(self)
        return {'RUNNING_MODAL'}

    def cancel(self, context):
        print ("CANCEL")
        context.window_manager.event_timer_remove(self._timer)
        return {'CANCELLED'}


def register():
    bpy.utils.register_class(NetListener)


def unregister():
    bpy.utils.unregister_class(NetListener)

if __name__ == "__main__":
    register()
    # test call
    bpy.ops.wm.modal_netlistener_operator()
    

