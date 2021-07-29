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
package com.example.androiddevchallenge.di.modules

import com.example.androiddevchallenge.data.remote.AuthInterceptor
import com.example.androiddevchallenge.data.remote.PetApiService
import com.example.androiddevchallenge.data.remote.PetAuthService
import com.example.androiddevchallenge.data.remote.PetAuthenticator
import com.example.androiddevchallenge.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    @Singleton
    fun providePetApiService(
        authInterceptor: AuthInterceptor,
        petAuthenticator: PetAuthenticator
    ): PetApiService =
        retrofitBuilder
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .authenticator(petAuthenticator)
                    .build()
            )
            .build()
            .create(PetApiService::class.java)

    @Provides
    @Singleton
    fun providePetAuthService(
        authInterceptor: AuthInterceptor,
    ): PetAuthService =
        retrofitBuilder
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .build()
            .create(PetAuthService::class.java)

    private val retrofitBuilder =
        Retrofit.Builder()
            .baseUrl(NetworkUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
}
