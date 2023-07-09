package com.example.goparking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private val myRef = FirebaseDatabase.getInstance().reference
    private val slotAref = myRef.child("slotA/state")
    private val slotBref = myRef.child("slotB/state")
    private val slotCref = myRef.child("slotC/state")

    private lateinit var selectOption: String
    private var diffInMinutes by Delegates.notNull<Long>()
    private lateinit var formattedCost: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        btBack = binding.btnBack
        btBack.setOnClickListener {
            val intent = Intent(this, ParkingDetailsActivity::class.java)
            startActivity(intent)
        }

        retreiveData(slotAref, ivSlotA, radioSlotA)
        retreiveData(slotBref, ivSlotB, radioSlotB)
        retreiveData(slotCref, ivSlotC, radioSlotC)

        radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radioButton: RadioButton = findViewById(checkedId)
            selectOption = radioButton.text.toString()

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

            Log.e("bookinginfo", "onCreate: $bookingInfo")
            val intent = Intent(this, ReviewSummaryActivity::class.java)
            intent.putExtra("bookingInfo", bookingInfo)
            startActivity(intent)
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

    private fun retreiveData(ref: DatabaseReference, imageView: ImageView, radioButton: RadioButton){
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val state = snapshot.getValue<String> ()
                Log.e("firebase", "onDataChange: $state")

                if (state.equals("occupied")) {
                    imageView.setImageResource(R.drawable.parking_car_icon)
                    radioButton.isClickable = false
                    radioButton.isFocusable = false
                }
                else
                    imageView.setImageResource(R.drawable.parking_icon)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", "Failed to read value.", error.toException())
            }
        }
        ref.addValueEventListener(listener)
    }
}