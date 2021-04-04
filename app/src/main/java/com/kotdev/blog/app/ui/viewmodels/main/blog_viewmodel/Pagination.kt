package com.kotdev.blog.app.ui.viewmodels.main.blog_viewmodel

import android.util.Log
import com.kotdev.blog.app.ui.fragments.main.blog.state.BlogStateEvent
import com.kotdev.blog.app.ui.fragments.main.blog.state.BlogViewState

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.refreshFromCache() {
    if (!isJobAlreadyActive(BlogStateEvent.BlogSearchEvent())) {
        setQueryExhausted(false)
        setStateEvent(BlogStateEvent.BlogSearchEvent(false))
    }
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.loadFirstPage() {
    if (!isJobAlreadyActive(BlogStateEvent.BlogSearchEvent())) {
        setQueryExhausted(false)
        resetPage()
        setStateEvent(BlogStateEvent.BlogSearchEvent())
        Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")
    }
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
private fun BlogViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page ?: 1
    update.blogFields.page = page.plus(1)
    setViewState(update)
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.nextPage() {
    if (!isJobAlreadyActive(BlogStateEvent.BlogSearchEvent())
        && !viewState.value!!.blogFields.isQueryExhausted!!
    ) {
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setStateEvent(BlogStateEvent.BlogSearchEvent())
    }
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState) {
    viewState.blogFields.let { blogFields ->
        blogFields.blogList?.let { setBlogListData(it) }
    }
}

