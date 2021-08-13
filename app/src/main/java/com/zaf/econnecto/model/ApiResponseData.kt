package com.zaf.econnecto.model

data class HomeResponse(val status: Int, val message:String, val data: HomeData)
data class HomeData(val banner_image: List<String>,val getbusiness_image: List<String> , val card_image : List<String>, val bottom_image: List<String>)