package io.github.howardjohn.scanamo

import org.scalatest.FunSuite
import play.api.libs.json.{JsValue, Json}

import scala.util.Try

class PlayJsonDynamoFormatSpec extends FunSuite with DynamoFormatBehavior {
  private val parse: String => Either[Throwable, JsValue] = input => Try(Json.parse(input)).toEither

  testsFor(dynamoFormatTest(parse)(PlayJsonDynamoFormat.format))
}
