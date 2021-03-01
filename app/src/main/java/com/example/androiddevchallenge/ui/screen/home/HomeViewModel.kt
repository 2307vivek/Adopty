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
package com.example.androiddevchallenge.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.repository.PetRepository
import com.example.androiddevchallenge.model.DogBreed
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _selectedBreed = MutableStateFlow<DogBreed?>(null)
    private val _petState = MutableStateFlow<PetState<PetListResponse>?>(null)
    private val _state = MutableStateFlow(HomeScreenViewState())

    private val _selectedDog = MutableStateFlow<Pet?>(null)

    val state: StateFlow<HomeScreenViewState>
        get() = _state

    val selectedDog: StateFlow<Pet?>
        get() = _selectedDog

    init {
        onBreedSelected(DogBreed("Affenpinscher"))

        viewModelScope.launch {
            combine(
                _petState,
                _selectedBreed,
                petRepository.getDogBreeds()
            ) { petState, selectedBreed, dogBreeds ->
                HomeScreenViewState(
                    petState = petState,
                    dogBreeds = dogBreeds,
                    selectedBreed = selectedBreed
                )
            }.collect { homeScreenViewState ->
                _state.value = homeScreenViewState
            }
        }
    }

    private fun getPets(breed: DogBreed) {
        viewModelScope.launch {
            petRepository.getPets(breed)
                .onStart { _petState.value = PetState(loading = true) }
                .map { result -> PetState.fromResult(result) }
                .collect { state -> _petState.value = state }
        }
    }

    fun onBreedSelected(breed: DogBreed) {
        _selectedBreed.value = breed
        getPets(breed)
    }

    fun onDogSelected(dog: Pet) {
        _selectedDog.value = dog
    }

    data class HomeScreenViewState(
        val petState: PetState<PetListResponse>? = null,
        val dogBreeds: List<DogBreed> = emptyList(),
        val selectedBreed: DogBreed? = null
    )
}
