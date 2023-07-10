import serial
import time

ser = serial.Serial('COM3', 9600) 

while True:
    data = "accept"
    ser.write(data.encode())

    received_data = ser.readline()
    decoded_data = received_data.decode().strip()
    print(decoded_data)
    time.sleep(1)
    
ser.close()