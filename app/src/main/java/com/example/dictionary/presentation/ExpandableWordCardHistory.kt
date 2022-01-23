package com.example.dictionary.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dictionary.R
import com.example.dictionary.domain.model.WordInfo
import com.example.dictionary.ui.theme.Shapes

@ExperimentalMaterialApi
@Composable
fun ExpandableWordCardHistory(
    wordInfo: WordInfo
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = Shapes.large,
        backgroundColor = Color.White,
        elevation = 8.dp,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 12.dp, start = 8.dp, end = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(1f),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_access_time),
                        contentDescription = "history_icon"
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(7f),
                    text = wordInfo.word,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(1f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Text(text = wordInfo.phonetic, fontWeight = FontWeight.Light)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = wordInfo.origin)

                wordInfo.meanings.forEach { meaning ->
                    Text(text = meaning.partOfSpeech, fontWeight = FontWeight.Bold)
                    meaning.definitions.forEachIndexed() { i, definition ->
                        Text(text = "${i + 1}. ${definition.definition}")
                        Spacer(modifier = Modifier.height(8.dp))
                        definition.example?.let { example ->
                            Text(text = "Example: $example")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }
        }
    }
}