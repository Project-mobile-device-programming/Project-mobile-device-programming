package com.example.spike.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun StrokedText(
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    textColor: Color = Color.Gray,
    strokeColor: Color = Color.Black,
    strokeWidth: Dp = 1.5.dp,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = strokeColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.offset(x = -strokeWidth / 2, y = -strokeWidth / 2)
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = strokeColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.offset(x = strokeWidth / 2, y = -strokeWidth / 2)
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = strokeColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.offset(x = -strokeWidth / 2, y = strokeWidth / 2)
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = strokeColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.offset(x = strokeWidth / 2, y = strokeWidth / 2)
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
