package com.rickyputrah.search.ui.search


data class SearchUiState(
    var searchText: String = "",
    var searchAutocompleteList: List<String> = emptyList()
)