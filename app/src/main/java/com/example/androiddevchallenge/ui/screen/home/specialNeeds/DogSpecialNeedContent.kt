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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import com.example.androiddevchallenge.ui.screen.home.DogHeading
import com.example.androiddevchallenge.ui.screen.home.PetImage

@Composable
fun DogSpecialNeedList(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val viewModel: DogSpecialNeedViewModel = viewModel()
    val dogState by viewModel.dogsState.collectAsState()

    DogSpecialNeedsContent(
        dogState = dogState,
        pagerState = pagerState,
        modifier = modifier
    )
}

@Composable
fun DogSpecialNeedsContent(
    dogState: PetState<PetListResponse>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    if (dogState.loading) {
        CircularProgressIndicator()
    } else if (dogState.isSuccessfull) {
        DogHeading(
            text = "Dogs needing special care",
            modifier = Modifier
                .padding(bottom = 8.dp, start = 16.dp, top = 8.dp)
        )
        DogSpecialNeedsPager(
            pagerState = pagerState,
            dogList = dogState.data!!.animals,
            modifier = modifier
        )
    } else if (!dogState.isSuccessfull) {
        Text(text = "Error ${dogState.error}")
    }
}

@Composable
fun DogSpecialNeedsPager(
    pagerState: PagerState,
    dogList: List<Pet>,
    modifier: Modifier = Modifier
) {
    pagerState.maxPage = (dogList.size - 1).coerceAtLeast(0)

    Pager(
        state = pagerState,
        modifier = modifier
    ) {
        val pet = dogList[page]
        DogSpecialNeedsItem(
            dog = pet,
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp)
                .width(140.dp)
        )
    }
}

@Composable
fun DogSpecialNeedsItem(
    dog: Pet,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        PetImage(
            imageUrl =
            dog.photos.firstOrNull()?.large,
            modifier = Modifier
                .width(140.dp)
                .height(210.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dog.name,
            maxLines = 1,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(4.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                dog.breeds.primary,
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.requiredWidth(120.dp)
            )
        }
    }
}
