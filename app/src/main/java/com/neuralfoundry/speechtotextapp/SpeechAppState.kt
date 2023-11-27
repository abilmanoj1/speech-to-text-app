package com.neuralfoundry.speechtotextapp

data class SpeechAppState(
    var isRecording: Boolean = false,
    val recognisedString: String? = ""
)
