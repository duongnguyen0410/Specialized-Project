def userEntity(item) -> dict:
    return {
        "id":str(item["_id"]),
        "name":item["name"],
        "email":item["email"],
        "password":item["password"],
        "phone":item["phone"],
        "license_plate":item["license_plate"]
    }

def usersEntity(entity) -> list:
    return [userEntity(item) for item in entity]

def spotEntity(item) -> dict:
    spot_entity = {
        "id": str(item["_id"]),
        "spot_id": item.get("spot_id"),
        "status": item.get("status"),
        "lp_detected": item.get("lp_detected"),
        "latest_reservation": item.get("latest_reservation")
    }
    if spot_entity["latest_reservation"] is not None:
        spot_entity["latest_reservation"]["_id"] = str(spot_entity["latest_reservation"]["_id"])
    return spot_entity

def spotsEntity(entity) -> list:
    return [spotEntity(item) for item in entity]

def reservationEntity(item) -> dict:
    return {
        "id":str(item["_id"]),
        "user_id":item["user_id"],
        "spot_id":item["spot_id"],
        "date":item["date"],
        "duration":item["duration"],
        "hours":item["hours"],
        "total_cost":item["total_cost"]
    }
def reservationsEntity(entity) -> list:
    return {
        "reservations": [reservationEntity(item) for item in entity]
    } 