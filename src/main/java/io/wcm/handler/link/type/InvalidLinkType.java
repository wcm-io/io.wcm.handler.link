/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2024 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.handler.link.type;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.spi.LinkType;

/**
 * This link type is used for invalid links. It must not be registered explicitly.
 */
@Model(adaptables = {
    SlingHttpServletRequest.class, Resource.class
})
@ProviderType
public final class InvalidLinkType extends LinkType {

  /**
   * Link type ID
   */
  public static final @NotNull String ID = "invalid";

  /**
   * @return Link type ID (is stored as identifier in repository)
   */
  @Override
  public @NotNull String getId() {
    return ID;
  }

  @Override
  public @Nullable String getPrimaryLinkRefProperty() {
    return null;
  }

  @Override
  public boolean accepts(@NotNull String linkRef) {
    return false;
  }

  @Override
  public @NotNull Link resolveLink(@NotNull Link link) {
    return link;
  }

}
