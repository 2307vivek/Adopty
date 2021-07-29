/*
 * MIT License
 *
 * Copyright (c) 2021 Vivek Singh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
