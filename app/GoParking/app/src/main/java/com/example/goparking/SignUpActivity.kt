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
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.goparking.databinding.ActivitySignInBinding
import com.example.goparking.databinding.ActivitySignupBinding
import okhttp3.RequestBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var btnBack: ImageButton
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorStatusBar()

        btnBack = binding.btnBackLogin
        btnBack.setOnClickListener{
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.pwd.text.toString()
            val name = binding.name.text.toString()
            val phone = binding.phone.text.toString()
            val license_plate = binding.licensePlate.text.toString()

            val requestBody = UserApiBody(
                email,
                password,
                name,
                phone,
                license_plate
            )

            createUser(requestBody)
        }
    }

    private fun createUser(requestBody: UserApiBody){
        val apiService = ParkingApiClient.create()
        val call = apiService.createUser(requestBody)
        call.enqueue(object : Callback<List<UserApiResponse>?> {
            override fun onResponse(
                call: Call<List<UserApiResponse>?>,
                response: Response<List<UserApiResponse>?>
            ) {
                Log.e("create user", "onResponse: ${response.code()}")
                Log.e("create user", "onResponse: ${response.body()}")
                if (response.isSuccessful){
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    Toast.makeText(this@SignUpActivity, "Sign up success", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<UserApiResponse>?>, t: Throwable) {
                Log.e("create user", "onResponse: $t")
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
}