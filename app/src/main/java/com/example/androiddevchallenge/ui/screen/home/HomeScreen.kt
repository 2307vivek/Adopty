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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.DogBreed
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import com.example.androiddevchallenge.ui.navigation.Screen
import com.example.androiddevchallenge.ui.screen.home.specialNeeds.DogSpecialNeedList
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onBulbClicked: () -> Unit,
    navController: NavController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewState by viewModel.state.collectAsState()
    val dogsSpecialNeedLazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            HomeScreenTopAppBar(onBulbClicked)
        },
        content = {
            DogContent(
                modifier = Modifier.fillMaxSize(),
                onBreedSelected = viewModel::onBreedSelected,
                selectedBreed = viewState.selectedBreed,
                dogBreeds = viewState.dogBreeds,
                petState = viewState.petState,
                lazyListState = dogsSpecialNeedLazyListState,
                navBackStackEntry = navBackStackEntry,
                onDogSelected = {
                    viewModel.onDogSelected(it)
                    navController.navigate(Screen.DogDetail.route)
                }
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
    lazyListState: LazyListState,
    onDogSelected: (Pet) -> Unit,
    navBackStackEntry: NavBackStackEntry
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
                lazyListState = lazyListState,
                onDogSelected = onDogSelected,
                modifier = Modifier.fillMaxSize(),
                navBackStackEntry = navBackStackEntry
            )
        }
    } else {
        Text(text = "Not found")
    }
}

@Composable
fun DogBreedContent(
    pets: PetState<PetListResponse>,
    lazyListState: LazyListState,
    onDogSelected: (Pet) -> Unit,
    navBackStackEntry: NavBackStackEntry,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            DogSpecialNeedList(
                lazyListState = lazyListState,
                navBackStackEntry = navBackStackEntry,
                onDogSelected = onDogSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .height(350.dp)
            )
        }

        item {
            DogHeading(
                text = "Adopt a dog today",
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        if (pets.loading) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        } else if (pets.isSuccessfull) {
            items(pets.data!!.animals) { pet ->
                PetItem(
                    pet,
                    onClick = onDogSelected,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
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
    onClick: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.clickable(
            enabled = true,
            onClick = { onClick(pet) }
        )
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                        contentDescription = "More icon"
                    )
                }
            }
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
    imageUrl: String?,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    elevation: Dp = 0.dp
) {
    Surface(shape = shape, elevation = elevation) {
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

@Composable
fun HomeScreenTopAppBar(
    onBulbClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text("Woof.")
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { onBulbClicked() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bulb),
                    contentDescription = "Change theme button",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    )
}
