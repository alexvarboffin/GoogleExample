package com.google.api.services

import com.google.api.services.samples.Config

class CFG(var credentialDataStoreName: String, var projectName: String) {

    val client_secret: String = Config.CRED_BASE + "\\" + this.projectName + "\\client_secret.json"

    override fun toString(): String {
        return "{" +
                "credentialDataStoreName='" + credentialDataStoreName + '\'' +
                ", client_secrets='" + client_secret + '\'' +
                '}'
    }
}
