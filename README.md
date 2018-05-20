# Scanamo Json
Scanamo Json provides `DynamoFormat`s for popular Scala Json libraries. The format will serialize directly to DynamoDB `AttributeValue`s, allowing full use of DynamoDB while allowing arbitrary Json objects to be stored or reusing existing formats.

# Getting started

### Circe

First, add the dependency:

```scala
    libraryDependencies += "io.github.howardjohn" %% "scanamo-circe" % "0.2.1"
```

Finally, the format can be imported with:

```scala
    import io.github.howardjohn.scanamo.CirceDynamoFormat._
```

This provides a `DynamoFormat[T]` for all `T` with both an `Encoder` and `Decoder`.

### Play Json

First, add the dependency:

```scala
    libraryDependencies += "io.github.howardjohn" %% "scanamo-play-json" % "0.2.1"
```

Finally, the format can be imported with:

```scala
    import io.github.howardjohn.scanamo.PlayJsonDynamoFormat._
```

This provides a `DynamoFormat[T]` for all `T` with a (play-json) `Format`.
