package com.example.goparking

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.goparking.databinding.ActivityBookParkingBinding
import com.example.goparking.databinding.ActivityParkingDetailsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ParkingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParkingDetailsBinding
    private lateinit var btnBooking: AppCompatButton

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParkingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnBooking = binding.btnBooking
        btnBooking.setOnClickListener {
            val intent = Intent(this, BookParkingActivity::class.java)
            startActivity(intent)
        }

    }
}