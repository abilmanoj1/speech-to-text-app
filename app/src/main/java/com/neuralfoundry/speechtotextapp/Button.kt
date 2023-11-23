package com.neuralfoundry.speechtotextapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neuralfoundry.speechtotextapp.ui.theme.DarkYellow
import com.neuralfoundry.speechtotextapp.ui.theme.LightGray

@Composable
fun CustomButton(state: SpeechAppState, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 550.dp)
            .clickable {
                Log.d("CustomButton", "Button clicked")
                onClick() }
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp) // Increase the height of the button
                .padding(horizontal = 30.dp, vertical = 10.dp), // Adjust padding
            colors = ButtonDefaults.buttonColors(containerColor = DarkYellow)
        ) {
            Text(text = if (!state.isRecording) {
                "Start recording..."
            } else {
                "Stop recording..."
            }, color = LightGray, fontSize = 20.sp, fontFamily = customFontFamily, fontWeight = FontWeight.Bold)
        }
    }
}