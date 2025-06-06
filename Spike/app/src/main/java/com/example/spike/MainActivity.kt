package com.example.spike

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column {
        Text("Chào mừng bạn đến với MainActivity!")

        Button(onClick = {
            val prefs = context.getSharedPreferences("user_prefs", Activity.MODE_PRIVATE)

            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as? Activity)?.finish()
        }) {
            Text("Đăng xuất")
        }
    }
}
