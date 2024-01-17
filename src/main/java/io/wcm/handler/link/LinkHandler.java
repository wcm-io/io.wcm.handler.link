/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
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
package io.wcm.handler.link;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import com.day.cq.wcm.api.Page;

/**
 * Manages link resolving and markup generation.
 * <p>
 * The interface is implemented by a Sling Model. You can adapt from
 * {@link org.apache.sling.api.SlingHttpServletRequest} or {@link org.apache.sling.api.resource.Resource} to get a
 * context-specific handler instance.
 * </p>
 */
@ProviderType
public interface LinkHandler {

  /**
   * Special link used to notify invalid links.
   */
  @NotNull
  String INVALID_LINK = "/invalid///link";

  /**
   * Build link which is referenced in the resource (containing properties e.g. pointing to internal or external link).
   * @param resource Resource containing properties that define the link target
   * @return Link builder
   */
  @NotNull
  LinkBuilder get(@Nullable Resource resource);

  /**
   * Build internal link referencing the given content page.
   * @param page Target content page
   * @return Link builder
   */
  @NotNull
  LinkBuilder get(@Nullable Page page);

  /**
   * Build link with auto-detecting the type from the given string.
   * @param reference Link reference (internal or external).
   * @return Link builder
   */
  @NotNull
  LinkBuilder get(@Nullable String reference);

  /**
   * Build link for the given request holding all further request properties.
   * @param linkRequest Link handling request
   * @return Link builder
   */
  @NotNull
  LinkBuilder get(@NotNull LinkRequest linkRequest);

  /**
   * Returns an empty link that is marked as invalid.
   * @return Invalid link
   */
  Link invalid();

}
