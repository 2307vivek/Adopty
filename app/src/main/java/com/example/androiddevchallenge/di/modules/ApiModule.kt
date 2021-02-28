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
