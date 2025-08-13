package com.leoevg.pryatki.presenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.leoevg.pryatki.data.PersonEntity

@Composable
private fun ItemImage(imageName: String) {
    // Пустой квадрат в превью, AsyncImage в рантайме
    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier
                .padding(6.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Red.copy(alpha = 0.3f))
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/images/$imageName")
                .build(),
            modifier = Modifier
                .padding(start = 8.dp, top = 5.dp, bottom = 5.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp)),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ItemInfo(name: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BasicText(
            name,
            modifier = Modifier
                .padding(start = 5.dp, 2.dp),
            maxLines = 1,
            autoSize = TextAutoSize
                .StepBased(
                    minFontSize = 10.sp,
                    maxFontSize = 30.sp
                ),
        )

        Text(
            count.toString(),
            modifier = Modifier
                .padding(2.dp),
            fontSize = 30.sp,
            color = Color.Blue)
    }
}

@Composable
private fun ItemControls(
    item: PersonEntity,
    onClickDelete: (PersonEntity) -> Unit,
    onClickIncrement: (PersonEntity) -> Unit,
    onClickDecrement: (PersonEntity) -> Unit
) {
    Row {
        Icon(
            modifier = Modifier.size(100.dp).clickable { onClickIncrement(item) },
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.Blue
        )
        Column(
            modifier = Modifier.padding(3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(32.dp).clickable { onClickDelete(item) },
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(32.dp).clickable { onClickDecrement(item) },
                imageVector = Icons.Default.Remove,
                contentDescription = "Remove"
            )
        }
    }
}

// Сам элемент без дублирования
@Composable
fun ListItem(
    item: PersonEntity,
    onClick: (PersonEntity) -> Unit = {},
    onClickDelete: (PersonEntity) -> Unit = {},
    onClickIncrement: (PersonEntity) -> Unit = {},
    onClickDecrement: (PersonEntity) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.LightGray.copy(alpha = 0.9f))
            .clickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemImage(item.image)
        Spacer(modifier = Modifier.weight(1f))
        ItemInfo(item.name, item.count)
        Spacer(modifier = Modifier.weight(1f))
        ItemControls(item, onClickDelete, onClickIncrement, onClickDecrement)
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview(){
    ListItem(
        item = PersonEntity(
            id = 1,
            name = "Diana",
            count = 5,
            image = "pikachu.webp"
        )
    )
}