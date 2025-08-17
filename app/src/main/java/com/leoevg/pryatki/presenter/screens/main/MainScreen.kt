package com.leoevg.pryatki.presenter.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leoevg.pryatki.R
import com.leoevg.pryatki.data.PersonEntity
import com.leoevg.pryatki.presenter.ui.components.ListItem
import com.leoevg.pryatki.presenter.ui.components.NameInputRow
import com.leoevg.pryatki.presenter.ui.theme.Indigo500
import com.leoevg.pryatki.presenter.ui.theme.Violet500

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(
        factory = MainScreenViewModel.Factory
    )
) {
    val itemsList = mainScreenViewModel.personsList.collectAsState(initial = emptyList())

    MainScreenContent(
        items = itemsList.value,
        text = mainScreenViewModel.newText.value,
        errorMessage = mainScreenViewModel.errorMessage.value,
        onTextChange = { mainScreenViewModel.onEvent(MainScreenEvent.OnTextChange(it)) },
        onAddClick = { mainScreenViewModel.onEvent(MainScreenEvent.OnAddClick) },
        onItemClick = { mainScreenViewModel.onEvent(MainScreenEvent.OnItemClick(it)) },
        onIncrement = { mainScreenViewModel.onEvent(MainScreenEvent.OnIncrement(it)) },
        onDecrement = { mainScreenViewModel.onEvent(MainScreenEvent.OnDecrement(it)) },
        onDelete = { mainScreenViewModel.onEvent(MainScreenEvent.OnDelete(it)) }
    )
}

@Composable
fun MainScreenContent(
    items: List<PersonEntity>,
    text: String,
    errorMessage: String?,
    onTextChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onItemClick: (PersonEntity) -> Unit,
    onIncrement: (PersonEntity) -> Unit,
    onDecrement: (PersonEntity) -> Unit,
    onDelete: (PersonEntity) -> Unit
) {
    val gradientBrush = Brush.verticalGradient(listOf(Indigo500, Violet500))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_pattern),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.08f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            NameInputRow(
                text = text,
                onTextChange = { onTextChange(it) },
                onAddClick = { onAddClick() },
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 16.dp)
            )

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                items(items, key = { it.id ?: it.hashCode() }) { item ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                onDelete(item); true
                            } else false
                        }
                    )

                    val bgAlpha =
                        if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0f else 1f

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true,
                        backgroundContent = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = bgAlpha))
                                    .padding(end = 20.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.alpha(bgAlpha)
                                )
                            }
                        },
                        content = {
                            ListItem(
                                item = item,
                                onClick = { onItemClick(it) },
                                onClickIncrement = { onIncrement(it) },
                                onClickDecrement = { onDecrement(it) }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenContentPreview() {
    MainScreenContent(
        items = listOf(
            PersonEntity(id = 1, name = "Пикачу", count = 5, image = "pikachu.webp"),
            PersonEntity(id = 2, name = "Чермандер", count = 2, image = "chermander.webp")
        ),
        text = "Введите имя...",
        errorMessage = null,
        onTextChange = {},
        onAddClick = {},
        onItemClick = {},
        onIncrement = {},
        onDecrement = {},
        onDelete = {}
    )
}