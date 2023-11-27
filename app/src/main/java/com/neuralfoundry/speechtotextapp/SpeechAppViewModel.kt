package com.neuralfoundry.speechtotextapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

class SpeechAppViewModel: ViewModel() {
    var state by mutableStateOf(SpeechAppState())
        private set

    var initialEntryBreaker: Boolean = true;
    var outputString = ""

    fun onAction(action: SpeechAppActions){
        when(action){
            is SpeechAppActions.StartRecording -> startRecordAndConversion()
            is SpeechAppActions.StopRecording -> stopRecordingAndConversion()
            is SpeechAppActions.ClearTextArea -> clearTextAreaFunction()
            is SpeechAppActions.ToggleRecording -> toggleRecordingFunction()
            is SpeechAppActions.UpdateRecognisedString -> updateRecognisedString(action.newString)
        }
    }

    private fun startRecordAndConversion(){
        state = state.copy(isRecording = true)
    }

    private fun stopRecordingAndConversion(){
        state = state.copy(isRecording = false)
    }

    private fun clearTextAreaFunction(){

    }

    private fun toggleRecordingFunction(){
        Log.d("THE INITIAL","The toggle function is clicked!!!")
        Log.d("THE INITIAL", state.isRecording.toString())
        if(state.isRecording){
            stopRecordingAndConversion()
        }
        else{
            startRecordAndConversion()
        }
        Log.d("THE INITIAL AFTER CHANGE", state.isRecording.toString())
    }

    // Function to update recognisedString in the state
     fun updateRecognisedString(newValue: String?) {
//        val jsonObject = JSONObject(newValue)
        val gson = Gson()

        try {
            val jsonObject = gson.fromJson(newValue, JsonObject::class.java)

            val partialText = jsonObject["partial"]?.asString ?: ""
            val text = jsonObject["text"]?.asString ?: ""

            // Checking if either partial or text is not blank
            if (partialText.isNotBlank() || text.isNotBlank()) {
                // Choosing partial if present, otherwise text
                outputString = if (text.isNotBlank()) text else partialText
            }

            Log.d("THE OUTPUT TEXT", outputString)

        } catch (e: Exception) {
            Log.e("Error parsing JSON", e.message ?: "Unknown error")
        }

        state = state.copy(recognisedString = outputString)
    }
}