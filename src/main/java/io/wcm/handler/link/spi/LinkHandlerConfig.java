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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ConsumerType;

import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.markup.DummyLinkMarkupBuilder;
import io.wcm.handler.link.markup.SimpleLinkMarkupBuilder;
import io.wcm.handler.link.processor.DefaultInternalLinkInheritUrlParamLinkPostProcessor;
import io.wcm.handler.link.type.ExternalLinkType;
import io.wcm.handler.link.type.InternalCrossContextLinkType;
import io.wcm.handler.link.type.InternalLinkType;
import io.wcm.handler.link.type.MediaLinkType;
import io.wcm.handler.url.ui.SiteRoot;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.sling.commons.caservice.ContextAwareService;
import io.wcm.sling.commons.resource.ResourceType;
import io.wcm.wcm.commons.util.Path;

/**
 * {@link LinkHandlerConfig} OSGi services provide application-specific configuration for link handling.
 * Applications can set service properties or bundle headers as defined in {@link ContextAwareService} to apply this
 * configuration only for resources that match the relevant resource paths.
 */
@ConsumerType
public abstract class LinkHandlerConfig implements ContextAwareService {

  private static final List<Class<? extends LinkType>> DEFAULT_LINK_TYPES = List.of(
      InternalLinkType.class,
      ExternalLinkType.class,
      MediaLinkType.class);

  private static final List<Class<? extends LinkMarkupBuilder>> DEFAULT_LINK_MARKUP_BUILDERS = List.of(
      SimpleLinkMarkupBuilder.class,
      DummyLinkMarkupBuilder.class);

  private static final List<Class<? extends LinkProcessor>> DEFAULT_POST_PROCESSORS = List.of(
      DefaultInternalLinkInheritUrlParamLinkPostProcessor.class);

  private static final String REDIRECT_RESOURCE_TYPE = "wcm-io/handler/link/components/page/redirect";

  /**
   * Default content root path.
   */
  @SuppressWarnings("java:S1075") // no file path
  public static final String DEFAULT_ROOT_PATH_CONTENT = "/content";

  /**
   * Default media/asset root path.
   */
  @SuppressWarnings("java:S1075") // no file path
  public static final String DEFAULT_ROOT_PATH_MEDIA = "/content/dam";

  /**
   * @return Supported link types
   */
  public @NotNull List<Class<? extends LinkType>> getLinkTypes() {
    return DEFAULT_LINK_TYPES;
  }

  /**
   * @return Available link markup builders
   */
  public @NotNull List<Class<? extends LinkMarkupBuilder>> getMarkupBuilders() {
    return DEFAULT_LINK_MARKUP_BUILDERS;
  }

  /**
   * @return List of link metadata pre processors (optional). The processors are applied in list order.
   */
  public @NotNull List<Class<? extends LinkProcessor>> getPreProcessors() {
    // no processors
    return Collections.emptyList();
  }

  /**
   * @return List of link metadata post processors (optional). The processors are applied in list order.
   */
  public @NotNull List<Class<? extends LinkProcessor>> getPostProcessors() {
    return DEFAULT_POST_PROCESSORS;
  }

  /**
   * Detected if page is acceptable as link target.
   * This is used by {@link io.wcm.handler.link.type.InternalLinkType}, other {@link LinkType} implementations
   * may implement other logic.
   * @param page Page
   * @return true if Page is acceptable as link target.
   */
  public boolean isValidLinkTarget(@NotNull Page page) {
    // by default accept all pages
    return true;
  }

  /**
   * Detected if page contains redirect link information
   * @param page Page
   * @return true if Page is a redirect page
   */
  public boolean isRedirect(@NotNull Page page) {
    return ResourceType.is(page.getContentResource(), REDIRECT_RESOURCE_TYPE);
  }

  /**
   * Get root path for picking links using path field widgets.
   * @param page Context page
   * @param linkTypeId Link type ID
   * @return Root path or null
   */
  public @Nullable String getLinkRootPath(@NotNull Page page, @NotNull String linkTypeId) {
    if (StringUtils.equals(linkTypeId, InternalLinkType.ID)) {
      // inside an experience fragment it does not make sense to use a site root path
      if (Path.isExperienceFragmentPath(page.getPath()) || Path.isEditableTemplatePath(page.getPath())) {
        return DEFAULT_ROOT_PATH_CONTENT;
      }
      return AdaptTo.notNull(page.getContentResource(), SiteRoot.class).getRootPath(page);
    }
    else if (StringUtils.equals(linkTypeId, InternalCrossContextLinkType.ID)) {
      return DEFAULT_ROOT_PATH_CONTENT;
    }
    else if (StringUtils.equals(linkTypeId, MediaLinkType.ID)) {
      return DEFAULT_ROOT_PATH_MEDIA;
    }
    return null;
  }

}
