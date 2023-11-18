package com.example.goparking

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.example.goparking.databinding.ActivityQrcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

class QRCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrcodeBinding

    private lateinit var tvDate: TextView
    private lateinit var tvParkingSpot: TextView
    private lateinit var tvHours: TextView
    private lateinit var tvDuration: TextView

    private lateinit var ivQrCode: ImageView

    private var idSpot001 = "655877227287c1d104e6f527"
    private var idSpot002 = "655877297287c1d104e6f528"
    private var idSpot003 = "6558772e7287c1d104e6f529"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorStatusBar()

        val bookingInfo = intent.getSerializableExtra("bookingInfo") as? BookingInfo
        val reservationID = intent.getStringExtra("reservationID")
        Log.e("reservationID", "onCreate: $reservationID")
        Log.e("reservationID", "onCreate: $bookingInfo")

        if (bookingInfo != null) {
            var parkingSpot = bookingInfo.parkingSpot
            val date = bookingInfo.date
            val duration = bookingInfo.duration
            val hours = bookingInfo.hours

            tvDate = binding.tvDate
            tvDate.text = date

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

            tvHours = binding.tvHours
            tvHours.text = hours

            tvDuration = binding.tvDuration
            tvDuration.text = duration + " hours"

            ivQrCode = binding.qrCodeImageView
            try {
                val bitMatrix: BitMatrix = QRCodeWriter().encode(
                    reservationID,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
                )

                val width = bitMatrix.width
                val height = bitMatrix.height
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                    }
                }

                ivQrCode.setImageBitmap(bitmap)

            } catch (e: WriterException) {
                e.printStackTrace()
            }

        }
    }

    private fun setColorStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor("#BC0063")
        }
    }
}