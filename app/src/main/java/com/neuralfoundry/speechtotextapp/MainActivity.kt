package com.neuralfoundry.speechtotextapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import org.vosk.android.RecognitionListener
import org.vosk.Recognizer
import org.vosk.Model
import org.vosk.android.SpeechStreamService
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.neuralfoundry.speechtotextapp.ui.theme.LightGray
import org.vosk.android.SpeechService

import org.vosk.LogLevel
import com.neuralfoundry.speechtotextapp.ui.theme.SpeechToTextAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vosk.LibVosk
import java.io.IOException
import java.lang.Exception
import java.util.logging.LogManager

class MainActivity : ComponentActivity() {

    private val STATE_START = 0
    private val STATE_READY = 1
    private val STATE_DONE = 2
    private val STATE_FILE = 3
    private val STATE_MIC = 4

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1

    private var model: Model? = null
    private var speechService: Recognizer? = null
    private var speechStreamService: SpeechStreamService? = null

    private val speechAppViewModel: SpeechAppViewModel by viewModels()

        private val recognitionListenerImplementation = object : RecognitionListener {
        override fun onPartialResult(hypothesis: String?) {
            TODO("Not yet implemented")
        }

        override fun onResult(hypothesis: String?) {
            TODO("Not yet implemented")
        }

        override fun onFinalResult(hypothesis: String?) {
            TODO("Not yet implemented")
        }

        override fun onError(exception: Exception?) {
            TODO("Not yet implemented")
        }

        override fun onTimeout() {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeechToTextAppTheme {
                // A surface container using the 'background' color from the theme
                val viewModel = viewModel<SpeechAppViewModel>()
                val state = viewModel.state

                Column{
                    Header(title = "Speech to Text")
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(modifier = Modifier
                            .background(LightGray)
                            .fillMaxSize()
                        ){
                            VoiceToText(interfaceImplementation = recognitionListenerImplementation,
                                state = state)
//                    VoiceToText()
                        }
                        OutputTextArea(state = state)
                        CustomButton(state = state){
                            speechAppViewModel.onAction(SpeechAppActions.toggleRecording)
                        }
                    }

                }
            }
        }

//        LibVosk.setLogLevel(LogLevel.INFO)
        val permissionCheck = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.RECORD_AUDIO
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        } else {
//            initModel()
        }


    }


}

