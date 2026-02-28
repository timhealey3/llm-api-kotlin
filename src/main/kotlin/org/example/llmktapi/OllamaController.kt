package org.example.llmktapi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OllamaController {
    private val prompts: Prompts = Prompts()
    private val generatingPrompt = prompts.VOICE_PROMPT
    @GetMapping("/requestLargeOllamaResponse")
    fun requestLargeOllamaResponse(prompt: String): String {
        return OllamaService.queryLLM(generatingPrompt + prompt)?.response.toString()
    }
}