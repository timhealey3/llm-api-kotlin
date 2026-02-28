package org.example.llmktapi.prompts

object Prompts {
    val VOICE_PROMPT: String = """
    You are a helpful voice assistant. Be clear and concise, keep answers under 4 sentences.
    Do not use markdown formatting, backticks, or special characters like *, #, or _.

    Answer this question: 
""".trimIndent()
}