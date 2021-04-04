package com.kotdev.blog.app.api.main.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotdev.blog.app.models.BlogPost

/**
 * Class for modeling the response when querying https://open-api.xyz/
 * See example response here: https://gist.github.com/mitchtabian/ae03573737067c9269701ea662460205
 */
class BlogListSearchResponse(

    @SerializedName("results")
    @Expose
    var results: List<BlogSearchResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String,
) {

    fun toList(): List<BlogPost> {
        val blogPostList: ArrayList<BlogPost> = ArrayList()
        for (blogPostResponse in results) {
            blogPostList.add(
                blogPostResponse.toBlogPost()
            )
        }
        return blogPostList
    }


    override fun toString(): String {
        return "BlogListSearchResponse(results=$results, detail='$detail')"
    }
}