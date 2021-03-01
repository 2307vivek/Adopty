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
package com.example.androiddevchallenge.data.repository

import com.example.androiddevchallenge.data.remote.PetApiService
import com.example.androiddevchallenge.model.DogBreed
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.Result
import com.example.androiddevchallenge.utils.call
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepository @Inject constructor(
    private val petApiService: PetApiService
) {
    suspend fun getPets(breed: DogBreed): Flow<Result<PetListResponse>> = flow {
        val petListResult = if (breed.name.equals("All")) {
            petApiService.getPets().call()
        } else {
            petApiService.getPets(breed = breed.name).call()
        }
        emit(petListResult)
    }.catch {
        emit(Result.Error("Something went wrong"))
    }

    suspend fun getPetsSpecialNeeds(): Flow<Result<PetListResponse>> = flow {
        val petListResult = petApiService.getPetSpecialNeed()
            .call()

        emit(petListResult)
    }.catch {
        emit(Result.Error("Something went wrong"))
    }

    fun getDogBreeds() = flow {
        val dogBreeds = listOf(
            DogBreed("All"),
            DogBreed("Affenpinscher"),
            DogBreed("American Bulldog"),
            DogBreed("Anatolian Shepherd"),
            DogBreed("Bloodhound"),
            DogBreed("Beagle"),
            DogBreed("Blue Lacy"),
            DogBreed("Carolina Dog"),
            DogBreed("Golden Retriever"),
            DogBreed("Greyhound"),
            DogBreed("Great Dane"),
            DogBreed("Labrador Retriever"),
            DogBreed("Labradoodle"),
            DogBreed("Maltipoo"),
            DogBreed("Poodle"),
            DogBreed("Pug"),
            DogBreed("Pomeranian"),
            DogBreed("Pit Bull Terrier"),
            DogBreed("Shepherd"),
            DogBreed("Sheep Dog"),
        )
        emit(dogBreeds)
    }
}
