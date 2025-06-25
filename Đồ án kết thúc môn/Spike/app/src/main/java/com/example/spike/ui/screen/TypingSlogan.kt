package com.example.spike.ui.components

import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier

@Composable
fun TypingSlogan(
    fullText: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 20,
    color: Color = Color.Gray,
    onTyped: (() -> Unit)? = null
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in 1..fullText.length) {
            displayedText = fullText.substring(0, i)
            delay(80)
        }
        onTyped?.invoke()
    }

    Text(
        text = displayedText,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Medium,
        color = color,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
