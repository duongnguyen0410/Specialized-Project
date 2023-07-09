package com.example.goparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookingInfo = intent.getSerializableExtra("bookingInfo") as? BookingInfo

        if (bookingInfo != null) {
            val id = bookingInfo.id
            val parkingSpot = bookingInfo.parkingSpot
            val date = bookingInfo.date
            val duration = bookingInfo.duration
            val hours = bookingInfo.hours
            val totalCost = bookingInfo.totalCost

            tvParkingSpot = binding.tvParkingSpot
            tvParkingSpot.text = parkingSpot

            tvDate = binding.tvDate
            tvDate.text = date

            tvDuration = binding.tvDuration
            tvDuration.text = duration + " hours"

            tvHours = binding.tvHours
            tvHours.text = hours

            tvTotalCost = binding.totalCost
            tvTotalCost.text = totalCost + " VND"

            when (parkingSpot) {
                "Slot A" -> slotRef = myRef.child("slotA/ticket")
                "Slot B" -> slotRef = myRef.child("slotB/ticket")
                "Slot C" -> slotRef = myRef.child("slotC/ticket")
            }

            btContinue = binding.btnContinue
            btContinue.setOnClickListener {
                slotRef.setValue(bookingInfo)
                val intent = Intent(this, QRCodeActivity::class.java)
                intent.putExtra("bookingInfo", bookingInfo)
                startActivity(intent)
            }

        }

    }
}