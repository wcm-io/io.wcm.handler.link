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
package io.wcm.handler.link.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ConsumerType;

import io.wcm.handler.commons.dom.Anchor;
import io.wcm.handler.link.Link;

/**
 * Builds XHTML markup for links. The markup builder should build only an empty anchor tag without content.
 *
 * <p>
 * This interface has to be implemented by a Sling Model class. The adaptables
 * should be {@link org.apache.sling.api.SlingHttpServletRequest} and {@link org.apache.sling.api.resource.Resource}.
 * </p>
 */
@ConsumerType
public interface LinkMarkupBuilder {

  /**
   * Checks whether this builder can generate markup for the given link.
   * @param link Link metadata
   * @return true if this markup builder can handle the given link
   */
  boolean accepts(@NotNull Link link);

  /**
   * Build link anchor markup
   * @param link Link metadata with resolved link information
   * @return Anchor or null if link is invalid
   */
  @Nullable
  Anchor build(@NotNull Link link);

}
