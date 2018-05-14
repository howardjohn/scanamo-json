package io.github.howardjohn.scanamo

import com.gu.scanamo.DynamoFormat
import org.scalatest.{Assertion, EitherValues, FunSuite, Matchers}

trait DynamoFormatBehavior extends Matchers with EitherValues { this: FunSuite =>
  def dynamoFormatTest[T](parse: String => Either[Any, T])(format: DynamoFormat[T]): Unit = {
    def roundTrip(input: String, expected: String): Assertion = {
      val json = parse(input)
      val attribute = format.write(json.right.value)
      val jsonResp = format.read(attribute)
      assert(expected === attribute.toString)
      assert(jsonResp === json)
    }

    test("empty map")(roundTrip("{}", "{M: {},}"))
    test("integer value")(roundTrip("""{"a":1}""", "{M: {a={N: 1,}},}"))
    test("string value")(roundTrip("""{"a":"b"}""", "{M: {a={S: b,}},}"))
    test("bool value")(roundTrip("""{"a":true}""", "{M: {a={BOOL: true}},}"))
    test("null value")(roundTrip("""{"a":null}""", "{M: {a={NULL: true,}},}"))
    test("map map")(roundTrip("""{"nested":{"a":1}}""", "{M: {nested={M: {a={N: 1,}},}},}"))
    test("int list value")(roundTrip("""{"a":[1,2,3]}""", "{M: {a={L: [{N: 1,}, {N: 2,}, {N: 3,}],}},}"))
    test("string list value")(roundTrip("""{"a":["b","c","d"]}""", "{M: {a={L: [{S: b,}, {S: c,}, {S: d,}],}},}"))
    test("mixed values")(roundTrip("""{"a":1,"b":"value"}""", "{M: {a={N: 1,}, b={S: value,}},}"))
    test("mixed list") {
      roundTrip("""{"a":[1,"b",false,null]}""", "{M: {a={L: [{N: 1,}, {S: b,}, {BOOL: false}, {NULL: true,}],}},}")
    }
    test("nested list") {
      roundTrip("""{"a":[1,[2,[3]]]}""", "{M: {a={L: [{N: 1,}, {L: [{N: 2,}, {L: [{N: 3,}],}],}],}},}")
    }
    test("just bool")(roundTrip("true", "{BOOL: true}"))
    test("just number")(roundTrip("1", "{N: 1,}"))
    test("just string")(roundTrip("\"string\"", "{S: string,}"))
    test("just list")(roundTrip("[1,2,3]", "{L: [{N: 1,}, {N: 2,}, {N: 3,}],}"))
  }
}
