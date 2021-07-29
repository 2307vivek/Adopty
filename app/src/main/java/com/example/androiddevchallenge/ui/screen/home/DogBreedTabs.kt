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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.model.DogBreed

@Composable
fun DogBreedTabs(
    dogBreeds: List<DogBreed>,
    selectedBreed: DogBreed,
    onBreedSelected: (DogBreed) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = dogBreeds.indexOfFirst { it == selectedBreed }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 16.dp,
        backgroundColor = MaterialTheme.colors.background,
        divider = {},
        indicator = {},
        modifier = modifier
    ) {
        dogBreeds.forEachIndexed { index, dogBreed ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onBreedSelected(dogBreed) }
            ) {
                DogBreedChip(
                    breedName = dogBreed.name,
                    selected = index == selectedIndex,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun DogBreedChip(
    breedName: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onBackground.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.onPrimary
            else -> MaterialTheme.colors.onBackground
        },
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = breedName,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
