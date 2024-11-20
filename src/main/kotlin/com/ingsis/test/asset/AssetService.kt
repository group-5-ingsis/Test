package com.ingsis.test.asset

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class AssetService(private val restTemplate: RestTemplate) {

  private val assetServiceBaseUrl: String = System.getenv("ASSET_SERVICE_URL")

  fun getAssetContent(container: String, key: String): String {
    val url = "$assetServiceBaseUrl/$container/$key"
    val headers = HttpHeaders().apply {
      contentType = MediaType.TEXT_EVENT_STREAM
      accept = listOf(MediaType.ALL)
    }

    return try {
      val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity<Unit>(null, headers), String::class.java)
      response.body ?: "No Content"
    } catch (e: RestClientException) {
      "Error retrieving asset content: ${e.message ?: "Unknown error"}"
    }
  }
}
