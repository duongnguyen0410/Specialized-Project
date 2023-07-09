def userEntity(item) -> dict:
    return {
        "id":str(item["_id"]),
        "name":item["name"],
        "email":item["email"],
        "password":item["password"],
        "phone":item["phone"]
    }

def usersEntity(entity) -> list:
    return [userEntity(item) for item in entity]

def spotEntity(item) -> dict:
    return {
        "id":str(item["_id"]),
        "spot_id":item["spot_id"],
        "status":item["status"]   
    }
def spotsEntity(entity) -> list:
    return [spotEntity(item) for item in entity]

def reservationEntity(item) -> dict:
    return {
        "id":str(item["_id"]),
        "user_id":item["user_id"],
        "spot_id":item["spot_id"],
        "reservation_time":item["reservation_time"],
        "start_hour":item["start_hour"],
        "end_hour":item["end_hour"]
    }
def reservationsEntity(entity) -> list:
    return [reservationEntity(item) for item in entity]