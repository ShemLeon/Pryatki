package com.leoevg.pryatki.presenter.screens.main


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leoevg.pryatki.data.PersonEntity
import com.leoevg.pryatki.presenter.components.ListItem
import com.leoevg.pryatki.presenter.components.NameInputRow

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
){
    val itemsList = mainScreenViewModel.itemsList.collectAsState(initial = emptyList())
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6366F1),
            Color(0xFF8B5CF6)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(horizontal = 10.dp),
    ){
        NameInputRow(
            text = mainScreenViewModel.newText.value,
            onTextChange = {
                mainScreenViewModel.newText.value = it
                mainScreenViewModel.errorMessage.value = null
            },
            onAddClick = { mainScreenViewModel.insertItem() },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 100.dp, bottom = 16.dp)
        )

        // Отображение ошибки
        mainScreenViewModel.errorMessage.value?.let { error ->
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
            items(itemsList.value) { item ->
                ListItem(
                    item = item,
                    onClick = {
                        mainScreenViewModel.personEntity = it
                        mainScreenViewModel.newText.value = it.name
                    },
                    onClickDelete = { mainScreenViewModel.deleteItem(it) },
                    onClickIncrement = { mainScreenViewModel.incrementCount(it) },
                    onClickDecrement = { mainScreenViewModel.decrementCount(it) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(horizontal = 10.dp),
    ) {
        NameInputRow(
            text = "Введите имя...",
            onTextChange = {},
            onAddClick = {},
            modifier = Modifier.padding(top = 100.dp, bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ListItem(
            item = PersonEntity(
                id = 1,
                name = "Пикачу",
                count = 5,
                image = "pikachu.webp"
            )
        )
    }
}