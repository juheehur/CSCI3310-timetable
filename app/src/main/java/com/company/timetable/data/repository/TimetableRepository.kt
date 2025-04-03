package com.company.timetable.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.company.timetable.data.model.*
import com.company.timetable.data.network.ApiService
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimetableRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    
    private val gson = Gson()
    
    suspend fun processTimeTableImage(uri: Uri): Result<TimetableData> {
        return withContext(Dispatchers.IO) {
            try {
                val base64Image = convertUriToBase64(uri)
                
                val prompt = """
                    Analyze this timetable image and extract the schedule information. 
                    Return a structured JSON with the following format:
                    {
                      "days": ["Monday", "Tuesday", ...],
                      "timeSlots": ["9:00-10:00", "10:00-11:00", ...],
                      "classes": [
                        {
                          "day": "Monday",
                          "timeSlot": "9:00-10:00",
                          "className": "Subject Name",
                          "location": "Room Number",
                          "professor": "Professor Name"
                        },
                        ...
                      ]
                    }
                    
                    Note: Only include the JSON data in your response, nothing else.
                """.trimIndent()
                
                val message = Message(
                    role = "user", 
                    content = listOf(
                        MessageContent.Text(prompt),
                        MessageContent.ImageUrl(
                            imageUrl = Image(url = "data:image/jpeg;base64,$base64Image")
                        )
                    )
                )
                
                val request = OpenAIRequest(
                    model = "gpt-4o",
                    messages = listOf(message),
                    max_tokens = 2048
                )
                
                val response = apiService.generateResponse(request)
                val content = response.choices.firstOrNull()?.message?.content
                    ?: return@withContext Result.failure(Exception("No response content"))
                
                try {
                    val timetableData = gson.fromJson(content, TimetableData::class.java)
                    Result.success(timetableData)
                } catch (e: JsonSyntaxException) {
                    // Try to extract JSON if the response contains additional text
                    val jsonPattern = "\\{.*\\}".toRegex(RegexOption.DOT_MATCHES_ALL)
                    val jsonMatch = jsonPattern.find(content)
                    
                    if (jsonMatch != null) {
                        try {
                            val extractedJson = jsonMatch.value
                            val timetableData = gson.fromJson(extractedJson, TimetableData::class.java)
                            Result.success(timetableData)
                        } catch (e: Exception) {
                            Result.failure(Exception("Failed to parse response JSON: ${e.message}"))
                        }
                    } else {
                        Result.failure(Exception("Invalid JSON format in response"))
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    private fun convertUriToBase64(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Could not open input stream for URI: $uri")
        
        // Decode image size to estimate if we need to resize
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream.close()
        
        // Calculate sample size for reasonable memory usage
        val maxDimension = 1024
        var sampleSize = 1
        while (options.outWidth / sampleSize > maxDimension || options.outHeight / sampleSize > maxDimension) {
            sampleSize *= 2
        }
        
        // Decode with the sample size
        val decodingOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        
        val newInputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Could not open input stream for URI: $uri")
        
        val bitmap = BitmapFactory.decodeStream(newInputStream, null, decodingOptions)
        newInputStream.close()
        
        // Convert bitmap to base64
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
} 