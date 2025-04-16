/*
 * Copyright © 2025 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.io7m.dixmont.carbide.DmCarbide;
import com.io7m.dixmont.carbide.DmCarbideTypeRestrictions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DmCarbideTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(DmCarbideTest.class);

  private static void checkRejectSerialization(
    final JsonMapper mapper,
    final Object value)
  {
    final var ex =
      assertThrows(JsonProcessingException.class, () -> {
        mapper.writeValueAsString(value);
      });
    LOG.debug("writeValue: ", ex);
  }

  private static <T> void checkRejectDeserialization(
    final JsonMapper mapper,
    final String text,
    final TypeReference<T> clazz)
  {
    final var ex =
      assertThrows(JsonProcessingException.class, () -> {
        mapper.readValue(text, clazz);
      });
    LOG.debug("readValue: ", ex);
  }

  private static void checkAcceptSerialization(
    final JsonMapper mapper,
    final Object value)
    throws JsonProcessingException
  {
    LOG.debug("writeValue: ", mapper.writeValueAsString(value));
  }

  private static <T> T checkAcceptDeserialization(
    final JsonMapper mapper,
    final String text,
    final TypeReference<T> clazz)
    throws JsonProcessingException
  {
    final T x = mapper.readValue(text, clazz);
    LOG.debug("readValue: {}", x);
    return x;
  }

  @Test
  public void testRejectAll()
  {
    final var types =
      DmCarbideTypeRestrictions.builder()
        .build();

    final var builder = JsonMapper.builder();
    DmCarbide.configure(builder, types);
    final var mapper = builder.build();

    checkRejectDeserialization(
      mapper,
      "1",
      new TypeReference<Integer>()
      {
      }
    );
    checkRejectSerialization(mapper, 1);
  }

  @Test
  public void testPermitInt()
    throws Exception
  {
    final var types =
      DmCarbideTypeRestrictions.builder()
        .permit(int.class)
        .build();

    final var builder = JsonMapper.builder();
    DmCarbide.configure(builder, types);
    final var mapper = builder.build();

    checkAcceptDeserialization(
      mapper,
      "1",
      new TypeReference<Integer>()
      {
      });
    checkAcceptSerialization(mapper, 1);

    checkRejectDeserialization(
      mapper,
      "1",
      new TypeReference<Long>()
      {
      });
    checkRejectSerialization(mapper, 1L);
  }

  @Test
  public void testPermitSetInt()
    throws Exception
  {
    final var types =
      DmCarbideTypeRestrictions.builder()
        .permitSets(true)
        .permit(int.class)
        .build();

    final var builder = JsonMapper.builder();
    DmCarbide.configure(builder, types);
    final var mapper = builder.build();

    checkAcceptDeserialization(
      mapper,
      "[1]",
      new TypeReference<Set<Integer>>()
      {
      });
    checkAcceptSerialization(mapper, Set.of(1));

    checkRejectDeserialization(
      mapper,
      "1",
      new TypeReference<Set<Long>>()
      {
      });
    checkRejectSerialization(mapper, Set.of(1L));
  }

  @Test
  public void testPermitListInt()
    throws Exception
  {
    final var types =
      DmCarbideTypeRestrictions.builder()
        .permitLists(true)
        .permit(int.class)
        .build();

    final var builder = JsonMapper.builder();
    DmCarbide.configure(builder, types);
    final var mapper = builder.build();

    checkAcceptDeserialization(
      mapper,
      "[1]",
      new TypeReference<List<Integer>>()
      {
      });
    checkAcceptSerialization(mapper, List.of(1));

    checkRejectDeserialization(
      mapper,
      "1",
      new TypeReference<List<Long>>()
      {
      });
    checkRejectSerialization(mapper, List.of(1L));
  }

  @Test
  public void testPermitOptionalInt()
    throws Exception
  {
    final var types =
      DmCarbideTypeRestrictions.builder()
        .permitOptional(true)
        .permit(int.class)
        .build();

    final var builder = JsonMapper.builder();
    DmCarbide.configure(builder, types);
    builder.addModule(new Jdk8Module());
    final var mapper = builder.build();

    checkAcceptDeserialization(
      mapper,
      "1",
      new TypeReference<Optional<Integer>>()
      {
      });
    checkAcceptDeserialization(
      mapper,
      "null",
      new TypeReference<Optional<Integer>>()
      {
      });
    checkAcceptSerialization(mapper, Optional.of(1));

    checkRejectDeserialization(
      mapper,
      "1",
      new TypeReference<Optional<Long>>()
      {
      });
    checkRejectSerialization(mapper, Optional.of(1L));
  }

  @Test
  public void testPermitMap0()
    throws Exception
  {
    final var types =
      DmCarbideTypeRestrictions.builder()
        .permitMaps(true)
        .permit(String.class)
        .permit(int.class)
        .build();

    final var builder = JsonMapper.builder();
    DmCarbide.configure(builder, types);
    final var mapper = builder.build();

    checkAcceptDeserialization(
      mapper,
      """
        { "23": "x", "24": "y" }
        """,
      new TypeReference<Map<String, String>>()
      {
      });
    checkAcceptSerialization(mapper, Map.of("x", 1));

    checkRejectDeserialization(
      mapper,
      """
        { "23": 23, "24": 24 }
        """,
      new TypeReference<Map<String, Long>>()
      {
      });
    checkRejectSerialization(mapper, Map.of("x", 1L));
  }
}
