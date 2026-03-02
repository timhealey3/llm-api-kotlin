package org.example.llmktapi.web.dataclass

data class QueryCacheRequest(
    val prompt: String,
    val response: String,
    val model: String
)
