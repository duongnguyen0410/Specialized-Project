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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.goparking.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var btnLogIn: AppCompatButton
    private lateinit var btnSignUp: AppCompatButton

    private lateinit var tvSignUp: TextView

    private lateinit var apiService: ParkingApiService

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorStatusBar()

        apiService = ParkingApiClient.create()

        email = binding.email
        password = binding.pwd

        btnSignUp = binding.btnSignUp
        btnSignUp.setOnClickListener {
            intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogIn = binding.btnLogIn
        btnLogIn.setOnClickListener {
            checkingUser(email.text.toString(), password.text.toString())
        }
    }

    private fun checkingUser(email: String, password: String) {
        val call = apiService.loginUser(email, password)
        call.enqueue(object : Callback<UserApiResponse?> {
            override fun onResponse(
                call: Call<UserApiResponse?>,
                response: Response<UserApiResponse?>
            ) {
                if (response.isSuccessful){
                    Log.e("post user/login", "onResponse: ${response.body()}")
                    val userId = response.body()?.id
                    Log.e("userID", "onResponse: $userId")
                    val intent = Intent(this@LoginActivity, ParkingDetailsActivity::class.java)
                    intent.putExtra("userID", userId)
                    Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<UserApiResponse?>, t: Throwable) {
                Log.e("post /user/login", "onFailure: $t")
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
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