package com.zaf.econnecto.service

import com.zaf.econnecto.utils.AppConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    //private val baseUrl = AppConstant.URL_BASE
    private val baseUrl = "http://econnecto.com/andMVP/api/"

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logger).connectTimeout(100,TimeUnit.SECONDS).
                                                readTimeout(100,TimeUnit.SECONDS)

    private val builder = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                                        .client(okHttpClient.build())

    //Retrofit instance
    private val retrofit = builder.build()

    fun<T> buildConnectoService(serviceType : Class<T>):T{
        return retrofit.create(serviceType)
    }


}