package com.company.timetable.data.network

import com.company.timetable.data.model.OpenAIRequest
import com.company.timetable.data.model.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("v1/chat/completions")
    suspend fun generateResponse(@Body request: OpenAIRequest): OpenAIResponse
} 