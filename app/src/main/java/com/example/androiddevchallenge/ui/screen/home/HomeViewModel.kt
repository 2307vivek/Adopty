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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _selectedBreed = MutableStateFlow<DogBreed?>(DogBreed())
    private val _petState = MutableStateFlow<PetState<PetListResponse>>(PetState())
    private val _state = MutableStateFlow(HomeScreenViewState())
    private val _specialNeedsDogsState =
        MutableStateFlow<PetState<PetListResponse>>(PetState())

    private val _selectedDog = MutableStateFlow<Pet?>(null)
    val state: StateFlow<HomeScreenViewState>
        get() = _state

    val selectedDog: StateFlow<Pet?>
        get() = _selectedDog

    init {
        onBreedSelected(DogBreed())
        getSpecialNeedsDogs()

        viewModelScope.launch {
            combine(
                _petState,
                _specialNeedsDogsState,
                _selectedBreed,
                petRepository.getDogBreeds()
            ) { petState, specialNeedsDogsState, selectedBreed, dogBreeds ->
                HomeScreenViewState(
                    petState = petState,
                    specialNeedsDogState = specialNeedsDogsState,
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
                .collect { state -> _petState.value = state }
        }
    }

    private fun getSpecialNeedsDogs() {
        viewModelScope.launch {
            petRepository.getPetsSpecialNeeds()
                .collect { state -> _specialNeedsDogsState.value = state }
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
        val petState: PetState<PetListResponse> = PetState(),
        val specialNeedsDogState: PetState<PetListResponse> = PetState(),
        val dogBreeds: List<DogBreed> = emptyList(),
        val selectedBreed: DogBreed? = DogBreed()
    )
}
