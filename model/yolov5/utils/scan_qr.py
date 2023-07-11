import cv2
import requests
from pyzbar import pyzbar

def scan_qr_code():
    # Khởi tạo camera
    cap = cv2.VideoCapture(0)

    while True:
        # Đọc khung hình từ camera
        _, frame = cap.read()

        # Chuyển đổi hình ảnh sang ảnh xám
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # Tìm mã QR trong ảnh xám
        barcodes = pyzbar.decode(gray)

        # Lặp qua các mã QR tìm thấy
        for barcode in barcodes:
            # Vẽ đường bao xung quanh mã QR
            (x, y, w, h) = barcode.rect
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

            # Giải mã nội dung từ mã QR
            barcode_data = barcode.data.decode("utf-8")
            barcode_type = barcode.type

            # Hiển thị nội dung mã QR và loại mã QR lên khung hình
            cv2.putText(frame, f"{barcode_data} ({barcode_type})", (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
            print("qr_code data:", barcode_data)
            if (barcode_data == getSpot()):
                print("accept qr_code")
                cap.release()
                cv2.destroyAllWindows()
                return True
            else: 
                print("ivalid code.")

        # Hiển thị khung hình
        cv2.imshow("QR Code Scanner", frame)

        # Thoát khỏi vòng lặp nếu nhấn phím 'q'
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Giải phóng camera và đóng cửa sổ
    cap.release()
    cv2.destroyAllWindows()

def getSpot():
    url = 'http://192.168.0.101:8000/spot/get/64aad77d51c18b0ec38d2ae4'
    reponse = requests.get(url)
    if reponse.status_code == 200:
        json_data = reponse.json()
        latest_reservation = json_data.get('latest_reservation')

        if latest_reservation:
            reservation_id = latest_reservation.get('_id')
            print(reservation_id)
        return reservation_id
    else:
        return None

# Chạy hàm quét mã QR
#scan_qr_code()