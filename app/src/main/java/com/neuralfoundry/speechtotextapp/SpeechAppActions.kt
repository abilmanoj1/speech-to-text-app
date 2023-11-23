package com.neuralfoundry.speechtotextapp

sealed class SpeechAppActions{
    object toggleRecording: SpeechAppActions()
    object startRecording: SpeechAppActions()
    object stopRecording: SpeechAppActions()
    object clearTextArea: SpeechAppActions()
}


