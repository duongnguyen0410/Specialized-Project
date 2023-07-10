from pydantic import BaseModel

class User(BaseModel):
    name: str
    email: str
    password: str
    phone: str

class Spot(BaseModel):
    spot_id: str
    status: str

class Reservation(BaseModel):
    user_id: str
    spot_id: str
    date: str
    duration: str
    hours: str
    total_cost: str