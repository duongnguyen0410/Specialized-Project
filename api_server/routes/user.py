from fastapi import APIRouter

from models.models import User
from config.db import db
from schemas.schemas import userEntity, usersEntity
from bson import ObjectId

router = APIRouter()

@router.post('/user/create')
async def create_user(user: User):
    db["users"].insert_one(dict(user))
    return usersEntity(db["users"].find())

@router.put('/user/update/{id}')
async def update_user(id, user: User):
    db["users"].find_one_and_update({"_id":ObjectId(id)},{
        "$set":dict(user)
    })
    return userEntity(db["users"].find_one({"_id":ObjectId(id)}))

@router.get('/user/get/all')
async def find_all_users():
    print(db["users"].find())
    print(usersEntity(db["users"].find()))
    return usersEntity(db["users"].find())

@router.get('/user/get/{id}')
async def get_user(id):
    return userEntity(db["users"].find_one({"_id": ObjectId(id)}))

@router.delete('/user/delete/{id}')
async def delete_user(id, user: User):
    return userEntity(db["users"].find_one_and_delete({"_id":ObjectId(id)}))