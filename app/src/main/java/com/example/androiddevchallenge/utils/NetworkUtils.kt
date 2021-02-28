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
package com.example.androiddevchallenge.utils

import com.example.androiddevchallenge.model.Result
import retrofit2.Response

object NetworkUtils {

    const val BASE_URL = "https://api.petfinder.com/v2/"
    const val CLIENT_ID = "nNQA8PZYVmRQ4pWz2aBbdUt2IWudFaOkfZ74Els9ge5WALBmG1"
    const val SECRET_KEY = "WvE5Mwu3kUGaIYtpHwd4C1J9ePxnEtgKIJX7sxAn"
}

fun <T : Any> Response<T>.call(): Result<T> {
    val response = body()
    return if (this.isSuccessful && response != null) {
        Result.Success(response)
    } else
        Result.Error(errorBody().toString())
}
