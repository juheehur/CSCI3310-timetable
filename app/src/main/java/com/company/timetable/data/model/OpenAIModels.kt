package com.company.timetable.data.model

import com.google.gson.annotations.SerializedName

data class Message(
    val role: String,
    val content: List<MessageContent>
)

sealed class MessageContent {
    data class Text(val text: String) : MessageContent()
    data class ImageUrl(
        val imageUrl: Image
    ) : MessageContent()
}

data class Image(val url: String)

data class OpenAIRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int
)

data class OpenAIResponse(
    val id: String,
    val choices: List<Choice>,
    val created: Long,
    val model: String,
    val usage: Usage
)

data class Choice(
    val index: Int,
    val message: ChoiceMessage,
    val finish_reason: String
)

data class ChoiceMessage(
    val role: String,
    val content: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class TimetableData(
    val days: List<String>,
    val timeSlots: List<String>,
    val classes: List<ClassEntry>
)

data class ClassEntry(
    val day: String,
    val timeSlot: String,
    val className: String,
    val location: String? = null,
    val professor: String? = null
) 