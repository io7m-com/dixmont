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


package com.io7m.dixmont.carbide.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public final class DmCAnnotations
  extends AnnotationIntrospector
{
  public DmCAnnotations()
  {

  }

  @Override
  public Boolean hasRequiredMarker(
    final AnnotatedMember m)
  {
    final var ann = this._findAnnotation(m, JsonProperty.class);
    if (ann != null) {
      return ann.required();
    }
    return null;
  }

  @Override
  public String findPropertyDefaultValue(
    final Annotated ann)
  {
    final var propertyAnnotation =
      this._findAnnotation(ann, JsonProperty.class);

    if (propertyAnnotation == null) {
      return null;
    }

    final var str = propertyAnnotation.defaultValue();
    return str.isEmpty() ? null : str;
  }

  @Override
  public PropertyName findNameForDeserialization(
    final Annotated a)
  {
    final var propertyAnnotation =
      this._findAnnotation(a, JsonProperty.class);

    if (propertyAnnotation != null) {
      var namespace = propertyAnnotation.namespace();
      if (namespace != null && namespace.isEmpty()) {
        namespace = null;
      }
      return PropertyName.construct(propertyAnnotation.value(), namespace);
    }
    return null;
  }

  @Override
  public PropertyName findNameForSerialization(
    final Annotated a)
  {
    final var propertyAnnotation =
      this._findAnnotation(a, JsonProperty.class);

    if (propertyAnnotation != null) {
      var namespace = propertyAnnotation.namespace();
      if (namespace != null && namespace.isEmpty()) {
        namespace = null;
      }
      return PropertyName.construct(propertyAnnotation.value(), namespace);
    }
    return null;
  }

  @Override
  public Version version()
  {
    return DmCVersion.version();
  }
}
