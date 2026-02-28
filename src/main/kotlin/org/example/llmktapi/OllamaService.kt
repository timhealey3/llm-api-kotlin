package org.example.llmktapi

import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.models.response.OllamaResult
import org.springframework.stereotype.Service
import java.net.http.HttpTimeoutException

@Service
class OllamaService {
    private val ollamaAPI = OllamaAPI("http://localhost:11434/").apply {
        setRequestTimeoutSeconds(240)
    }

    @Throws(HttpTimeoutException::class)
    fun queryLLM(prompt: String): OllamaResult? {
        return ollamaAPI.generate("gemma3:12b", prompt, null)
    }
}