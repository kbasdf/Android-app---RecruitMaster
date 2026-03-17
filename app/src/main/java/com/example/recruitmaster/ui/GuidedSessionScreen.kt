package com.example.recruitmaster.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recruitmaster.viewmodel.SessionViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuidedSessionScreen(viewModel: SessionViewModel = viewModel()) {
    val pages = viewModel.pages
    val pagerState = rememberPagerState(pageCount = { pages.size })
    var isShowingProgressBar by remember { mutableStateOf(false) }

    // Call fetchPage when page is scrolled
    LaunchedEffect(pagerState.currentPage) {
        viewModel.fetchPage(pagerState.currentPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content (Pager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val current = pages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isShowingProgressBar = false
                    }
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = current.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                // Show chunked prefix only for Page 7 (index 6)
                if (page == 6) {
                    TextWithLineNumbers(
                        text = current.description ?: "Loading...",
                        style = MaterialTheme.typography.bodyLarge,
                        prefix = "              |from:rahul s..|__java dev....|from:Max Jon..|__interest....|from:Jay Sid..|__your pos....|from:smrudi ..|__linkedin....|from:ilya mu..|__linkedin....|from:Billing..|__your res....|from:parimat..|__all okay....|"
                    )
                } else if (page == 4) {
                    Text(
                        buildAnnotatedString {
                            append(current.description ?: "Loading...")
                            append("\n")
                            withStyle(style = SpanStyle(fontSize = 5.sp)) {
                                append("**Talent Acquisition is same as Recruitment. It is one and the same thing, often referred to eoeach other by ")
                            }
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = current.description ?: "Loading...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Left Side Detection Strip
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .align(Alignment.CenterStart)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isShowingProgressBar = true
                }
        )

        // Vertical Progress Bar
        if (isShowingProgressBar) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.14f)
                    .width(30.dp)
                    .align(BiasAlignment(horizontalBias = -1f, verticalBias = -0.5f))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                VerticalProgressBar(
                    currentPage = pagerState.currentPage,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun TextWithLineNumbers(text: String, style: TextStyle, prefix: String = "") {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val chunkLength = 15

    Row(modifier = Modifier.fillMaxWidth()) {
        // Gutter for rotating prefix chunks
        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.End
        ) {
            textLayoutResult?.let { layout ->
                for (i in 0 until layout.lineCount) {
                    // Safe modular chunking
                    val startIndex = (i * chunkLength) % prefix.length
                    val chunk = StringBuilder()
                    for (j in 0 until chunkLength) {
                        chunk.append(prefix[(startIndex + j) % prefix.length])
                    }

                    Text(
                        text = "$chunk",
                        style = style.copy(
                            fontSize = 12.sp, 
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Main Content
        Text(
            text = text,
            style = style,
            onTextLayout = { textLayoutResult = it },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun VerticalProgressBar(currentPage: Int, modifier: Modifier = Modifier) {
    val activeColor = MaterialTheme.colorScheme.primary
    val greyColor = Color.LightGray
    
    val circle1Active = true
    val circle2Active = currentPage >= 10
    val circle3Active = currentPage >= 20
    
    val line1Active = currentPage >= 10
    val line2Active = currentPage >= 20

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val totalHeight = size.height
        val circleRadius = 3.5.dp.toPx()
        
        val circle1Y = circleRadius
        val circle2Y = totalHeight / 2
        val circle3Y = totalHeight - circleRadius
        
        drawLine(
            color = if (line1Active) activeColor else greyColor,
            start = Offset(centerX, circle1Y),
            end = Offset(centerX, circle2Y),
            strokeWidth = 1.2.dp.toPx()
        )
        drawLine(
            color = if (line2Active) activeColor else greyColor,
            start = Offset(centerX, circle2Y),
            end = Offset(centerX, circle3Y),
            strokeWidth = 1.2.dp.toPx()
        )
        
        drawCircle(
            color = if (circle1Active) activeColor else greyColor,
            radius = circleRadius,
            center = Offset(centerX, circle1Y)
        )
        drawCircle(
            color = if (circle2Active) activeColor else greyColor,
            radius = circleRadius,
            center = Offset(centerX, circle2Y)
        )
        drawCircle(
            color = if (circle3Active) activeColor else greyColor,
            radius = circleRadius,
            center = Offset(centerX, circle3Y)
        )
    }
}
