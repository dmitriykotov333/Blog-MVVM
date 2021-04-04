package com.kotdev.blog.app.ui.viewmodels.main.blog_viewmodel

import android.net.Uri
import com.kotdev.blog.app.models.BlogPost
import com.kotdev.blog.helpers.util.BlogQueryUtils.Companion.BLOG_FILTER_DATE_UPDATED
import com.kotdev.blog.helpers.util.BlogQueryUtils.Companion.BLOG_ORDER_DESC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getIsQueryExhausted(): Boolean {
    return getCurrentViewStateOrNew().blogFields.isQueryExhausted
        ?: false
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getFilter(): String {
    return getCurrentViewStateOrNew().blogFields.filter
        ?: BLOG_FILTER_DATE_UPDATED
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getOrder(): String {
    return getCurrentViewStateOrNew().blogFields.order
        ?: BLOG_ORDER_DESC
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().blogFields.searchQuery
        ?: return ""
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getPage(): Int {
    return getCurrentViewStateOrNew().blogFields.page
        ?: return 1
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getSlug(): String {
    getCurrentViewStateOrNew().let {
        it.viewBlogFields.blogPost?.let {
            return it.slug
        }
    }
    return ""
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.isAuthorOfBlogPost(): Boolean {
    return getCurrentViewStateOrNew().viewBlogFields.isAuthorOfBlogPost
        ?: false
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.let {
            return it
        } ?: getDummyBlogPost()
    }
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getDummyBlogPost(): BlogPost {
    return BlogPost(-1, "", "", "", "", 1, "")
}

@FlowPreview
@OptIn(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}