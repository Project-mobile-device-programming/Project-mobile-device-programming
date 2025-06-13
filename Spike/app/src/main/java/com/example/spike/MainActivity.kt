package com.example.spike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.spike.Navigatione.AppNavigator
import com.example.spike.ui.theme.SpikeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpikeAppTheme {
                val navController = rememberNavController()
                AppNavigator(navController = navController)
            }
        }
    }
}
