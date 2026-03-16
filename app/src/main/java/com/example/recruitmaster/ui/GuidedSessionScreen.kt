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
import androidx.compose.ui.text.buildAnnotatedString
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
                if (page == 4) {
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

        // Vertical Progress Bar (Narrower, smaller, and shifted up)
        if (isShowingProgressBar) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.14f) // Reduced height by 20% (from 0.175f)
                    .width(30.dp) // Narrower width (reduced by 15% from 36dp)
                    // Shifted slightly up (Bias -0.5 is middle between center and top)
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
fun VerticalProgressBar(currentPage: Int, modifier: Modifier = Modifier) {
    val activeColor = MaterialTheme.colorScheme.primary // Purple title color
    val greyColor = Color.LightGray
    
    val circle1Active = true
    val circle2Active = currentPage >= 10
    val circle3Active = currentPage >= 20
    
    val line1Active = currentPage >= 10
    val line2Active = currentPage >= 20

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val totalHeight = size.height
        val circleRadius = 3.5.dp.toPx() // Smaller circles
        
        val circle1Y = circleRadius
        val circle2Y = totalHeight / 2
        val circle3Y = totalHeight - circleRadius
        
        // Lines (Narrower)
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
        
        // Circles
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
