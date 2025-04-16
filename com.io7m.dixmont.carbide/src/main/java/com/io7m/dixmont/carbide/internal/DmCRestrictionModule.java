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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.io7m.dixmont.carbide.DmCarbideTypeRestrictions;

import java.util.Objects;

public final class DmCRestrictionModule extends Module
{
  private final DmCarbideTypeRestrictions restrictions;

  public DmCRestrictionModule(
    final DmCarbideTypeRestrictions typeRestrictions)
  {
    this.restrictions =
      Objects.requireNonNull(typeRestrictions, "typeRestrictions");
  }

  @Override
  public String getModuleName()
  {
    return "com.io7m.dixmon.carbide.restrictions";
  }

  @Override
  public Version version()
  {
    return DmCVersion.version();
  }

  @Override
  public void setupModule(
    final SetupContext context)
  {
    context.addBeanSerializerModifier(
      new DmCBeanSerializerChecker(this.restrictions));
    context.addBeanDeserializerModifier(
      new DmCBeanDeserializerChecker(this.restrictions));
  }
}
