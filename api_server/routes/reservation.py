from fastapi import APIRouter

from models.models import Reservation
from config.db import db
from schemas.schemas import reservationEntity, reservationsEntity
from bson import ObjectId

router = APIRouter()

@router.post('/reservation/create')
async def create_reservation(reservation: Reservation):
    db["reservations"].insert_one(dict(reservation))
    return reservationsEntity(db["reservations"].find())

@router.put('/reservation/update/{id}')
async def update_reservation(id, reservation: Reservation):
    db["reservations"].find_one_and_update({"_id":ObjectId(id)},{
        "$set":dict(reservation)
    })
    return reservationEntity(db["reservations"].find_one({"_id":ObjectId(id)}))

@router.get('/reservation/get/all')
async def find_all_reservations():
    print(db["reservations"].find())
    print(reservationsEntity(db["reservations"].find()))
    return reservationsEntity(db["reservations"].find())

@router.get('/reservation/get/{id}')
async def get_reservation(id):
    return reservationEntity(db["reservations"].find_one({"_id": ObjectId(id)}))

@router.get('/reservation/get_last/{spot_id}')
async def get_last_reservation(spot_id: str):
    reservations = db["reservations"].find({"spot_id": spot_id}).sort("_id", -1).limit(1)
    last_reservation = reservationsEntity(reservations)[0]
    return last_reservation

@router.delete('/reservation/delete/{id}')
async def delete_reservation(id, reservation: Reservation):
    return reservationEntity(db["reservations"].find_one_and_delete({"_id":ObjectId(id)}))