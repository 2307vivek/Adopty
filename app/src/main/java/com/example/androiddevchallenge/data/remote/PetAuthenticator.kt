/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.data.remote

import android.util.Log
import com.example.androiddevchallenge.data.preferences.Session
import com.example.androiddevchallenge.model.AuthInfo
import com.example.androiddevchallenge.utils.NetworkUtils
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetAuthenticator @Inject constructor(
    private val petAuthService: PetAuthService,
    private val session: Session
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        val token = petAuthService
            .getAccessToken(
                AuthInfo(
                    client_id = NetworkUtils.CLIENT_ID,
                    client_secret = NetworkUtils.SECRET_KEY
                )
            )
            .execute()
        Log.d("debuggg", "Authenticator fired in")
        return if (token.code() == 200) {
            session.saveToken(token.body()!!)
            Log.d("debuggg", "Authenticator refreshed token")
            response.request().newBuilder()
                .header("Authorization", "Bearer ${token.body()!!.access_token}")
                .build()
        } else null
    }
}
