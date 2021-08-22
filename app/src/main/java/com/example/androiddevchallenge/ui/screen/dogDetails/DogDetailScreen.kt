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
package com.example.androiddevchallenge.ui.screen.dogDetails

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.androiddevchallenge.ui.theme.colorDarkText
import com.example.androiddevchallenge.ui.theme.colorTextBody

@Composable
fun DogDetailScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val selectedDog by viewModel.selectedDog.collectAsState()
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            DogDetailTopAppBar(
                onBackButtonClicked = { navController.popBackStack() },
                onMoreButtonClicked = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
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
                    context = context,
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
    context: Context,
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

        Text(
            text = DefaultData.defaultDescription,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal),
            color = if (MaterialTheme.colors.isLight)
                colorTextBody.copy(alpha = 0.8f)
            else
                colorDarkText.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        AdoptButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onClick = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AdoptButton(
    modifier: Modifier = Modifier,
    onClick: (message: String) -> Unit
) {
    Button(
        onClick = {
            onClick("Not Implemented")
        },
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
            val secBreed = if (breed.secondary == null) "" else ", ${breed.secondary}"
            Text(
                text = "${breed.primary}$secBreed",
                style = MaterialTheme.typography.body2
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

    val modifier = if (MaterialTheme.colors.isLight) {
        Modifier
            .background(MaterialTheme.colors.primary.copy(alpha = 0.2f))
            .border(BorderStroke(2.dp, MaterialTheme.colors.primary))
    } else {
        Modifier
            .background(MaterialTheme.colors.onBackground.copy(alpha = 0.12f))
            .border(BorderStroke(2.dp, MaterialTheme.colors.onBackground))
    }

    Box(
        modifier = modifier
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
    onBackButtonClicked: () -> Unit,
    onMoreButtonClicked: (message: String) -> Unit
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
            IconButton(onClick = { onMoreButtonClicked("Not Implemented") }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = "More Icon"
                )
            }
        }
    )
}
