package com.example.weatherforecastcompose.utils

class AppSideEffect<T>(
    value: T
) {

    private var _value: T? = value

    fun get(): T? = _value.also { _value = null }
}