package com.example.recruitmaster.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recruitmaster.viewmodel.SessionViewModel
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuidedSessionScreen(viewModel: SessionViewModel = viewModel()) {
    val pages = viewModel.pages
    val pagerState = rememberPagerState(pageCount = { pages.size })
    
    // Call fetchPage when page is scrolled
    LaunchedEffect(pagerState.currentPage) {
        viewModel.fetchPage(pagerState.currentPage)
    }
    
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val current = pages[page]
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                        // Normal description first
                        append(current.description ?: "Loading...")
                        append("\n") // line break
                        // Last line styled smaller
                        withStyle(style = SpanStyle(fontSize = 5.sp)) {
                            append("**Talent Acquisition is same as Recruitment. It is one and the same thing, often referred to eoeach other by ")
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                // Default rendering for other pages
                Text(
                    text = current.description ?: "Loading...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
