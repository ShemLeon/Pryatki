package com.leoevg.pryatki.presenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.leoevg.pryatki.R
import com.leoevg.pryatki.data.PersonEntity

@Composable
private fun ItemImage(imageName: String) {
    // Плейсхолдер в превью, AsyncImage в рантайме
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
            modifier = Modifier.padding(start = 5.dp, 2.dp),
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = 30.sp),
        )
        Text(
            count.toString(),
            modifier = Modifier.padding(2.dp),
            fontSize = 30.sp,
            color = Color.Blue
        )
    }
}

@Composable
private fun ItemControls(
    item: PersonEntity,
    onClickIncrement: (PersonEntity) -> Unit,
    onClickDecrement: (PersonEntity) -> Unit
) {
    Row {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(70.dp)
                .clickable { onClickIncrement(item) },
            painter = painterResource(id = R.drawable.icon_plus),
            contentDescription = "Add",
            tint = Color.Blue
        )
        Column(
            modifier = Modifier.padding(3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onClickDecrement(item) },
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrement",
                tint = Color.Red.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun ListItem(
    item: PersonEntity,
    onClick: (PersonEntity) -> Unit = {},
    onClickIncrement: (PersonEntity) -> Unit = {},
    onClickDecrement: (PersonEntity) -> Unit = {}
) {
    val shape = RoundedCornerShape(14.dp)
    val leftWidth = 96.dp     // фикс. ширина блока с картинкой
    val rightWidth = 120.dp   // фикс. ширина блока с иконками

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(2.dp) // внешний отступ от соседей
    ) {
        // Слой с ободком: базовый равномерный + акцент вниз‑вправо
        Box(
            Modifier
                .matchParentSize()
                .drawWithCache {
                    val baseStroke = 1.dp.toPx()
                    val accentStroke = 2.dp.toPx()
                    val r = 14.dp.toPx()

                    val baseColor = Color(0x33000000) // 20% чёрного — ровная линия по периметру
                    val accentBrush = Brush.linearGradient(
                        colors = listOf(Color.Transparent, Color(0x33000000)),
                        start = Offset.Zero,
                        end = Offset(size.width, size.height)
                    )

                    onDrawBehind {
                        // Ровный тонкий периметр — чтобы слева тоже было видно
                        drawRoundRect(
                            color = baseColor,
                            cornerRadius = CornerRadius(r, r),
                            style = Stroke(width = baseStroke)
                        )
                        // Лёгкий акцент вниз‑вправо
                        drawRoundRect(
                            brush = accentBrush,
                            cornerRadius = CornerRadius(r, r),
                            style = Stroke(width = accentStroke)
                        )
                    }
                }
        )

        // Контент
        Row(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .background(Color.White.copy(alpha = 0.3f)) // 70% прозрачности
                .clickable { onClick(item) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1) фиксированный блок с картинкой
            Box(Modifier.width(leftWidth), contentAlignment = Alignment.Center) {
                ItemImage(item.image)
            }
            // 2) центр тянется
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                ItemInfo(item.name, item.count)
            }
            // 3) фиксированный блок с иконками
            Box(Modifier.width(rightWidth), contentAlignment = Alignment.Center) {
                ItemControls(item, onClickIncrement, onClickDecrement)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    ListItem(
        item = PersonEntity(
            id = 1,
            name = "Диана",
            count = 0,
            image = "chermander.webp"
        )
    )
}