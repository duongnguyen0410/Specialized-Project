from fastapi import FastAPI

from routes.user import router as user
from routes.spot import router as spot
from routes.reservation import router as reservation

app = FastAPI()

app.include_router(user)
app.include_router(spot)
app.include_router(reservation)