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

package com.io7m.dixmont.core;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A restricted serializer that only allows deserializing values from a fixed
 * list of classes.
 */

public final class DmJsonRestrictedDeserializers extends SimpleDeserializers
{
  private static final Logger LOG =
    LoggerFactory.getLogger(DmJsonRestrictedDeserializers.class);

  private final Set<String> allowClasses;

  private DmJsonRestrictedDeserializers(
    final Set<String> inAllowClasses)
  {
    this.allowClasses =
      Objects.requireNonNull(inAllowClasses, "allowClasses");
  }

  /**
   * Create a new deserializer builder.
   *
   * @return The builder
   */

  public static DmJsonRestrictedDeserializerBuilderType builder()
  {
    return new Builder();
  }

  private void checkAllowed(
    final String name)
  {
    LOG.trace("checkAllowed: {}", name);

    if (!this.allowClasses.contains(name)) {
      throw new IllegalArgumentException(
        String.format("Deserializing a value of type %s is not allowed", name)
      );
    }
  }

  @Override
  public JsonDeserializer<?> findArrayDeserializer(
    final ArrayType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkAllowed(type.toCanonical());
    return super.findArrayDeserializer(
      type,
      config,
      beanDesc,
      elementTypeDeserializer,
      elementDeserializer);
  }

  @Override
  public JsonDeserializer<?> findBeanDeserializer(
    final JavaType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc)
    throws JsonMappingException
  {
    this.checkAllowed(type.getRawClass().getCanonicalName());
    return super.findBeanDeserializer(type, config, beanDesc);
  }

  @Override
  public JsonDeserializer<?> findCollectionDeserializer(
    final CollectionType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkAllowed(type.toCanonical());
    return super.findCollectionDeserializer(
      type,
      config,
      beanDesc,
      elementTypeDeserializer,
      elementDeserializer);
  }

  @Override
  public JsonDeserializer<?> findCollectionLikeDeserializer(
    final CollectionLikeType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkAllowed(type.toCanonical());
    return super.findCollectionLikeDeserializer(
      type,
      config,
      beanDesc,
      elementTypeDeserializer,
      elementDeserializer);
  }

  @Override
  public JsonDeserializer<?> findEnumDeserializer(
    final Class<?> type,
    final DeserializationConfig config,
    final BeanDescription beanDesc)
    throws JsonMappingException
  {
    this.checkAllowed(type.getCanonicalName());
    return super.findEnumDeserializer(type, config, beanDesc);
  }

  @Override
  public JsonDeserializer<?> findTreeNodeDeserializer(
    final Class<? extends JsonNode> nodeType,
    final DeserializationConfig config,
    final BeanDescription beanDesc)
    throws JsonMappingException
  {
    this.checkAllowed(nodeType.getCanonicalName());
    return super.findTreeNodeDeserializer(nodeType, config, beanDesc);
  }

  @Override
  public JsonDeserializer<?> findReferenceDeserializer(
    final ReferenceType refType,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final TypeDeserializer contentTypeDeserializer,
    final JsonDeserializer<?> contentDeserializer)
    throws JsonMappingException
  {
    this.checkAllowed(refType.toCanonical());
    return super.findReferenceDeserializer(
      refType,
      config,
      beanDesc,
      contentTypeDeserializer,
      contentDeserializer);
  }

  @Override
  public JsonDeserializer<?> findMapDeserializer(
    final MapType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final KeyDeserializer keyDeserializer,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkAllowed(type.toCanonical());
    return super.findMapDeserializer(
      type,
      config,
      beanDesc,
      keyDeserializer,
      elementTypeDeserializer,
      elementDeserializer);
  }

  @Override
  public JsonDeserializer<?> findMapLikeDeserializer(
    final MapLikeType type,
    final DeserializationConfig config,
    final BeanDescription beanDesc,
    final KeyDeserializer keyDeserializer,
    final TypeDeserializer elementTypeDeserializer,
    final JsonDeserializer<?> elementDeserializer)
    throws JsonMappingException
  {
    this.checkAllowed(type.toCanonical());
    return super.findMapLikeDeserializer(
      type,
      config,
      beanDesc,
      keyDeserializer,
      elementTypeDeserializer,
      elementDeserializer);
  }

  private static final class Builder
    implements DmJsonRestrictedDeserializerBuilderType
  {
    private final HashSet<String> allowClasses;

    private Builder()
    {
      this.allowClasses = new HashSet<>();
    }

    @Override
    public DmJsonRestrictedDeserializerBuilderType allowClassName(
      final String name)
    {
      Objects.requireNonNull(name, "name");
      this.allowClasses.add(name);
      return this;
    }

    @Override
    public SimpleDeserializers build()
    {
      final var copyAllowClasses = Set.copyOf(this.allowClasses);
      this.allowClasses.clear();
      return new DmJsonRestrictedDeserializers(copyAllowClasses);
    }
  }
}
