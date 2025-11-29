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

import tools.jackson.databind.module.SimpleDeserializers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A mutable builder for restricted deserializers.
 */

public interface DmJsonRestrictedDeserializerBuilderType
{
  /**
   * Allow access to the given class.
   *
   * @param clazz The class
   *
   * @return this
   */

  default DmJsonRestrictedDeserializerBuilderType allowClass(
    final Class<?> clazz)
  {
    Objects.requireNonNull(clazz, "clazz");
    return this.allowClassName(clazz.getCanonicalName());
  }

  /**
   * Allow access to the given class name.
   *
   * @param name The class name
   *
   * @return this
   */

  DmJsonRestrictedDeserializerBuilderType allowClassName(String name);

  /**
   * Allow access to the given class names.
   *
   * @param names The class names
   *
   * @return this
   */

  default DmJsonRestrictedDeserializerBuilderType allowClassNames(
    final Set<String> names)
  {
    for (final var name : names) {
      this.allowClassName(name);
    }
    return this;
  }

  /**
   * Allow access to the given classes.
   *
   * @param classes The classes
   *
   * @return this
   */

  default DmJsonRestrictedDeserializerBuilderType allowClasses(
    final Set<Class<?>> classes)
  {
    for (final var clazz : classes) {
      this.allowClass(clazz);
    }
    return this;
  }

  /**
   * Allow access to lists of the given class.
   *
   * @param clazz The class
   *
   * @return this
   *
   * @since 3.0.0
   */

  default DmJsonRestrictedDeserializerBuilderType allowListsOfClass(
    final Class<?> clazz)
  {
    return this.allowClass(clazz)
      .allowClassName(
        "%s<%s>".formatted(
          List.class.getCanonicalName(),
          clazz.getCanonicalName()
        )
      );
  }

  /**
   * Allow access to sets of the given class.
   *
   * @param clazz The class
   *
   * @return this
   *
   * @since 3.0.0
   */

  default DmJsonRestrictedDeserializerBuilderType allowSetsOfClass(
    final Class<?> clazz)
  {
    return this.allowClass(clazz)
      .allowClassName(
        "%s<%s>".formatted(
          Set.class.getCanonicalName(),
          clazz.getCanonicalName()
        )
      );
  }

  /**
   * Allow access to maps of the given key and value classes.
   *
   * @param keyClass   The key class
   * @param valueClass The value class
   *
   * @return this
   *
   * @since 3.0.0
   */

  default DmJsonRestrictedDeserializerBuilderType allowMapsOfClass(
    final Class<?> keyClass,
    final Class<?> valueClass)
  {
    return this.allowClass(keyClass)
      .allowClass(valueClass)
      .allowClassName(
        "%s<%s,%s>".formatted(
          Map.class.getCanonicalName(),
          keyClass.getCanonicalName(),
          valueClass.getCanonicalName()
        )
      );
  }

  /**
   * @return The simple deserializer
   */

  SimpleDeserializers build();
}
