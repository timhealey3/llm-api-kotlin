package org.example.llmktapi

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpTimeoutException

@RestController
class OllamaController(private val ollamaService: OllamaService) {
    private val generatingPrompt = Prompts.VOICE_PROMPT

    @GetMapping("/requestLargeOllamaResponse")
    fun requestLargeOllamaResponse(@RequestParam prompt: String): ResponseEntity<String> {
        return try {
            val response = ollamaService.queryLLM(generatingPrompt + prompt)?.response.toString()
            ResponseEntity.ok(response)
        } catch (e: HttpTimeoutException) {
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build()
        } catch (e: Exception) {
            // Check if it's a timeout wrapped in another exception
            if (e.cause is HttpTimeoutException || e.message?.contains("timeout") == true) {
                ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build()
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
            }
        }
    }
}