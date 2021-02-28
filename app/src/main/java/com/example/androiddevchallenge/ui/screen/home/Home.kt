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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.DogBreed
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import com.example.androiddevchallenge.ui.screen.home.specialNeeds.DogSpecialNeedList
import com.example.androiddevchallenge.ui.screen.home.specialNeeds.PagerState
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()
    val pagerState = remember { PagerState() }

    Scaffold(
        topBar = {
            HomeScreenTopAppBar()
        },
        content = {
            DogContent(
                modifier = Modifier.fillMaxSize(),
                onBreedSelected = viewModel::onBreedSelected,
                selectedBreed = viewState.selectedBreed,
                dogBreeds = viewState.dogBreeds,
                petState = viewState.petState,
                pagerState = pagerState
            )
        }
    )
}

@Composable
fun DogContent(
    modifier: Modifier = Modifier,
    onBreedSelected: (DogBreed) -> Unit,
    selectedBreed: DogBreed?,
    dogBreeds: List<DogBreed>,
    petState: PetState<PetListResponse>?,
    pagerState: PagerState
) {
    if (dogBreeds.isNotEmpty() && selectedBreed != null) {
        Column(modifier = modifier) {
            DogBreedTabs(
                dogBreeds = dogBreeds,
                selectedBreed = selectedBreed,
                onBreedSelected = onBreedSelected,
                modifier = Modifier.fillMaxWidth()
            )

            DogBreedContent(
                pets = petState!!,
                pagerState = pagerState,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Text(text = "Not found")
    }
}

@Composable
fun DogBreedContent(
    pets: PetState<PetListResponse>,
    pagerState: PagerState,
    modifier: Modifier
) {
    var topPadding = 0

    LazyColumn(modifier = modifier) {

        item {
            DogSpecialNeedList(
                pagerState = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 26.dp)
                    .height(300.dp)
            )
        }

        if (pets.loading) {
            item { CircularProgressIndicator() }
        } else if (pets.isSuccessfull) {
            item {
                DogHeading(
                    text = "Adopt a dog today",
                    modifier = Modifier
                        .padding(bottom = 8.dp, start = 16.dp)
                )
            }
            itemsIndexed(pets.data!!.animals) { index, pet ->
                topPadding = if (index == 0) 4 else 1
                PetItem(
                    pet = pet,
                    modifier = Modifier.padding(top = topPadding.dp)
                )
            }
        }
    }
}

@Composable
fun DogHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier
    )
}

@Composable
fun PetItem(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            PetImage(
                modifier = Modifier.requiredSize(70.dp),
                imageUrl = pet.photos.firstOrNull()?.large
            )
            Spacer(modifier = Modifier.width(8.dp))
            PetName(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                pet = pet
            )
        }
    }
}

@Composable
fun PetName(
    modifier: Modifier = Modifier,
    pet: Pet
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = pet.name,
            style = MaterialTheme.typography.body1,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = pet.gender,
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

@Composable
fun PetImage(
    modifier: Modifier = Modifier,
    imageUrl: String?
) {
    Surface(shape = RoundedCornerShape(4.dp)) {
        CoilImage(
            data = imageUrl ?: "",
            contentDescription = "Pet Image",
            contentScale = ContentScale.Crop,
            fadeIn = true,
            modifier = modifier,
            error = {
                Image(
                    painter = painterResource(id = R.drawable.ic_dog),
                    contentDescription = "Dog Image not found",
                    contentScale = ContentScale.Crop
                )
            }
        )
    }
}

@Preview
@Composable
fun HomeScreenTopAppBar() {
    TopAppBar(
        title = {
            Text("Woof.")
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    )
}
