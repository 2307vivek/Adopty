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
package com.example.androiddevchallenge.data.repository

import com.example.androiddevchallenge.data.remote.PetApiService
import com.example.androiddevchallenge.model.DogBreed
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import com.example.androiddevchallenge.model.Result
import com.example.androiddevchallenge.utils.call
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepository @Inject constructor(
    private val petApiService: PetApiService
) {
    suspend fun getPets(breed: DogBreed): Flow<PetState<PetListResponse>> = flow {
        val petListResult = if (breed.name == "All") {
            petApiService.getPets().call()
        } else {
            petApiService.getPets(breed = breed.name).call()
        }
        emit(PetState.fromResult(petListResult))
    }.catch {
        emit(PetState.fromResult(Result.Error("Something went wrong")))
    }.onStart {
        emit(PetState(loading = true))
    }

    suspend fun getPetsSpecialNeeds(): Flow<PetState<PetListResponse>> = flow {
        val petListResult = petApiService.getPetSpecialNeed().call()

        emit(PetState.fromResult(petListResult))
    }.catch {
        emit(PetState.fromResult(Result.Error("Something went wrong")))
    }.onStart {
        emit(PetState(loading = true))
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
