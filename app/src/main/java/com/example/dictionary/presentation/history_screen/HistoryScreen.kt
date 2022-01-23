package com.example.dictionary.presentation.history_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionary.presentation.ExpandableWordCardHistory
import com.example.dictionary.presentation.SearchAppBar

@ExperimentalMaterialApi
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val searchQuery by viewModel.searchQuery
    val scaffoldState = rememberScaffoldState()
    val limit = viewModel.limit.value

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row(modifier = Modifier.fillMaxWidth()) {
                HistorySearchBar(
                    text = searchQuery,
                    onTextChange = {
                        viewModel.onSearch(it)
                    },
                    onCloseClicked = {
                        viewModel.onCloseClicked()
                    },
                    onSearchClicked = {
                        viewModel.onSearch(query = searchQuery)
                    },
                    onClearClicked = {
                        viewModel.onClearClicked()
                    }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val listState = rememberLazyListState()

                LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                    itemsIndexed(state.wordInfoItems) { index, word ->
                        viewModel.onChangeRecipeScrollPosition(index)
                        if ((index + 1) >= limit) {
                            viewModel.nextPage()
                        }
                        ExpandableWordCardHistory(wordInfo = word)
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

    }
}