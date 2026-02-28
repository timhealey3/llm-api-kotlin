package org.example.llmktapi

import io.github.ollama4j.models.response.OllamaResult
import org.example.llmktapi.prompts.Prompts
import org.example.llmktapi.web.OllamaService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.net.http.HttpTimeoutException

@SpringBootTest
class OllamaControllerTest {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @MockitoBean
    private lateinit var ollamaService: OllamaService

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun `should return success response when service returns response`() {
        val prompt = "hello"
        val expectedResponse = "Hello! I am a helpful voice assistant."
        val ollamaResult = OllamaResult(expectedResponse, "", 1000, 200)

        `when`(ollamaService.queryLLM(Prompts.VOICE_PROMPT + prompt)).thenReturn(ollamaResult)

        mockMvc.perform(get("/requestLargeOllamaResponse").param("prompt", prompt))
            .andExpect(status().isOk)
            .andExpect(content().string(expectedResponse))
    }

    @Test
    fun `should return 408 when service throws HttpTimeoutException`() {
        val prompt = "slow"

        `when`(ollamaService.queryLLM(Prompts.VOICE_PROMPT + prompt)).thenThrow(HttpTimeoutException("Request timed out"))

        mockMvc.perform(get("/requestLargeOllamaResponse").param("prompt", prompt))
            .andExpect(status().isRequestTimeout)
    }

    @Test
    fun `should return 408 when service throws exception with timeout message`() {
        val prompt = "slow"

        `when`(ollamaService.queryLLM(Prompts.VOICE_PROMPT + prompt)).thenThrow(RuntimeException("Ollama request timeout"))

        mockMvc.perform(get("/requestLargeOllamaResponse").param("prompt", prompt))
            .andExpect(status().isRequestTimeout)
    }

    @Test
    fun `should return 500 when service throws other exception`() {
        val prompt = "error"

        `when`(ollamaService.queryLLM(Prompts.VOICE_PROMPT + prompt)).thenThrow(RuntimeException("Internal error"))

        mockMvc.perform(get("/requestLargeOllamaResponse").param("prompt", prompt))
            .andExpect(status().isInternalServerError)
    }
}
