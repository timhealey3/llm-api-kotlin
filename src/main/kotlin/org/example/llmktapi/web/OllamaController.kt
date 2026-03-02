package org.example.llmktapi.web

import org.example.llmktapi.prompts.Prompts
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpTimeoutException

@RestController
class OllamaController(
    private val ollamaService: OllamaService,
    private val cacheService: CacheService
) {
    private val generatingPrompt = Prompts.VOICE_PROMPT

    @GetMapping("/requestLargeOllamaResponse")
    fun requestLargeOllamaResponse(@RequestParam prompt: String): ResponseEntity<String> {
        return try {
            // see if in cache
            val cacheResponse = cacheService.queryCache(prompt)
            var ollamaResponse: String = ""
            if (cacheResponse.response == null) {
                ollamaResponse = ollamaService.queryLLM(generatingPrompt + prompt)?.response.toString()
                cacheService.addToCache(prompt, ollamaResponse, "gemma3:12b")
            }
            val response = cacheResponse.response ?: ollamaResponse
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