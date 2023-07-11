import requests

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