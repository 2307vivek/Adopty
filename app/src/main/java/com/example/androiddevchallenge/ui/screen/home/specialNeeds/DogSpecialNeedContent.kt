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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import com.example.androiddevchallenge.ui.screen.home.DogHeading
import com.example.androiddevchallenge.ui.screen.home.PetImage

@Composable
fun DogSpecialNeedList(
    lazyListState: LazyListState,
    navBackStackEntry: NavBackStackEntry,
    onDogSelected: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: DogSpecialNeedViewModel =
        viewModel(null, HiltViewModelFactory(LocalContext.current, navBackStackEntry))
    val dogState by viewModel.dogsState.collectAsState()

    Column(modifier = modifier) {
        DogSpecialNeedsContent(
            dogState = dogState,
            lazyListState = lazyListState,
            onDogSelected = onDogSelected,
            modifier = modifier.wrapContentHeight(unbounded = true)
        )
    }
}

@Composable
fun DogSpecialNeedsContent(
    dogState: PetState<PetListResponse>,
    lazyListState: LazyListState,
    onDogSelected: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DogHeading(
            text = "Dogs needing special care",
            modifier = Modifier
                .padding(bottom = 8.dp, start = 16.dp, top = 8.dp)
                .fillMaxWidth()
        )
        if (dogState.loading) {
            CircularProgressIndicator()
        } else if (dogState.isSuccessfull) {
            DogSpecialNeedList(
                lazyListState = lazyListState,
                dogList = dogState.data!!.animals,
                onDogSelected = onDogSelected,
                modifier = modifier
            )
        } else if (!dogState.isSuccessfull) {
            Text(text = "Error ${dogState.error}")
        }
    }
}

@Composable
fun DogSpecialNeedList(
    dogList: List<Pet>,
    lazyListState: LazyListState,
    onDogSelected: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        state = lazyListState,
        modifier = modifier
    ) {
        itemsIndexed(dogList) { index, pet ->

            val padding = when (index) {
                0 -> Modifier.padding(end = 8.dp, start = 16.dp, top = 8.dp, bottom = 8.dp)
                dogList.lastIndex -> Modifier.padding(
                    end = 16.dp,
                    start = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                else -> Modifier.padding(end = 8.dp, start = 8.dp, top = 8.dp, bottom = 8.dp)
            }

            DogSpecialNeedsItem(
                dog = pet,
                onDogSelected = onDogSelected,
                modifier = padding
                    .width(140.dp)
            )
        }
    }
}

@Composable
fun DogSpecialNeedsItem(
    dog: Pet,
    onDogSelected: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.clickable(
            enabled = true,
            onClick = { onDogSelected(dog) }
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = modifier) {
            PetImage(
                imageUrl = dog.photos.firstOrNull()?.large,
                modifier = Modifier
                    .width(140.dp)
                    .height(210.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = dog.name,
                maxLines = 1,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    dog.breeds.primary ?: "Unknown",
                    maxLines = 1,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.requiredWidth(120.dp)
                )
            }
        }
    }
}
