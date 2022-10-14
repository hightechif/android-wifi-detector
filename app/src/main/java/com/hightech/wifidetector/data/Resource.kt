package com.hightech.wifidetector.data

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {

    open var status: Status = Status.LOADING

    sealed class Status {
        object SUCCESS : Status()
        object ERROR : Status()
        object LOADING : Status()
        object UNAUTHORIZED : Status()
    }

    class Success<T>(data: T?) : Resource<T>(data) {
        override var status: Status = Status.SUCCESS
    }

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message) {
        override var status: Status = Status.ERROR
    }

    class Loading<T>(data: T? = null) : Resource<T>(data) {
        override var status: Status = Status.LOADING
    }

    class Unauthorized<T>() : Resource<T>() {
        override var status: Status = Status.UNAUTHORIZED
    }

}

