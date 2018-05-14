package io.github.howardjohn.scanamo

import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.gu.scanamo.DynamoFormat
import com.gu.scanamo.error.{DynamoReadError, TypeCoercionError}
import io.circe.Json
import io.circe.parser.parse

import scala.collection.JavaConverters._

object CirceDynamoFormat {
  implicit val format: DynamoFormat[Json] = new DynamoFormat[Json] {
    private val placeholder = "document"

    def read(av: AttributeValue): Either[DynamoReadError, Json] =
      parse {
        InternalUtils
          .toItemList(List(Map(placeholder -> av).asJava).asJava)
          .asScala
          .head
          .getJSON(placeholder)
      }.left.map(f => TypeCoercionError(f))

    def write(json: Json): AttributeValue = {
      val item = new Item().withJSON(placeholder, json.noSpaces)
      InternalUtils.toAttributeValues(item).get(placeholder)
    }
  }
}
