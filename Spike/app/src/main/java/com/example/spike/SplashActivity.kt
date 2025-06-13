package com.example.spike

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spike.ui.components.StrokedText
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SplashScreen()
            }
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }
    var sloganText by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        visible = true
        val slogan = "Yoursoundtrack, your story"
        for (i in slogan.indices) {
            sloganText = slogan.substring(0, i + 1)
            delay(50)
        }
        delay(2000)

        // ✅ Kiểm tra trạng thái đăng nhập
        val sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            context.startActivity(Intent(context, MainActivity::class.java))
        } else {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
        (context as? Activity)?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000)) + scaleIn(initialScale = 0.8f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(150.dp)
                    )
                    PulseEffect()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
            ) {
                StrokedText(
                    text = sloganText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textColor = Color.LightGray,
                    strokeColor = Color.Black,
                    strokeWidth = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PulseEffect() {
    var pulse by remember { mutableStateOf(1f) }

    LaunchedEffect(true) {
        while (true) {
            pulse = 1.2f
            delay(400)
            pulse = 1f
            delay(400)
        }
    }

    Box(
        modifier = Modifier
            .size(180.dp)
            .scale(pulse)
            .background(Color.White.copy(alpha = 0.05f), shape = CircleShape)
    )
}