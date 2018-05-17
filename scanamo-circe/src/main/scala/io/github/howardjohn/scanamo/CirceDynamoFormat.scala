package io.github.howardjohn.scanamo

import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.gu.scanamo.DynamoFormat
import com.gu.scanamo.error.{DynamoReadError, TypeCoercionError}
import io.circe.{Decoder, Encoder}
import io.circe.parser.parse
import io.circe.syntax._

import scala.collection.JavaConverters._

object CirceDynamoFormat {
  private val placeholder = "document"

  implicit def format[T: Encoder: Decoder]: DynamoFormat[T] = new DynamoFormat[T] {
    def read(av: AttributeValue): Either[DynamoReadError, T] = {
      val rawJson = InternalUtils
        .toItemList(List(Map(placeholder -> av).asJava).asJava)
        .asScala
        .head
        .getJSON(placeholder)
      parse(rawJson)
        .flatMap(_.as[T])
        .left
        .map(f => TypeCoercionError(f))
    }

    def write(t: T): AttributeValue = {
      val item = new Item().withJSON(placeholder, t.asJson.noSpaces)
      InternalUtils.toAttributeValues(item).get(placeholder)
    }
  }
}
