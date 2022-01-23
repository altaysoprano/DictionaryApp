package com.example.dictionary.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue

@ExperimentalMaterialApi
@Composable
fun WordSearchScreen(
    viewModel: WordInfoViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val searchQuery by viewModel.searchQuery
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is WordInfoViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchAppBar(
                text = searchQuery,
                onTextChange = {
                    viewModel.onSearch(it)
                },
                onCloseClicked = {
                    viewModel.onCloseClicked()
                },
                onSearchClicked = {
                    viewModel.onSearch(searchQuery)
                }
            )
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
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.searchedWordInfoItems.size) { i ->
                        val searchedWordInfo = state.searchedWordInfoItems[i]
                        ExpandableWordCardHistory(wordInfo = searchedWordInfo)
                    }
                    items(state.wordInfoItems.size) { i ->
                        val apiWordInfo = state.wordInfoItems[i]
                        ExpandableWordCard(
                            wordInfo = apiWordInfo,
                            {viewModel.onWordClicked(apiWordInfo)}
                        )
                    }
                }
            }
            if (state.isLoading && searchQuery.isNotBlank()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}











