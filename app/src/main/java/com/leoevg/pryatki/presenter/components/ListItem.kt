package com.leoevg.pryatki.presenter.components

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.leoevg.pryatki.R
import com.leoevg.pryatki.data.PersonEntity
import com.leoevg.pryatki.presenter.ui.theme.CardGlass
import com.leoevg.pryatki.presenter.ui.theme.MinusBg
import com.leoevg.pryatki.presenter.ui.theme.OnAccent
import com.leoevg.pryatki.presenter.ui.theme.PlusBg
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix as ComposeColorMatrix
import androidx.compose.ui.draw.alpha
import android.graphics.Shader
import androidx.compose.ui.graphics.asComposeRenderEffect
import android.graphics.ColorMatrix as AndroidColorMatrix
import android.graphics.ColorMatrixColorFilter

@Composable
private fun ItemImage(imageName: String) {
    // Плейсхолдер в превью, AsyncImage в рантайме
    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier
                .padding(6.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(CardGlass)
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/images/$imageName")
                .crossfade(true)
                .build(),
            modifier = Modifier
                .padding(start = 8.dp, top = 5.dp, bottom = 5.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp)),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.preview_image),
            error = painterResource(id = R.drawable.preview_image)
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
            style = TextStyle(color = Color.White) // добавить
        )
        Text(
            count.toString(),
            modifier = Modifier.padding(2.dp),
            fontSize = 30.sp,
            color = Color.White // было Color.Blue
        )
    }
}

@Composable
private fun ItemControls(
    item: PersonEntity,
    onClickIncrement: (PersonEntity) -> Unit,
    onClickDecrement: (PersonEntity) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PlusBg)
                .clickable {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClickIncrement(item)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(id = R.drawable.icon_plus),
                contentDescription = "Add",
                tint = OnAccent
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MinusBg)
                .clickable { onClickDecrement(item) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrement",
                tint = OnAccent
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

                    val baseColor = Color(0x66FFFFFF) // ~40% белого
                    val accentBrush = Brush.linearGradient(
                        colors = listOf(Color.Transparent, Color(0x26FFFFFF)), // лёгкий белый акцент
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
                .background(CardGlass) // было Color.White.copy(alpha = 0.3f)
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