/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.dixmont.tests;

import com.io7m.dixmont.core.DmJsonRestrictedDeserializers;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.io7m.dixmont.tests.EnumExample.ENUM_EXAMPLE_A;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public final class DmJsonRestrictedDeserializersTest
{
  private ObjectMapper createMapper(
    final Set<String> classes)
  {
    final var deserializers =
      DmJsonRestrictedDeserializers.builder()
        .allowClassNames(classes)
        .build();

    final var simpleModule = new SimpleModule();
    simpleModule.setDeserializers(deserializers);

    final var builder = JsonMapper.builder();
    builder.addModule(simpleModule);

    return builder.disable(FAIL_ON_UNKNOWN_PROPERTIES)
      .build();
  }

  @Test
  public void testNothingAllowed()
  {
    final var mapper = this.createMapper(Set.of());
    assertThrows(
      JacksonException.class, () -> {
        mapper.readValue("23", int.class);
      });
  }

  @Test
  public void testIntAllowed()
    throws Exception
  {
    final var mapper =
      this.createMapper(Set.of(int.class.getCanonicalName()));

    assertEquals(23, mapper.readValue("23", int.class));
  }

  @Test
  public void testListIntAllowed()
    throws Exception
  {
    final var mapper =
      this.createMapper(
        Set.of(
          Integer.class.getCanonicalName(),
          "java.util.List<java.lang.Integer>"
        ));

    assertEquals(
      List.of(Integer.valueOf(23)),
      mapper.readValue(
        "[23]", new TypeReference<List<Integer>>()
        {
        })
    );
  }

  @Test
  public void testArrayIntAllowed()
    throws Exception
  {
    final var mapper =
      this.createMapper(
        Set.of(
          int[].class.getName()
        ));

    assertEquals(
      Integer.valueOf(new int[]{23}[0]),
      mapper.readValue("[23]", int[].class)[0]
    );
  }

  @Test
  public void testEnumAllowed()
    throws Exception
  {
    final var mapper =
      this.createMapper(Set.of(EnumExample.class.getCanonicalName()));

    assertEquals(
      ENUM_EXAMPLE_A,
      mapper.readValue("\"ENUM_EXAMPLE_A\"", EnumExample.class)
    );
  }

  @Test
  public void testMapIntAllowed()
    throws Exception
  {
    final var mapper =
      this.createMapper(
        Set.of(
          Integer.class.getCanonicalName(),
          "java.util.Map<java.lang.Integer,java.lang.Integer>"
        ));

    assertEquals(
      Map.of(Integer.valueOf(23), Integer.valueOf(25)),
      mapper.readValue(
        "{\"23\": 25}",
        new TypeReference<Map<Integer, Integer>>()
        {
        })
    );
  }

  @Test
  public void testMapIntAllowedConvenience()
    throws Exception
  {
    final var deserializers =
      DmJsonRestrictedDeserializers.builder()
        .allowMapsOfClass(Integer.class, String.class)
        .build();

    final var simpleModule = new SimpleModule();
    simpleModule.setDeserializers(deserializers);

    final var builder = JsonMapper.builder();
    builder.addModule(simpleModule);

    final var mapper =
      (ObjectMapper) builder.disable(FAIL_ON_UNKNOWN_PROPERTIES)
        .build();

    assertEquals(
      Map.of(Integer.valueOf(23), "25"),
      mapper.readValue(
        "{\"23\": \"25\"}",
        new TypeReference<Map<Integer, String>>()
        {
        })
    );
  }

  @Test
  public void testSetIntAllowed()
    throws Exception
  {
    final var mapper =
      this.createMapper(
        Set.of(
          Integer.class.getCanonicalName(),
          "java.util.Set<java.lang.Integer>"
        ));

    assertEquals(
      Set.of(
        Integer.valueOf(23),
        Integer.valueOf(24),
        Integer.valueOf(25)),
      mapper.readValue(
        "[23,24,25]", new TypeReference<Set<Integer>>()
        {
        })
    );
  }

  @Test
  public void testSetIntAllowedConvenience()
    throws Exception
  {
    final var deserializers =
      DmJsonRestrictedDeserializers.builder()
        .allowSetsOfClass(Integer.class)
        .build();

    final var simpleModule = new SimpleModule();
    simpleModule.setDeserializers(deserializers);

    final var builder = JsonMapper.builder();
    builder.addModule(simpleModule);

    final var mapper =
      (ObjectMapper) builder.disable(FAIL_ON_UNKNOWN_PROPERTIES)
        .build();

    assertEquals(
      Set.of(
        Integer.valueOf(23),
        Integer.valueOf(24),
        Integer.valueOf(25)),
      mapper.readValue(
        "[23,24,25]", new TypeReference<Set<Integer>>()
        {
        })
    );
  }

  @Test
  public void testListIntAllowedConvenience()
    throws Exception
  {
    final var deserializers =
      DmJsonRestrictedDeserializers.builder()
        .allowListsOfClass(Integer.class)
        .build();

    final var simpleModule = new SimpleModule();
    simpleModule.setDeserializers(deserializers);

    final var builder = JsonMapper.builder();
    builder.addModule(simpleModule);

    final var mapper =
      (ObjectMapper) builder.disable(FAIL_ON_UNKNOWN_PROPERTIES)
        .build();

    assertEquals(
      List.of(
        Integer.valueOf(23),
        Integer.valueOf(24),
        Integer.valueOf(25)),
      mapper.readValue(
        "[23,24,25]", new TypeReference<List<Integer>>()
        {
        })
    );
  }
}
