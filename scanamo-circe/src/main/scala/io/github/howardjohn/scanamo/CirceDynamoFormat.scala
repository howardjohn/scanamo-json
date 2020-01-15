package io.github.howardjohn.scanamo

import com.amazonaws.services.dynamodbv2.document.{Item, ItemUtils}
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.scanamo.{DynamoFormat, DynamoReadError, DynamoValue, TypeCoercionError}

import scala.collection.JavaConverters._
import cats.implicits._

object CirceDynamoFormat {
  private val placeholder = "document"

  implicit def format[T: Encoder: Decoder]: DynamoFormat[T] = new DynamoFormat[T] {
    override def read(av: DynamoValue): Either[DynamoReadError, T] = {
      val rawJson = ItemUtils
        .toItemList(List(Map(placeholder -> av.toAttributeValue).asJava).asJava)
        .asScala
        .head
        .getJSON(placeholder)
      decode[T](rawJson).leftMap(TypeCoercionError.apply)
    }

    override def write(t: T): DynamoValue = {
      val item = new Item().withJSON(placeholder, t.asJson.noSpaces)
      DynamoValue.fromAttributeValue(ItemUtils.toAttributeValues(item).get(placeholder))
    }
  }
}
