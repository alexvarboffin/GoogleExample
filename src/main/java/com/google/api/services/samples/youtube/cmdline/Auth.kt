package com.google.api.services.samples.youtube.cmdline

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.samples.Config
import java.io.*

object Auth {
    /**
     * Define a global instance of the HTTP transport.
     */
    @JvmField
    val HTTP_TRANSPORT: HttpTransport = NetHttpTransport()

    /**
     * Define a global instance of the JSON factory.
     */
    @JvmField
    val JSON_FACTORY: JsonFactory = JacksonFactory()



    @JvmStatic
    @Throws(IOException::class)
    fun authorize2(
        scopes: List<String?>?, credentialDatastore: String?,
        oautth_cre: String, client_secret: String
    ): Credential {
        val `in`: InputStream = FileInputStream(client_secret)
        println(`in` == null)

        val clientSecretReader: Reader = InputStreamReader(`in`)
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader)

        // Checks that the defaults have been replaced (Default = "Enter X here").
        if (clientSecrets.details.clientId.startsWith("Enter")
            || clientSecrets.details.clientSecret.startsWith("Enter ")
        ) {
            println(
                "Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential "
                        + "into src/main/resources/client_secrets.json"
            )
            System.exit(1)
        }

        // This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
        val fileDataStoreFactory = FileDataStoreFactory(File(oautth_cre))
        val datastore = fileDataStoreFactory.getDataStore<StoredCredential>(credentialDatastore)

        val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
            .setCredentialDataStore(datastore)
            .build()


        // Build the local server and bind it to port PORT_NUMBER


        // Authorize.
        val localReceiver = LocalServerReceiver.Builder().setPort(3888).build()
        return AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user")
    }

    @JvmStatic
    @Throws(IOException::class)
    public fun authorize(scopes: List<String>, credentialDatastore: String): Credential? {
        val cfg = Config.cfgs[0]

        val clientSecretReader: Reader = InputStreamReader(FileInputStream(cfg.client_secret))
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader)

        // Checks that the defaults have been replaced (Default = "Enter X here").
        if (clientSecrets.details.clientId.startsWith("Enter")
            || clientSecrets.details.clientSecret.startsWith("Enter ")
        ) {
            println(
                "Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential "
                        + "into src/main/resources/client_secrets.json"
            )
            System.exit(1)
        }

        // This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
        val fileDataStoreFactory = FileDataStoreFactory(
            File(
                (System.getProperty("user.home") //+ "/" + Config.CREDENTIALS_DIRECTORY
                        + "/" + "." + cfg.credentialDataStoreName)
            )
        )

        val datastore = fileDataStoreFactory.getDataStore<StoredCredential>(credentialDatastore)

        val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
            .setCredentialDataStore(datastore)
            .build()


        // Build the local server and bind it to port PORT_NUMBER


        // Authorize.
        return null //new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
    }

}
