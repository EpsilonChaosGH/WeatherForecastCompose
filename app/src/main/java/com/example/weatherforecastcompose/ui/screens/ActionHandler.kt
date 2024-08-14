package com.example.weatherforecastcompose.ui.screens

interface ActionHandler<T> {
    fun onAction(action: T)
}