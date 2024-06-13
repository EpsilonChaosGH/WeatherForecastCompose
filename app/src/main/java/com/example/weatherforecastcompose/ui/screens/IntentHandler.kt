package com.example.weatherforecastcompose.ui.screens

interface IntentHandler<T> {
    fun obtainIntent(intent: T)
}