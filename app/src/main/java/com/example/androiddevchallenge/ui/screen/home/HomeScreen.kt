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

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.DogBreed
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import com.example.androiddevchallenge.ui.navigation.Screen
import com.example.androiddevchallenge.ui.screen.home.specialNeeds.DogSpecialNeedList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
) {
    val viewState by viewModel.state.collectAsState()
    val dogsSpecialNeedLazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            HomeScreenTopAppBar(isSystemInDarkTheme())
        },
        content = {
            DogContent(
                modifier = Modifier.fillMaxSize(),
                onBreedSelected = viewModel::onBreedSelected,
                selectedBreed = viewState.selectedBreed,
                dogBreeds = viewState.dogBreeds,
                petState = viewState.petState,
                specialNeedsDogsState = viewState.specialNeedsDogState,
                lazyListState = dogsSpecialNeedLazyListState,
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
    petState: PetState<PetListResponse>,
    specialNeedsDogsState: PetState<PetListResponse>,
    lazyListState: LazyListState,
    onDogSelected: (Pet) -> Unit,
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
                pets = petState,
                specialNeedsDogs = specialNeedsDogsState,
                lazyListState = lazyListState,
                onDogSelected = onDogSelected,
                modifier = Modifier.fillMaxSize(),
            )
        }
    } else {
        Text(text = "Not found")
    }
}

@Composable
fun DogBreedContent(
    pets: PetState<PetListResponse>,
    specialNeedsDogs: PetState<PetListResponse>,
    lazyListState: LazyListState,
    onDogSelected: (Pet) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            DogSpecialNeedList(
                lazyListState = lazyListState,
                specialNeedsDogsState = specialNeedsDogs,
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

        when {
            pets.loading -> {
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
            }
            pets.isSuccessful -> {
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
            pets.error != null -> {
                item {
                    Text(
                        text = pets.error,
                        modifier = Modifier.padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
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
        Image(
            painter = rememberImagePainter(
                data = imageUrl ?: "",
                builder = {
                    crossfade(true)
                    error(R.drawable.ic_dog)
                    placeholder(R.drawable.ic_placeholder)
                }
            ),
            contentDescription = "Pet Image",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )
    }
}

@Composable
fun HomeScreenTopAppBar(
    darkTheme: Boolean
) {
    TopAppBar(
        title = {
            Text("Adopty")
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { setTheme(darkTheme) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bulb),
                    contentDescription = "Change theme button",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    )
}

private fun setTheme(darkTheme: Boolean) {
    if (darkTheme) {
        setLightTheme()
    } else
        setDarkTheme()
}

private fun setLightTheme() {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}

private fun setDarkTheme() {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
}
