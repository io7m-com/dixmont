/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.dixmont.colors;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;

import java.util.regex.Pattern;

/**
 * A deserializer for color values.
 */

public final class DmColorDeserializer
  extends StdDeserializer<DmColor>
{
  private static final Pattern COLOR_PATTERN =
    Pattern.compile("#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})");

  /**
   * A deserializer for color values.
   */

  public DmColorDeserializer()
  {
    this(DmColor.class);
  }

  /**
   * A deserializer for color values.
   *
   * @param t The deserialized class
   */

  public DmColorDeserializer(
    final Class<DmColor> t)
  {
    super(t);
  }

  @Override
  public DmColor deserialize(
    final JsonParser p,
    final DeserializationContext ctxt)
  {
    final var text = p.getValueAsString();

    final var matcher = COLOR_PATTERN.matcher(text);
    if (matcher.matches()) {
      final var r = matcher.group(1);
      final var g = matcher.group(2);
      final var b = matcher.group(3);
      return new DmColor(
        (double) Integer.parseUnsignedInt(r, 16) / 255.0,
        (double) Integer.parseUnsignedInt(g, 16) / 255.0,
        (double) Integer.parseUnsignedInt(b, 16) / 255.0
      );
    }

    throw MismatchedInputException.from(
      p,
      DmColor.class,
      "Color values must match the pattern %s".formatted(COLOR_PATTERN)
    );
  }
}
