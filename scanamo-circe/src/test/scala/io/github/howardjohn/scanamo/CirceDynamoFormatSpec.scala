package io.github.howardjohn.scanamo

import io.circe.Json
import io.circe.parser.parse
import org.scalatest.FunSuite

class CirceDynamoFormatSpec extends FunSuite with DynamoFormatBehavior {
  testsFor(dynamoFormatTest(parse)(CirceDynamoFormat.format[Json]))
}
