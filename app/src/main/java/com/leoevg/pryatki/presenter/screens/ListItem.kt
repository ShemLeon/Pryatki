package com.leoevg.pryatki.presenter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.leoevg.pryatki.data.PersonEntity

import com.leoevg.pryatki.R

@Composable
fun ListItem(
    item: PersonEntity,
    onClick: (PersonEntity) -> Unit = {},
    onClickDelete: (PersonEntity) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color = Color.LightGray.copy(alpha = 0.9f))
        ,

        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(start = 8.dp, top = 5.dp, bottom = 5.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp)),
            painter = painterResource(R.drawable.pikachu),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                item.name,
                modifier = Modifier
                    .padding(start = 5.dp,2.dp),
                fontSize = 30.sp
            )
            Text(
                item.count.toString(),
                modifier = Modifier
                    .padding(2.dp),
                fontSize = 30.sp,
                color = Color.Blue
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        // Плюс
        Row {
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { /* действие плюса */ },
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.Blue
            )

            Column(
                modifier = Modifier
                    .padding(all = 3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onClickDelete(item) },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",

                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { /* действие минуса */ },
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Remove"
                )
            }
        }
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
            image = R.drawable.pikachupng
        )
    )
}