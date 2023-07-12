import serial

def serial_comm():
    ser = serial.Serial('COM3', 9600)
    data = 'accept'
    bytes_written = ser.write(data.encode())  # Ghi chuỗi data vào cổng Serial
    if bytes_written == len(data.encode()):
        print('Write successful')
    else:
        print('Write failed')