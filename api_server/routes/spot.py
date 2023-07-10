from fastapi import APIRouter

from models.models import Spot
from config.db import db
from schemas.schemas import spotEntity, spotsEntity
from bson import ObjectId

router = APIRouter()

@router.post('/spot/create')
async def create_spot(spot: Spot):
    db["spots"].insert_one(dict(spot))
    return spotsEntity(db["spots"].find())

@router.put('/spot/update/{id}')
async def update_spot(id, state: str):
    if state == "reserved" or state == "occupied":
        latest_reservation = db["reservations"].find({"spot_id": id}).sort("_id", -1).limit(1)
        latest_reservation_data = latest_reservation[0]
        db["spots"].find_one_and_update({"_id": ObjectId(id)}, {
            "$set": {
                "status": state,
                "latest_reservation": latest_reservation_data
            }
        })
    else:
        db["spots"].find_one_and_update({"_id": ObjectId(id)}, {
            "$set": {"status": state},
            "$unset": {"latest_reservation": ""}
        })

    return spotEntity(db["spots"].find_one({"_id": ObjectId(id)}))

@router.get('/spot/get/all')
async def find_all_spots():
    print(db["spots"].find())
    print(spotsEntity(db["spots"].find()))
    return spotsEntity(db["spots"].find())

@router.get('/spot/get/{id}')
async def get_spot(id):
    return spotEntity(db["spots"].find_one({"_id": ObjectId(id)}))

@router.delete('/spot/delete/{id}')
async def delete_spot(id, spot: Spot):
    return spotEntity(db["spots"].find_one_and_delete({"_id":ObjectId(id)}))