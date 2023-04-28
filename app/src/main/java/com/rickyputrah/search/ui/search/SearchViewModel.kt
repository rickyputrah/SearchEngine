package com.rickyputrah.search.ui.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickyputrah.search.dispatcher.AppDispatchers
import com.rickyputrah.search.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    fun onSearchTextChanged(text: String) {
        uiState = uiState.copy(searchText = text)
        getRecommendation(text)
    }

    private fun getRecommendation(query: String) = viewModelScope.launch(appDispatchers.IO) {
        searchRepository.getRecommendation(query)
            .onFailure { ex ->
                Log.e("SearchViewModel", "Failed to get recommendation from $query", ex)
                withContext(appDispatchers.Main) {
                    uiState = uiState.copy(searchAutocompleteList = emptyList())
                }
            }
            .onSuccess { list ->
                withContext(appDispatchers.Main) {
                    uiState = uiState.copy(searchAutocompleteList = list.map { it.phrase })
                }
            }
    }
}