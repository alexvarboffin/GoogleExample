package com.psyberia.outh2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

data class UploadConfig(
    val name: String,
    val cfgObj: CfgObj,
    val cfgIndex: Int
)

class UploadConfigManager {

    private val configFile = File("upload_configs.json")
    private var uploadConfigs: List<UploadConfig> = emptyList()

    init {
        if (configFile.exists()) {
            val type = object : TypeToken<List<UploadConfig>>() {}.type
            uploadConfigs = Gson().fromJson(configFile.readText(), type)
        }
    }

    fun getUploadConfigs(): List<UploadConfig> {
        return uploadConfigs
    }
}
