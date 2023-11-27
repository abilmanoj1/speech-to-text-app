package com.neuralfoundry.speechtotextapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neuralfoundry.speechtotextapp.ui.theme.LightGray
import com.neuralfoundry.speechtotextapp.ui.theme.SpeechToTextAppTheme
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService
import java.io.IOException

class MainActivity : ComponentActivity(), RecognitionListener {

    private val STATE_START = 0
    private val STATE_READY = 1
    private val STATE_DONE = 2
    private val STATE_FILE = 3
    private val STATE_MIC = 4

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1

    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var speechStreamService: SpeechStreamService? = null

    private val speechAppViewModel: SpeechAppViewModel by viewModels()

    fun initModel(){
        try {
            Log.d("Model Access", model?.equals(null).toString())
            StorageService.unpack(this, "model-en-us", "model",
                { model ->
                    runOnUiThread {
                        this.model = model
                        // Perform further actions after obtaining the model
                        // For example, setUiState(STATE_READY)
                        Log.d("Model Access", "Model 'model-en-us' is accessible.")
                        Log.d("Model Access", model?.equals(null).toString())
                    }
                },
                { exception ->
                    runOnUiThread {
                        Log.e("Model Access", "Failed to unpack the model: ${exception.message}")
                        // Perform actions in response to the failure to unpack the model
                        // For example, setErrorState("Failed to unpack the model ${exception.message}")
                    }
                }
            )
        } catch (e: Exception) {
            Log.e("Model Access", "Error accessing model: ${e.message}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("THE INITIAL","The program starts")
        val permissionCheck = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.RECORD_AUDIO
        )
        Log.d("THE INITIAL",permissionCheck.toString())
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        }
        else {
            Log.d("THE INITIAL","Called init model")
            initModel()
        }

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
                            VoiceToText()
//                    VoiceToText()
                        }
                        OutputTextArea(state = state)
                        CustomButton(state = state, onClick = {
                            viewModel.onAction(SpeechAppActions.ToggleRecording)
                            this@MainActivity.recognizeMicrophone()
                        })
//                        , onClick = { speechAppViewModel.onAction(SpeechAppActions.toggleRecording) }
//                        {
//
//                        }
                    }

                }
            }
        }

//        LibVosk.setLogLevel(LogLevel.INFO)


    }

    var outputText: String = ""
    override fun onPartialResult(hypothesis: String?) {
        if (hypothesis != null) {
            Log.d("THE OUTPUT",hypothesis)
        }
        outputText += hypothesis
        speechAppViewModel.onAction(SpeechAppActions.UpdateRecognisedString(hypothesis.toString()))

    }

    override fun onResult(hypothesis: String?) {
        outputText += hypothesis
        speechAppViewModel.onAction(SpeechAppActions.UpdateRecognisedString(hypothesis.toString()))
        if (hypothesis != null) {
            Log.d("THE OUTPUT",hypothesis)
        }
    }

    override fun onFinalResult(hypothesis: String?) {
        outputText += hypothesis
        speechAppViewModel.onAction(SpeechAppActions.UpdateRecognisedString(hypothesis))
        if (speechStreamService != null) {
            speechStreamService = null
        }
        if (hypothesis != null) {
            Log.d("THE OUTPUT",hypothesis)
        }
    }

    override fun onError(exception: Exception?) {
        TODO("Not yet implemented")
    }

    override fun onTimeout() {
        Log.d("TIMEOUT","The socket timed out!")
    }

    fun recognizeMicrophone() {
        Log.d("RECOGNISER DEBUG","The program starts")
        if (speechService != null) {
            Log.d("RECOGNISER DEBUG","The program entered catch if")
            speechService?.stop()
            speechService = null
        } else {
            Log.d("RECOGNISER DEBUG","The program entered catch else")
            try {
                Log.d("RECOGNISER DEBUG","The program entered catch else try start")
                val rec = Recognizer(model, 16000.0f)
                speechService = SpeechService(rec, 16000.0f)
                speechService?.startListening(this)
                Log.d("RECOGNISER DEBUG","The program entered catch else try end")
            } catch (e: IOException) {
//                setErrorState(e.message)
                Log.d("RECOGNISER DEBUG","The program entered catch")
            }
        }
        Log.d("RECOGNISER DEBUG","The program ends")
    }


}

