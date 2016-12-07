# Blender Control App - Test 003
Rough python, android, java, and Raspberry Pi code samples for controlling Blender3D in real-time from the network. In this case, controlling blender rotations using an accelerometer sensor. Work-in-progress.

| | |
| ------------- | ------------- |
| **android-controller-app** | Android application sending internal gravity sensor values to blender via UDP. |
| **blender-scene** | The 3D scene.|
| **blender-scripts** | The python listener/control script running in Blender.|
| **java-controller** | Simple Java sender/receiver for verifying UDP | transmissions.|
| **rpi-controller** | Raspberry Pi controller using accelerometer breakout from [Sparkfun](https://sparkfun.com) (Not yet committed)
