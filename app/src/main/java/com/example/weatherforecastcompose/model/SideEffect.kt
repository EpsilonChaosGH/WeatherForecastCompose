package com.example.weatherforecastcompose.model

class SideEffect<T>(
    value: T
) {

    private var _value: T? = value

    fun get(): T? = _value.also { _value = null }
}