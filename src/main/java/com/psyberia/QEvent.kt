package com.psyberia

abstract class QEvent(@JvmField val name: String) {
    abstract fun onCreate()
}