package com.neuralfoundry.speechtotextapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SpeechAppViewModel: ViewModel() {
    var state by mutableStateOf(SpeechAppState())
        private set

    fun onAction(action: SpeechAppActions){
        when(action){
            is SpeechAppActions.startRecording -> startRecordAndConversion()
            is SpeechAppActions.stopRecording -> stopRecordingAndConversion()
            is SpeechAppActions.clearTextArea -> clearTextAreaFunction()
            is SpeechAppActions.toggleRecording -> toggleRecordingFunction()
        }
    }

    private fun startRecordAndConversion(){
        state.isRecording = true
    }

    private fun stopRecordingAndConversion(){
        state.isRecording = false
    }

    private fun clearTextAreaFunction(){

    }

    private fun toggleRecordingFunction(){
        if(state.isRecording){
            stopRecordingAndConversion()
        }
        else{
            startRecordAndConversion()
        }
    }
}