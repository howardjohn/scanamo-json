package io.github.howardjohn.scanamo

import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.gu.scanamo.DynamoFormat
import com.gu.scanamo.error.{DynamoReadError, TypeCoercionError}
import play.api.libs.json.{Format, Json}

import scala.collection.JavaConverters._
import scala.util.Try

object PlayJsonDynamoFormat {
  private val placeholder = "document"

  implicit def format[T: Format]: DynamoFormat[T] = new DynamoFormat[T] {
    def read(av: AttributeValue): Either[DynamoReadError, T] = {
      val rawJson = InternalUtils
        .toItemList(List(Map(placeholder -> av).asJava).asJava)
        .asScala
        .head
        .getJSON(placeholder)
      Try(Json.parse(rawJson).as[T]).toEither.left
        .map(f => TypeCoercionError(f))
    }

    def write(t: T): AttributeValue = {
      val item = new Item().withJSON(placeholder, Json.toJson(t).toString())
      InternalUtils.toAttributeValues(item).get(placeholder)
    }
  }
}
