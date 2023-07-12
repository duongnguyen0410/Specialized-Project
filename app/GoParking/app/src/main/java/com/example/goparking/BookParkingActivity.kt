package com.example.goparking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.goparking.databinding.ActivityBookParkingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class BookParkingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookParkingBinding
    private lateinit var dateSelect: FrameLayout
    private lateinit var shSelect: FrameLayout
    private lateinit var ehSelect: FrameLayout
    private lateinit var btBack: ImageButton
    private lateinit var btContinue: AppCompatButton
    private lateinit var tvDate: TextView
    private lateinit var tvStartHour: TextView
    private lateinit var tvEndHour: TextView
    private lateinit var tvTotalCost: TextView
    private lateinit var tvHoursQuantity: TextView
    private lateinit var ivSlotA: ImageView
    private lateinit var ivSlotB: ImageView
    private lateinit var ivSlotC: ImageView

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioSlotA: RadioButton
    private lateinit var radioSlotB: RadioButton
    private lateinit var radioSlotC: RadioButton

    private lateinit var selectedDate: Calendar
    private lateinit var myFormat: String
    private lateinit var sdf: SimpleDateFormat

    private lateinit var selectOption: String
    private var diffInMinutes by Delegates.notNull<Long>()
    private lateinit var formattedCost: String

    private var idSpotA = "64aad77d51c18b0ec38d2ae4"
    private var idSpotB = "64aad78651c18b0ec38d2ae5"
    private var idSpotC = "64aad78c51c18b0ec38d2ae6"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorStatusBar()

        val userId = intent.getStringExtra("userID")
        Log.e("userID", "onCreate: $userId")

        binding()

        btBack = binding.btnBack
        btBack.setOnClickListener {
            val intent = Intent(this, ParkingDetailsActivity::class.java)
            startActivity(intent)
        }

        getSpot(idSpotA, ivSlotA, radioSlotA)
        getSpot(idSpotB, ivSlotB, radioSlotB)
        getSpot(idSpotC, ivSlotC, radioSlotC)

        radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radioButton: RadioButton = findViewById(checkedId)
            selectOption = radioButton.text.toString()

            when (selectOption){
                "Spot 001" -> {
                    selectOption = idSpotA
                }

                "Spot 002" -> {
                    selectOption = idSpotB
                }

                "Spot 003" -> {
                    selectOption = idSpotC
                }
            }

            Log.e("radio", "onCreate: $selectOption")
        }

        val myCalendar = Calendar.getInstance()
        myFormat = "dd-MM-yyyy"
        sdf = SimpleDateFormat(myFormat, Locale.UK)

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateText(myCalendar)
        }
        dateSelect.setOnClickListener{
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        shSelect.setOnClickListener{
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (minute == 0)
                    tvStartHour.setText("$hourOfDay:${minute}0")
                else if (minute in 1..9)
                    tvStartHour.setText("$hourOfDay:0${minute}")
                else
                tvStartHour.setText("$hourOfDay:$minute")
            }, startHour, startMinute, false).show()
        }

        ehSelect.setOnClickListener{
            val currentTime = Calendar.getInstance()
            val endHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val endMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (minute <= 0)
                    tvEndHour.setText("$hourOfDay:${minute}0")
                else if (minute in 1..9)
                    tvEndHour.setText("$hourOfDay:0${minute}")
                else
                    tvEndHour.setText("$hourOfDay:$minute")

                calculateTotalCost(tvStartHour.text.toString(), tvEndHour.text.toString())
            }, endHour, endMinute, false).show()
        }

        btContinue.setOnClickListener {
            val uuid = UUID.randomUUID()
            val hours = tvStartHour.text.toString() + "-" + tvEndHour.text.toString()
            diffInMinutes /= 60
            val bookingInfo = BookingInfo(
                uuid.toString(),
                selectOption,
                tvDate.text.toString(),
                diffInMinutes.toString(),
                hours.toString(),
                formattedCost
            )

            val requestBody = ReservationApiBody(
                userId!!,
                selectOption,
                tvDate.text.toString(),
                diffInMinutes.toString(),
                hours.toString(),
                formattedCost
            )

            createReservation(requestBody, bookingInfo)
        }
    }

    private fun updateDateText(myCalendar: Calendar) {
        tvDate.setText(sdf.format(myCalendar.time))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateTotalCost(startHour: String, endHour: String){
        val format = SimpleDateFormat("H:mm", Locale.getDefault())

        val date1 = format.parse(startHour)
        val date2 = format.parse(endHour)

        val diffInMillis = date2.time - date1.time

        diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)

        Log.e("hours", "calculateTotalCost: ${diffInMinutes}")

        val cost = diffInMinutes/60 * 25000
        val numberFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        formattedCost = numberFormat.format(cost)

        tvTotalCost.setText("${formattedCost} VND")

        val hours = diffInMinutes/60
        if (hours.toInt() == 1)
            tvHoursQuantity.setText(" / 1 hour")
        else
            tvHoursQuantity.setText(" / $hours hours")
    }

    private fun getSpot(id: String, imageView: ImageView, radioButton: RadioButton){
        val apiService = ParkingApiClient.create()
        val call = apiService.getSpot(id)
        call.enqueue(object : Callback<SpotApiResponse?> {
            override fun onResponse(
                call: Call<SpotApiResponse?>,
                response: Response<SpotApiResponse?>
            ) {
                if (response.isSuccessful){
                    Log.e("get /spot/{id}", "onResponse: ")
                    val status = response.body()?.status
                    if (status.equals("available")) {
                        imageView.setImageResource(R.drawable.parking_icon)
                        radioButton.isEnabled = true
                    } else {
                        imageView.setImageResource(R.drawable.parking_car_icon)
                        radioButton.isEnabled = false
                    }
                }
            }

            override fun onFailure(call: Call<SpotApiResponse?>, t: Throwable) {
                Log.e("get /spot/{id}", "onFailure: $t")
            }
        })
    }

    private fun updateSpot(bookingInfo: BookingInfo, reservationID: String){
        val apiService = ParkingApiClient.create()
        val call = apiService.putSpot(bookingInfo.parkingSpot, "reserved")
        call.enqueue(object : Callback<SpotApiResponse?> {
            override fun onResponse(
                call: Call<SpotApiResponse?>,
                response: Response<SpotApiResponse?>
            ) {
                if (response.code() == 200){
                    Log.e("updateSpot", "onResponse: ${response.body()}")
                    val intent = Intent(this@BookParkingActivity, ReviewSummaryActivity::class.java)
                    intent.putExtra("reservationID", reservationID)
                    intent.putExtra("bookingInfo", bookingInfo)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<SpotApiResponse?>, t: Throwable) {
                Log.e("updateSpot", "onResponse: $t")
            }
        })
    }

    private fun createReservation(bodyRequest: ReservationApiBody, bookingInfo: BookingInfo){
        val apiService = ParkingApiClient.create()
        val call = apiService.createReservation(bodyRequest)
        call.enqueue(object : Callback<ReservationResponse?> {
            override fun onResponse(
                call: Call<ReservationResponse?>,
                response: Response<ReservationResponse?>
            ) {
                if (response.isSuccessful){
                    val reservationsResponse = response.body()
                    Log.e("post/reservation/create", "onResponse: ${response.code()}")

                    val lastReservation = reservationsResponse!!.reservations.lastOrNull()
                    if (lastReservation != null) {
                        val reservationId = lastReservation.id
                        updateSpot(bookingInfo, reservationId)
                    }
                }
            }

            override fun onFailure(call: Call<ReservationResponse?>, t: Throwable) {
                Log.e("post/reservation/create", "onFailure: $t")
            }
        })
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

    private fun binding(){
        dateSelect = binding.dateSelect
        shSelect = binding.shSelect
        ehSelect = binding.ehSelect
        btContinue = binding.btnContinue
        tvDate = binding.tvDate
        tvStartHour = binding.tvStartHour
        tvEndHour = binding.tvEndHour
        tvTotalCost = binding.totalCost
        tvHoursQuantity = binding.hourQuantity
        ivSlotA = binding.imageSlotA
        ivSlotB = binding.imageSlotB
        ivSlotC = binding.imageSlotC
        radioGroup = binding.radioGroup
        radioSlotA = binding.radioSlotA
        radioSlotB = binding.radioSlotB
        radioSlotC = binding.radioSlotC
    }
}