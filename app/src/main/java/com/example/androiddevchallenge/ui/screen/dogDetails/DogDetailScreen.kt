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
package com.example.androiddevchallenge.ui.screen.dogDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.repository.DefaultData
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetBreeds
import com.example.androiddevchallenge.ui.screen.home.HomeViewModel
import com.example.androiddevchallenge.ui.screen.home.PetImage
import com.example.androiddevchallenge.ui.theme.colorTextBody

@Composable
fun DogDetailScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val selectedDog by viewModel.selectedDog.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            DogDetailTopAppBar(
                onBackButtonClicked = {
                    navController.navigateUp()
                }
            )
        }
    ) {
        selectedDog?.let { dog ->
            Column(
                modifier = Modifier
                    .verticalScroll(enabled = true, state = scrollState)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                PetImage(
                    imageUrl = dog.photos.firstOrNull()?.large,
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                DogDetailContent(
                    dog = dog,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

@Composable
fun DogDetailContent(
    dog: Pet,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        DogNameAndBreed(
            name = dog.name,
            breed = dog.breeds,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        DogDetails(
            dog = dog,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = DefaultData.defaultDescription,
            style = MaterialTheme.typography.body1,
            color = colorTextBody.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        AdopButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AdopButton(
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /*TODO*/ },
        elevation = ButtonDefaults.elevation(
            defaultElevation = 1.dp,
            pressedElevation = 2.dp
        ),
        modifier = modifier
    ) {
        Text(
            text = "Adopt Me",
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun DogNameAndBreed(
    name: String,
    breed: PetBreeds,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.h4
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "${breed.primary}, ${breed.secondary ?: ""}",
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun DogDetails(
    dog: Pet,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        DogAttribute(name = "Age", attr = dog.age ?: "Unknown")
        DogAttribute(name = "Coat", attr = dog.coat ?: "Unknown")
        DogAttribute(name = "Gender", attr = dog.gender)
    }
}

@Composable
fun DogAttribute(name: String, attr: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary.copy(alpha = 0.2f))
            .border(BorderStroke(2.dp, MaterialTheme.colors.primary))
            .padding(vertical = 16.dp, horizontal = 32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.caption
                )
            }
            Text(
                text = attr,
                style = MaterialTheme.typography.body1.copy(fontSize = 15.sp)
            )
        }
    }
}

@Composable
fun DogDetailTopAppBar(
    onBackButtonClicked: () -> Unit
) {
    TopAppBar(
        title = { },
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            IconButton(onClick = { onBackButtonClicked() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back icon"
                )
            }
        },
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = "More Icon"
                )
            }
        }
    )
}
