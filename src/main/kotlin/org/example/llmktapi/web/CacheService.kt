package org.example.llmktapi.web

import org.example.llmktapi.web.dataclass.QueryCacheRequest
import org.example.llmktapi.web.dataclass.QueryCacheResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class CacheService(private val webClient: WebClient) {
    private val url = "http://127.0.0.1:5001"
    private val queryCacheURL = "/result_exist"
    private val addToCacheURL = "/add_result"


    fun queryCache(prompt: String): QueryCacheResponse {
        val queryUrl = url + queryCacheURL
        return webClient.get()
            .uri("$queryUrl?prompt=$prompt")
            .retrieve()
            .bodyToMono(QueryCacheResponse::class.java)
            .block()!!
    }

    fun addToCache(prompt: String, response: String, model: String): QueryCacheResponse {
        val addURL = url + addToCacheURL
        val queryRequest = QueryCacheRequest(
            prompt,
            response,
            model
        )
        return webClient.put()
            .uri(addURL)
            .bodyValue(queryRequest)
            .retrieve()
            .bodyToMono(QueryCacheResponse::class.java)
            .block()!!
    }
}