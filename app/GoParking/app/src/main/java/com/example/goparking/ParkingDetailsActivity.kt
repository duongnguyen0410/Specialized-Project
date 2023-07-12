package com.example.goparking

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.goparking.databinding.ActivityBookParkingBinding
import com.example.goparking.databinding.ActivityParkingDetailsBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.security.auth.callback.Callback

class ParkingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParkingDetailsBinding
    private lateinit var btnBooking: AppCompatButton

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorStatusBar()

        val userId = intent.getStringExtra("userID")
        Log.e("userID", "onCreate: $userId")

        binding = ActivityParkingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnBooking = binding.btnBooking
        btnBooking.setOnClickListener {
            val intent = Intent(this, BookParkingActivity::class.java)
            intent.putExtra("userID", userId)
            startActivity(intent)
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