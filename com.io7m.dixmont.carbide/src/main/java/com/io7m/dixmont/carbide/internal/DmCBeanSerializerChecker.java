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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.io7m.dixmont.carbide.DmCarbideTypeRestrictions;

import java.util.Objects;

public final class DmCBeanSerializerChecker
  extends BeanSerializerModifier
{
  private final DmCarbideTypeRestrictions restrictions;

  public DmCBeanSerializerChecker(
    final DmCarbideTypeRestrictions typeRestrictions)
  {
    this.restrictions =
      Objects.requireNonNull(typeRestrictions, "typeRestrictions");
  }

  @Override
  public JsonSerializer<?> modifyArraySerializer(
    final SerializationConfig config,
    final ArrayType valueType,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }

  @Override
  public JsonSerializer<?> modifyCollectionLikeSerializer(
    final SerializationConfig config,
    final CollectionLikeType valueType,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }

  @Override
  public JsonSerializer<?> modifyCollectionSerializer(
    final SerializationConfig config,
    final CollectionType valueType,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }

  @Override
  public JsonSerializer<?> modifyEnumSerializer(
    final SerializationConfig config,
    final JavaType valueType,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }

  @Override
  public JsonSerializer<?> modifyKeySerializer(
    final SerializationConfig config,
    final JavaType valueType,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permitsKey(serializer.handledType())) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }

  @Override
  public JsonSerializer<?> modifyMapLikeSerializer(
    final SerializationConfig config,
    final MapLikeType valueType,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }

  @Override
  public JsonSerializer<?> modifyMapSerializer(
    final SerializationConfig config,
    final MapType valueType,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }

  @Override
  public JsonSerializer<?> modifySerializer(
    final SerializationConfig config,
    final BeanDescription beanDesc,
    final JsonSerializer<?> serializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return serializer;
    }
    return new DmCRejectSerializer<>();
  }
}
