package com.example.safeside.DataModel

import com.google.gson.annotations.SerializedName

data class Article(val email : String,
                   val userName: String,
                   val articleBody: String,
                   val articleUpVotes : String,
                   val articleReviews : String,
                   val articleTime : String,
                   val articleDate : String)