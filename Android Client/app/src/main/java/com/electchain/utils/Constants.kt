package com.electchain.utils

import retrofit2.Retrofit

object Constants {
    lateinit var retrofit: Retrofit
    lateinit var routerService: RouterService
    lateinit var sessionManager: SessionManager
    const val BASE_URL: String = "http://192.168.29.111:3000"
    const val ADMIN_EMAIL: String = "admin@electchain.com"
}