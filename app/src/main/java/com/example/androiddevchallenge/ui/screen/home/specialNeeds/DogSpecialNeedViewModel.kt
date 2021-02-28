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
package com.example.androiddevchallenge.ui.screen.home.specialNeeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.repository.PetRepository
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogSpecialNeedViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _dogSpecialNeedsState =
        MutableStateFlow<PetState<PetListResponse>>(PetState())

    val dogsState: StateFlow<PetState<PetListResponse>>
        get() = _dogSpecialNeedsState

    init {
        getDogSpecialNeeds()
    }

    private fun getDogSpecialNeeds() {
        viewModelScope.launch {
            petRepository.getPetsSpecialNeeds()
                .onStart { _dogSpecialNeedsState.value = PetState(loading = true) }
                .map { result -> PetState.fromResult(result) }
                .collect { state -> _dogSpecialNeedsState.value = state }
        }
    }
}
