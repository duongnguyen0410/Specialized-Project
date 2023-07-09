package com.example.goparking

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private val TAG = "map_error"

    private val apiKey = "AIzaSyB-aVUtcniPvTWTHCdaMMp-eJpTL8JiX0Q"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo MapFragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val requestBody = createRequestBody()

        val apiService = RoutesApiClient.create()
        val call = apiService.computeRoutes(apiKey, requestBody)

        call.enqueue(object : Callback<RoutesApiResponse> {
            override fun onResponse(call: Call<RoutesApiResponse>, response: Response<RoutesApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val encodedPolyline = apiResponse?.routes?.get(0)?.polyline?.encodedPolyline
                    Log.e(TAG, "onResponse: $encodedPolyline")

                    drawPolyline(encodedPolyline!!)
                } else {
                    Log.e(TAG, "onResponse: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<RoutesApiResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
            }
        })

        val encodedPolyline = "ipkcFjichVzQ@d@gU{E?"
    }

    private fun createRequestBody(): RoutesApiBody {
        val origin = Origin(
            location = Location(
                latLng = LatLng(
                    latitude = 37.419734,
                    longitude = -122.0827784
                )
            )
        )

//        val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
//
//        if (!success){
//            Log.e("Main Activity", "onMapReady: Style parsing failed")
//        }

        val destination = Destination(
            location = Location(
                latLng = LatLng(
                    latitude = 37.417670,
                    longitude = -122.079595
                )
            )
        )

        val routeModifiers = RouteModifiers(
            avoidTolls = false,
            avoidHighways = false,
            avoidFerries = false
        )

        return RoutesApiBody(
            origin = origin,
            destination = destination,
            travelMode = "DRIVE",
            routingPreference = "TRAFFIC_AWARE",
            departureTime = "2023-10-15T15:01:23.045123456Z",
            computeAlternativeRoutes = false,
            routeModifiers = routeModifiers,
            languageCode = "en-US",
            units = "IMPERIAL"
        )
    }

    private fun drawPolyline(encodedPolyline: String){

        val decodedPolyline = PolyUtil.decode(encodedPolyline)

        val polylineOptions = PolylineOptions()
            .color(Color.RED)
            .width(15f)
            .addAll(decodedPolyline)

        map.addPolyline(polylineOptions)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(decodedPolyline.first(), 16f)
        map.animateCamera(cameraUpdate)

    }
}
