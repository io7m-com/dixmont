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

import com.fasterxml.jackson.databind.BeanDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class DmCarbideTypeRestrictions
{
  private static final Logger LOG =
    LoggerFactory.getLogger(DmCarbideTypeRestrictions.class);

  private final boolean permitLists;
  private final boolean permitSets;
  private final boolean permitMaps;
  private final boolean permitOptional;
  private final Set<Class<?>> classes;

  private DmCarbideTypeRestrictions(
    final boolean inPermitLists,
    final boolean inPermitSets,
    final boolean inPermitMaps,
    final boolean inPermitOptional,
    final Set<Class<?>> inClasses)
  {
    this.permitLists = inPermitLists;
    this.permitSets = inPermitSets;
    this.permitMaps = inPermitMaps;
    this.permitOptional = inPermitOptional;
    this.classes = Set.copyOf(inClasses);
  }

  public boolean permits(
    final BeanDescription beanDesc)
  {
    final var beanClass =
      beanDesc.getBeanClass();
    final var r =
      this.permitsCheck(beanClass);

    if (LOG.isTraceEnabled()) {
      LOG.trace("Check: {} -> {}", beanClass, r);
    }
    return r;
  }

  private boolean permitsCheck(
    final Class<?> beanClass)
  {
    if (List.class.isAssignableFrom(beanClass)) {
      return this.permitLists;
    }
    if (Set.class.isAssignableFrom(beanClass)) {
      return this.permitSets;
    }
    if (Map.class.isAssignableFrom(beanClass)) {
      return this.permitMaps;
    }
    if (Optional.class.isAssignableFrom(beanClass)) {
      return this.permitOptional;
    }
    return this.classes.contains(beanClass);
  }

  public boolean permitsKey(
    final Class<?> clazz)
  {
    final var r =
      this.permitsKeyCheck(clazz);

    if (LOG.isTraceEnabled()) {
      LOG.trace("CheckKey: {} -> {}", clazz, r);
    }
    return r;
  }

  private boolean permitsKeyCheck(
    final Class<?> clazz)
  {
    return this.classes.contains(clazz);
  }

  public static final class Builder
  {
    private boolean permitLists;
    private boolean permitSets;
    private boolean permitMaps;
    private boolean permitOptional;
    private final Set<Class<?>> classes;

    private Builder()
    {
      this.classes = new HashSet<>();
    }

    public Builder permit(
      final Class<?> clazz)
    {
      this.classes.add(clazz);
      return this;
    }

    public Builder permitLists(
      final boolean permit)
    {
      this.permitLists = permit;
      return this;
    }

    public Builder permitSets(
      final boolean permit)
    {
      this.permitSets = permit;
      return this;
    }

    public Builder permitMaps(
      final boolean permit)
    {
      this.permitMaps = permit;
      return this;
    }

    public Builder permitOptional(
      final boolean permit)
    {
      this.permitOptional = permit;
      return this;
    }

    public DmCarbideTypeRestrictions build()
    {
      return new DmCarbideTypeRestrictions(
        this.permitLists,
        this.permitSets,
        this.permitMaps,
        this.permitOptional,
        this.classes
      );
    }
  }

  public static Builder builder()
  {
    return new Builder();
  }
}
