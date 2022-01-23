package com.example.dictionary.presentation.history_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun HistorySearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onClearClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focus = remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false)}
    val inputService = LocalTextInputService.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(6f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (focus.value != it.isFocused) {
                            focus.value = it.isFocused
                            if (!it.isFocused) {
                                onTextChange("")
                                inputService?.hideSoftwareKeyboard()
                            }
                        }
                    },
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(ContentAlpha.medium),
                        text = "Search in History...",
                        color = Color.White
                    )
                },
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                ),
                singleLine = true,
                trailingIcon = {
                    if(text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                if (text.isNotEmpty()) {
                                    onTextChange("")
                                } else {
                                    onCloseClicked()
                                }
                            }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Search Icon",
                                tint = Color.White
                            )
                        }
                    }}
                ,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked(text)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
                )
            )
            Box(modifier = Modifier.weight(1f)) {
                IconButton(onClick = { showMenu = !showMenu} ) {
                    Icon(Icons.Default.MoreVert, "")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(onClick = { openDialog = true }) {
                        Text(text= "Clear All History")
                        IconButton(onClick = { } ) {
                            Icon(Icons.Default.Delete, "")
                        } }
                }
            }
        }
    }

    if(openDialog) {
        AlertDialog(
            onDismissRequest = {openDialog = false},
            title = { Text(text = "CONFIRM DELETE", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold) },
            text = {Text(text = "Are you sure you want to clear the entire search history?", color = Color.Black)},

            confirmButton = {
                TextButton(onClick = {
                    openDialog = false
                    onClearClicked()
                }) {
                    Text(text = "Delete", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text(text = "No", color = Color.Black)
                }
            },
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.primary
        )
    }
}












