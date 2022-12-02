package com.engie.eea_tech_interview.helpers

import com.engie.eea_tech_interview.model.Status

data class LoadingState constructor(val status: Status, val msg: String? = null) {
    companion object {
        val LOADED = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.RUNNING)
        fun error(msg: String?) = LoadingState(Status.FAILED, msg)
    }
}