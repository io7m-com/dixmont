dixmont
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.dixmont/com.io7m.dixmont.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.dixmont%22)
[![Maven Central (snapshot)](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fcom%2Fio7m%2Fdixmont%2Fcom.io7m.dixmont%2Fmaven-metadata.xml&style=flat-square)](https://central.sonatype.com/repository/maven-snapshots/com/io7m/dixmont/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m-com/dixmont.svg?style=flat-square)](https://codecov.io/gh/io7m-com/dixmont)
![Java Version](https://img.shields.io/badge/17-java?label=java&color=e65cc3)

![com.io7m.dixmont](./src/site/resources/dixmont.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/dixmont/main.linux.temurin.current.yml)](https://www.github.com/io7m-com/dixmont/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/dixmont/main.linux.temurin.lts.yml)](https://www.github.com/io7m-com/dixmont/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/dixmont/main.windows.temurin.current.yml)](https://www.github.com/io7m-com/dixmont/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/dixmont/main.windows.temurin.lts.yml)](https://www.github.com/io7m-com/dixmont/actions?query=workflow%3Amain.windows.temurin.lts)|

## dixmont

Some useful extension classes for [jackson](https://github.com/FasterXML/jackson).

### Features

* Restricted JSON deserializer for preventing reflection-based serialization attacks.
* Written in pure Java 17.
* [OSGi](https://www.osgi.org/) ready.
* [JPMS](https://en.wikipedia.org/wiki/Java_Platform_Module_System) ready.
* ISC license.
* High-coverage automated test suite.

### Motivation

Systems that use reflection to deserialize data are typically subject to
[deserialization attacks](https://cheatsheetseries.owasp.org/cheatsheets/Deserialization_Cheat_Sheet.html).
The [jackson](https://github.com/FasterXML/jackson) JSON library is no
exception to this.

The `dixmont` package provides a blunt and brute-force means to reduce the
impact of attacks: All of the permitted classes that can be deserialized are
listed, and everything else is rejected.

### Building

```
$ mvn clean verify
```

### Usage

Create a restricted serializer that is permitted to deserialize only the
given classes and no others, and then register it with a mapper:

```
var builder =
  DmJsonRestrictedDeserializers.builder();

// Allow deserializing values of various "value" classes...
builder.allowClass(Path.class)
  .allowClass(String.class)
  .allowClass(URI.class)
  .allowClass(int.class)
  .allowClass(double.class);

// Allow java.util.Optional<java.lang.Integer>
builder.allowOptionalOfClass(Integer.class);

// Or, equivalently:
builder.allowClassName("java.util.Optional<java.lang.Integer>")

// Allow java.util.List<java.lang.String>
builder.allowListsOfClass(String.class);

// Or, equivalently:
builder.allowClassName("java.util.List<java.lang.String>");

// Allow java.util.Set<java.lang.String>
builder.allowSetsOfClass(String.class);

// Or, equivalently:
builder.allowClassName("java.util.Set<java.lang.String>");

// Allow java.util.Map<java.lang.Integer, java.lang.String>
builder.allowMapsOfClass(Integer.class, String.class);

// Or, equivalently:
builder.allowClassName("java.util.Map<java.lang.Integer, java.lang.String>");

final var serializers = builder.build();
final var simpleModule = new SimpleModule();
simpleModule.setDeserializers(serializers);

final var mapper =
  JsonMapper.builder()
    .addModule(simpleModule)
    .build();
```

Parser code using the given `ObjectMapper` will be prevented from deserializing
values of anything other than the given classes. Hostile JSON text that attempts
to get the deserializer to instantiate other classes will fail.

