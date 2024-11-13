package com.ingsis.test.redis

import com.ingsis.test.asset.AssetService
import com.ingsis.test.tests.Test
import com.ingsis.test.utils.JsonUtils
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

@Component
class TestRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.test}") streamKey: String,
  @Value("\${groups.interpreter}") groupId: String,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    val streamValue = record.value
    val test = JsonUtils.deserializeTestRequest(streamValue)
    val snippetContent = assetService.getAssetContent(test.author, test.snippetId)
    

  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}

/*package com.ingsis.parse.async.format
@Component
class FormatRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.format}") streamKey: String,
  @Value("\${groups.parser}") groupId: String,
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    val streamValue = record.value

    val snippet = JsonUtil.deserializeFormatRequest(streamValue)
    val author = snippet.container
    val snippetName = snippet.key
    val snippetLanguage = snippet.language
    val snippetVersion = snippet.version

    val snippetContent = assetService.getAssetContent(author, snippetName)

    // Las reglas van a estar en formato json
    val formattingRules = assetService.getAssetContent(author, "FormattingRules")

    val language = LanguageProvider.getLanguages()[snippetLanguage]

    val result = language?.format(snippetContent, snippetVersion, formattingRules) ?: ""

    val asset = Asset(
      author,
      snippetName,
      content = result
    )

    assetService.updateAsset(asset)
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}
*/
