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
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.io7m.dixmont.carbide.DmCarbideTypeRestrictions;

import java.util.Objects;

public final class DmCBeanDeserializerChecker
  extends BeanDeserializerModifier
{
  private final DmCarbideTypeRestrictions restrictions;

  public DmCBeanDeserializerChecker(
    final DmCarbideTypeRestrictions typeRestrictions)
  {
    this.restrictions =
      Objects.requireNonNull(typeRestrictions, "typeRestrictions");
  }

  @Override
  public JsonDeserializer<?> modifyArrayDeserializer(
    final DeserializationConfig config,
    final ArrayType valueType,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }

  @Override
  public JsonDeserializer<?> modifyCollectionDeserializer(
    final DeserializationConfig config,
    final CollectionType type,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }

  @Override
  public JsonDeserializer<?> modifyCollectionLikeDeserializer(
    final DeserializationConfig config,
    final CollectionLikeType type,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }

  @Override
  public JsonDeserializer<?> modifyDeserializer(
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }

  @Override
  public JsonDeserializer<?> modifyEnumDeserializer(
    final DeserializationConfig config,
    final JavaType type,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }

  @Override
  public KeyDeserializer modifyKeyDeserializer(
    final DeserializationConfig config,
    final JavaType type,
    final KeyDeserializer deserializer)
  {
    if (this.restrictions.permitsKey(type.getRawClass())) {
      return deserializer;
    }
    return new DmCRejectKeyDeserializer(type.getRawClass());
  }

  @Override
  public JsonDeserializer<?> modifyMapDeserializer(
    final DeserializationConfig config,
    final MapType type,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }

  @Override
  public JsonDeserializer<?> modifyMapLikeDeserializer(
    final DeserializationConfig config,
    final MapLikeType type,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }

  @Override
  public JsonDeserializer<?> modifyReferenceDeserializer(
    final DeserializationConfig config,
    final ReferenceType type,
    final BeanDescription beanDesc,
    final JsonDeserializer<?> deserializer)
  {
    if (this.restrictions.permits(beanDesc)) {
      return deserializer;
    }
    return new DmCRejectDeserializer<>(beanDesc);
  }
}
