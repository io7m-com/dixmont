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


package com.io7m.dixmont.carbide;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.io7m.dixmont.carbide.internal.DmCAnnotations;
import com.io7m.dixmont.carbide.internal.DmCRestrictionModule;

import java.util.Objects;

public final class DmCarbide
{
  private DmCarbide()
  {

  }

  public static void configure(
    final MapperBuilder<? extends ObjectMapper, ?> builder,
    final DmCarbideTypeRestrictions typeRestrictions)
  {
    Objects.requireNonNull(builder, "builder");

    builder.disable(MapperFeature.AUTO_DETECT_FIELDS);
    builder.disable(MapperFeature.AUTO_DETECT_CREATORS);
    builder.disable(MapperFeature.AUTO_DETECT_GETTERS);
    builder.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
    builder.disable(MapperFeature.AUTO_DETECT_SETTERS);

    builder.deactivateDefaultTyping();
    builder.annotationIntrospector(new DmCAnnotations());
    builder.addModule(new DmCRestrictionModule(typeRestrictions));
  }
}
