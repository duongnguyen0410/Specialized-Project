package com.example.goparking

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.goparking.databinding.ActivityReviewSummaryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class ReviewSummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewSummaryBinding

    private lateinit var tvParkingSpot: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvHours: TextView
    private lateinit var tvTotalCost: TextView

    private lateinit var btContinue: AppCompatButton

    private val myRef = FirebaseDatabase.getInstance().reference
    private lateinit var slotRef: DatabaseReference

    private var idSpot001 = "64aad77d51c18b0ec38d2ae4"
    private var idSpot002 = "64aad78651c18b0ec38d2ae5"
    private var idSpot003 = "64aad77d51c18b0ec38d2ae6"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorStatusBar()

        binding = ActivityReviewSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookingInfo = intent.getSerializableExtra("bookingInfo") as? BookingInfo
        val reservationID = intent.getStringExtra("reservationID")
        Log.e("reservationID", "onCreate: $reservationID")

        if (bookingInfo != null) {
            val id = bookingInfo.id
            var parkingSpot = bookingInfo.parkingSpot
            val date = bookingInfo.date
            val duration = bookingInfo.duration
            val hours = bookingInfo.hours
            val totalCost = bookingInfo.totalCost

            tvParkingSpot = binding.tvParkingSpot
            tvParkingSpot = binding.tvParkingSpot
            when (parkingSpot){
                idSpot001 -> {
                    parkingSpot = "Spot 001"
                    tvParkingSpot.text = parkingSpot
                }

                idSpot002 -> {
                    parkingSpot = "Spot 002"
                    tvParkingSpot.text = parkingSpot
                }

                idSpot003 -> {
                    parkingSpot = "Spot 003"
                    tvParkingSpot.text = parkingSpot
                }
            }

            tvDate = binding.tvDate
            tvDate.text = date

            tvDuration = binding.tvDuration
            tvDuration.text = duration + " hours"

            tvHours = binding.tvHours
            tvHours.text = hours

            tvTotalCost = binding.totalCost
            tvTotalCost.text = totalCost + " VND"

            btContinue = binding.btnContinue
            btContinue.setOnClickListener {
                val intent = Intent(this, QRCodeActivity::class.java)
                intent.putExtra("bookingInfo", bookingInfo)
                intent.putExtra("reservationID", reservationID)
                startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setColorStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }
}