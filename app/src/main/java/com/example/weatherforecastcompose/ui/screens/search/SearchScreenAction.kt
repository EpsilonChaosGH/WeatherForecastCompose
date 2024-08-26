package com.example.weatherforecastcompose.ui.screens.search


sealed interface SearchScreenAction {

    data class SearchChanged(val searchValue: String) : SearchScreenAction

    data class SearchTriggered(val searchQuery: String) : SearchScreenAction

    data object ClearRecentSearches : SearchScreenAction

    data object BackButtonClicked : SearchScreenAction
}