package com.example.goparking

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookingInfo = intent.getSerializableExtra("bookingInfo") as? BookingInfo

        if (bookingInfo != null) {
            val id = bookingInfo.id
            val parkingSpot = bookingInfo.parkingSpot
            val date = bookingInfo.date
            val duration = bookingInfo.duration
            val hours = bookingInfo.hours

            tvDate = binding.tvDate
            tvDate.text = date

            tvParkingSpot = binding.tvParkingSpot
            tvParkingSpot.text = parkingSpot

            tvHours = binding.tvHours
            tvHours.text = hours

            tvDuration = binding.tvDuration
            tvDuration.text = duration + " hours"

            ivQrCode = binding.qrCodeImageView
            try {
                val bitMatrix: BitMatrix = QRCodeWriter().encode(
                    id,
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
}