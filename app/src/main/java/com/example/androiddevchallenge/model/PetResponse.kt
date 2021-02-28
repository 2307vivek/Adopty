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
package com.example.androiddevchallenge.model

data class Pet(
    val id: Int,
    val name: String,
    val gender: String,
    val photos: List<PetPhoto>,
    val age: String,
    val breeds: PetBreeds
)

data class PetListResponse(
    val animals: List<Pet>
)

data class PetBreeds(
    val primary: String,
    val secondary: String
)

data class PetPhoto(
    val small: String,
    val medium: String,
    val large: String,
    val full: String
)
