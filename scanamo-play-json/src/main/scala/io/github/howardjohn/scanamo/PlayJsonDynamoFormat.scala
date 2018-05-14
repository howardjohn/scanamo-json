package io.github.howardjohn.scanamo

import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.gu.scanamo.DynamoFormat
import com.gu.scanamo.error.{DynamoReadError, TypeCoercionError}
import play.api.libs.json.{JsValue, Json}

import scala.collection.JavaConverters._
import scala.util.Try

object PlayJsonDynamoFormat {
  implicit val format: DynamoFormat[JsValue] = new DynamoFormat[JsValue] {
    private val placeholder = "document"

    def read(av: AttributeValue): Either[DynamoReadError, JsValue] =
      Try {
        Json.parse {
          InternalUtils
            .toItemList(List(Map(placeholder -> av).asJava).asJava)
            .asScala
            .head
            .getJSON(placeholder)
        }
      }.toEither.left.map(f => TypeCoercionError(f))

    def write(json: JsValue): AttributeValue = {
      val item = new Item().withJSON(placeholder, Json.stringify(json))
      InternalUtils.toAttributeValues(item).get(placeholder)
    }
  }
}
