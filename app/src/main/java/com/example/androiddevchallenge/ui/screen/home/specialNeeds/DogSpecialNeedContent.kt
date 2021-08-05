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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.model.Pet
import com.example.androiddevchallenge.model.PetListResponse
import com.example.androiddevchallenge.model.PetState
import com.example.androiddevchallenge.ui.screen.home.DogHeading
import com.example.androiddevchallenge.ui.screen.home.PetImage

@Composable
fun DogSpecialNeedList(
    lazyListState: LazyListState,
    specialNeedsDogsState: PetState<PetListResponse>,
    onDogSelected: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        DogSpecialNeedsContent(
            specialNeedsDogsState = specialNeedsDogsState,
            lazyListState = lazyListState,
            onDogSelected = onDogSelected,
            modifier = modifier.wrapContentHeight(unbounded = true)
        )
    }
}

@Composable
fun DogSpecialNeedsContent(
    specialNeedsDogsState: PetState<PetListResponse>,
    lazyListState: LazyListState,
    onDogSelected: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DogHeading(
            text = "Dogs with special needs",
            modifier = Modifier
                .padding(bottom = 8.dp, start = 16.dp, top = 8.dp)
                .fillMaxWidth()
        )
        if (specialNeedsDogsState.loading) {
            CircularProgressIndicator()
        } else if (specialNeedsDogsState.isSuccessful) {
            DogSpecialNeedList(
                lazyListState = lazyListState,
                dogList = specialNeedsDogsState.data!!.animals,
                onDogSelected = onDogSelected,
                modifier = modifier
            )
        } else if (!specialNeedsDogsState.isSuccessful) {
            Text(text = "Error ${specialNeedsDogsState.error}")
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
