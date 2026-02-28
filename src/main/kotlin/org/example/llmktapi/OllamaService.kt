package org.example.llmktapi

import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.models.response.OllamaResult

// Singleton object to set up Ollama
object OllamaService {
    private val ollamaAPI = OllamaAPI("http://localhost:11434/").apply {
        setRequestTimeoutSeconds(240)
    }

    fun queryLLM(prompt: String): OllamaResult? {
        return ollamaAPI.generate("gemma3:12b", prompt, null)
    }
}