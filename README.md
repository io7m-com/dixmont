dixmont
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.dixmont/com.io7m.dixmont.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.dixmont%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/com.io7m.dixmont/com.io7m.dixmont?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/dixmont/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m/dixmont.svg?style=flat-square)](https://codecov.io/gh/io7m/dixmont)

![com.io7m.dixmont](./src/site/resources/dixmont.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/dixmont/main.linux.temurin.current.yml)](https://github.com/io7m/dixmont/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m/dixmont/main.linux.temurin.lts.yml)](https://github.com/io7m/dixmont/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/dixmont/main.windows.temurin.current.yml)](https://github.com/io7m/dixmont/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m/dixmont/main.windows.temurin.lts.yml)](https://github.com/io7m/dixmont/actions?query=workflow%3Amain.windows.temurin.lts)|

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
given classes and no others, and then register it with an `ObjectMapper`:

```
var serializers =
  DmJsonRestrictedDeserializers.builder()
    .allowClass(Optional.class)
    .allowClass(Path.class)
    .allowClass(String.class)
    .allowClass(URI.class)
    .allowClass(int.class)
    .allowClass(double.class)
    .allowClass(List.class)
    .allowClassName(
      "java.util.Optional<java.lang.Integer>")
    .allowClassName(
      "java.util.List<java.lang.String>")
    .build();

var mapper =
  JsonMapper.builder()
    .build();

final var simpleModule = new SimpleModule();
simpleModule.setDeserializers(this.serializers);
mapper.registerModule(simpleModule);
```

Parser code using the given `ObjectMapper` will be prevented from deserializing
values of anything other than the given classes. Hostile JSON text that attempts
to get the deserializer to instantiate other classes will fail.

