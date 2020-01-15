package io.github.howardjohn.scanamo

import io.circe.Json
import io.circe.parser.parse
import org.scalatest.funsuite.AnyFunSuite

class CirceDynamoFormatSpec extends AnyFunSuite with DynamoFormatBehavior {
  testsFor(dynamoFormatTest(parse)(CirceDynamoFormat.format[Json]))
}
