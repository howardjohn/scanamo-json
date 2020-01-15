package io.github.howardjohn.scanamo

import com.amazonaws.services.dynamodbv2.document.{Item, ItemUtils}
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import org.scanamo.{DynamoFormat, DynamoReadError, DynamoValue, TypeCoercionError}
import play.api.libs.json.{Format, Json}

import scala.collection.JavaConverters._
import scala.util.Try

object PlayJsonDynamoFormat {
  private val placeholder = "document"

  implicit def format[T: Format]: DynamoFormat[T] = new DynamoFormat[T] {
    override def read(av: DynamoValue): Either[DynamoReadError, T] = {
      val rawJson = ItemUtils
        .toItemList(List(Map(placeholder -> av.toAttributeValue).asJava).asJava)
        .asScala
        .head
        .getJSON(placeholder)
      Try(Json.parse(rawJson).as[T]).toEither.left
        .map(f => TypeCoercionError(f))
    }

    override def write(t: T): DynamoValue = {
      val item = new Item().withJSON(placeholder, Json.toJson(t).toString())
      DynamoValue.fromAttributeValue(ItemUtils.toAttributeValues(item).get(placeholder))
    }

  }
}
