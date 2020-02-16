package com.zaf.econnecto.network_call.request_model

data class UserRegisterData (

        var email: String? = null,
        var phone: String? = null,
        var password: String? = null,
        var year_of_birth: Int? = null,
        var gender: Int? = null
)