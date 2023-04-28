package com.rickyputrah.search.ui.search

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rickyputrah.search.R
import com.rickyputrah.search.ui.DetailScreen
import com.rickyputrah.search.ui.textfield.SearchTextField
import com.rickyputrah.search.ui.theme.SearchTheme

@VisibleForTesting
const val SearchRecommendationItemTestTag = "SearchRecommendationItemTestTag"

data class SearchScreen(val query: String = "") : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<SearchViewModel>()
        LaunchedEffect(key1 = query) {
            viewModel.onSearchTextChanged(query)
        }
        val uiState = viewModel.uiState
        SearchViewScreen(
            uiState = uiState,
            onSearchTextChanged = viewModel::onSearchTextChanged,
            onRecommendationItemClicked = { text ->
                navigator.push(DetailScreen(text))
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchViewScreen(
        uiState: SearchUiState,
        onSearchTextChanged: (String) -> Unit,
        onRecommendationItemClicked: (String) -> Unit,
    ) {
        var searchTextField by remember { mutableStateOf(TextFieldValue(uiState.searchText)) }
        Scaffold(modifier = Modifier
            .statusBarsPadding()
            .imePadding(), topBar = {
            Surface(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                shadowElevation = 8.dp,
                tonalElevation = 8.dp,
                content = {
                    SearchTextField(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        placeholder = stringResource(R.string.search_bar_placeholder),
                        onValueChange = {
                            searchTextField = it
                            onSearchTextChanged(it.text)
                        },
                        textFieldValue = searchTextField,
                        onClearClick = {
                            searchTextField = TextFieldValue("")
                            onSearchTextChanged("")
                        },
                    )
                })
        }) { paddingValues ->
            val list = uiState.searchAutocompleteList
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
            ) {
                LazyColumn {
                    itemsIndexed(list) { index, text ->
                        SearchItem(
                            text = text,
                            onClick = onRecommendationItemClicked,
                            isLastItem = index == list.size - 1,
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchItem(
        text: String,
        onClick: (String) -> Unit,
        isLastItem: Boolean,
    ) {
        Column(
            modifier = Modifier
                .testTag(SearchRecommendationItemTestTag)
                .clickable {
                    onClick(text)
                }
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    text = text
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_diagonal_arrow),
                    contentDescription = null,
                    tint = LocalContentColor.current
                )
            }
            if (!isLastItem) {
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchTheme {
        SearchScreen().SearchViewScreen(
            uiState = SearchUiState(
                searchText = "a",
                searchAutocompleteList = listOf("google", "yahoo", "bing")
            ),
            onSearchTextChanged = {},
            onRecommendationItemClicked = {})
    }
}