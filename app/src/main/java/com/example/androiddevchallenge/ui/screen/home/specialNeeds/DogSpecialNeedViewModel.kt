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
