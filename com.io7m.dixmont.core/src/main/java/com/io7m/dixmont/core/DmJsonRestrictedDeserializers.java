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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.KeyDeserializer;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.jsontype.TypeDeserializer;
import tools.jackson.databind.module.SimpleDeserializers;
import tools.jackson.databind.type.ArrayType;
import tools.jackson.databind.type.CollectionLikeType;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapLikeType;
import tools.jackson.databind.type.MapType;
import tools.jackson.databind.type.ReferenceType;

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
  public ValueDeserializer<?> findArrayDeserializer(
    final ArrayType type,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final ValueDeserializer<?> elementDeserializer)
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
  public ValueDeserializer<?> findBeanDeserializer(
    final JavaType type,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc)
  {
    this.checkAllowed(type.getRawClass().getCanonicalName());
    return super.findBeanDeserializer(type, config, beanDesc);
  }

  @Override
  public ValueDeserializer<?> findCollectionDeserializer(
    final CollectionType type,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final ValueDeserializer<?> elementDeserializer)
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
  public ValueDeserializer<?> findCollectionLikeDeserializer(
    final CollectionLikeType type,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc,
    final TypeDeserializer elementTypeDeserializer,
    final ValueDeserializer<?> elementDeserializer)
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
  public ValueDeserializer<?> findEnumDeserializer(
    final JavaType type,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc)
  {
    this.checkAllowed(type.toCanonical());
    return super.findEnumDeserializer(type, config, beanDesc);
  }

  @Override
  public ValueDeserializer<?> findTreeNodeDeserializer(
    final JavaType nodeType,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc)
  {
    this.checkAllowed(nodeType.getTypeName());
    return super.findTreeNodeDeserializer(nodeType, config, beanDesc);
  }

  @Override
  public ValueDeserializer<?> findReferenceDeserializer(
    final ReferenceType refType,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc,
    final TypeDeserializer contentTypeDeserializer,
    final ValueDeserializer<?> contentDeserializer)
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
  public ValueDeserializer<?> findMapDeserializer(
    final MapType type,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc,
    final KeyDeserializer keyDeserializer,
    final TypeDeserializer elementTypeDeserializer,
    final ValueDeserializer<?> elementDeserializer)
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
  public ValueDeserializer<?> findMapLikeDeserializer(
    final MapLikeType type,
    final DeserializationConfig config,
    final BeanDescription.Supplier beanDesc,
    final KeyDeserializer keyDeserializer,
    final TypeDeserializer elementTypeDeserializer,
    final ValueDeserializer<?> elementDeserializer)
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
