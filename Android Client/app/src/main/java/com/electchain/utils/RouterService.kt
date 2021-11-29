package com.electchain.utils

import com.electchain.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RouterService {
    @POST("signup")
    fun addUser(@Body user: User): Call<Result>

    @POST("signin/admin")
    fun adminLogin(@Body credential: Credentials): Call<Result>

    @POST("signin/voter")
    fun voterLogin(@Body user: User): Call<Result>

    @POST("signin/voter/verify")
    fun verifyOtp(@Body user: User): Call<Result>

    @POST("vote/voter-detail")
    fun voterDetail(@Body token: Token): Call<User>

    @POST("vote/is-active")
    fun isActive(@Body token: Token): Call<Status>

    @POST("vote/voting-phase")
    fun currentPhase(@Body token: Token): Call<Result>

    @POST("vote/change-phase")
    fun changePhase(@Body token: Token): Call<Result>

    @POST("vote/transfer-vote")
    fun transferVote(@Body candidate: Candidate): Call<Result>

    @POST("vote/result")
    fun getResult(@Body token: Token): Call<Result>

    @POST("candidate/register")
    fun addCandidate(@Body candidate: Candidate): Call<Result>

    @POST("candidate/list")
    fun getCandidatesList(@Body token: Token): Call<List<Candidate>>
}