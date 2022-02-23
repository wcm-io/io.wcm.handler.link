/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2022 wcm.io
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
package io.wcm.handler.link.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.wcm.seo.sitemap.externalizer.SitemapLinkExternalizer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;

import io.wcm.handler.link.LinkHandler;
import io.wcm.handler.url.UrlModes;
import io.wcm.sling.commons.adapter.AdaptTo;

/**
 * Implementation of {@link SitemapLinkExternalizer} that uses the link handler for externalizing links.
 * This is used to externalize links in sitemaps, and links used for SEO tags e.g. canoncial URLs.
 */
@Component(
    service = {
        SitemapLinkExternalizer.class,
        org.apache.sling.sitemap.spi.common.SitemapLinkExternalizer.class
    },
    property = {
        Constants.SERVICE_RANKING + ":Integer=500" // higher precedence than default AEM implementation (100)
    })
public class SeoSitemapLinkExternalizerImpl implements SitemapLinkExternalizer {

  private static final String HTML_EXTENSION = ".html";

  private final Logger log = LoggerFactory.getLogger(SeoSitemapLinkExternalizerImpl.class);

  @Reference
  private PageManagerFactory pageManagerFactory;

  @Reference(target = "(!(" + Constants.OBJECTCLASS + "=io.wcm.handler.link.impl.SeoSitemapLinkExternalizerImpl))")
  private SitemapLinkExternalizer aemSitemapLinkExternalizer;

  @Override
  public @NotNull String externalize(SlingHttpServletRequest request, String path) {
    // not used by AEM, use default implementation
    log.debug("Use AEM SitemapLinkExternalizer.externalize(SlingHttpServletRequest,String) for path {}", path);
    return aemSitemapLinkExternalizer.externalize(request, path);
  }

  @Override
  public @NotNull String externalize(Resource resource) {
    Page page = getPageForResource(resource);
    String externalizedUrl = externalizePageLink(page);
    if (externalizedUrl != null) {
      log.debug("Externalize {} to {}", resource, externalizedUrl);
      // remove ".html" extension, it's added automatically by AEM
      return StringUtils.removeEnd(externalizedUrl, HTML_EXTENSION);
    }

    // fallback to AEM implementation
    log.debug("Fallback to AEM SitemapLinkExternalizer.externalize(Resource) for {}", resource);
    return aemSitemapLinkExternalizer.externalize(resource);
  }

  @Override
  public @NotNull String externalize(ResourceResolver resourceResolver, String path) {
    // html extension is added implicitly by AEM, remove it to get the targeted page instance
    String pagePath = StringUtils.removeEnd(path, HTML_EXTENSION);
    Page page = getPageForPath(resourceResolver, pagePath);
    String externalizedUrl = externalizePageLink(page);
    if (externalizedUrl != null) {
      log.debug("Externalize {} to {}", path, externalizedUrl);
      return externalizedUrl;
    }

    // fallback to AEM implementation
    log.debug("Fallback to AEM SitemapLinkExternalizer.externalize(ResourceResolver,String) for {}", path);
    return aemSitemapLinkExternalizer.externalize(resourceResolver, path);
  }

  private @Nullable Page getPageForPath(ResourceResolver resourceResolver, String path) {
    PageManager pageManager = pageManagerFactory.getPageManager(resourceResolver);
    return pageManager.getPage(path);
  }

  private @Nullable Page getPageForResource(Resource resource) {
    Page page = resource.adaptTo(Page.class);
    if (page == null) {
      PageManager pageManager = pageManagerFactory.getPageManager(resource.getResourceResolver());
      page = pageManager.getContainingPage(resource);
    }
    return page;
  }

  private @Nullable String externalizePageLink(@Nullable Page page) {
    if (page == null) {
      return null;
    }
    LinkHandler linkHandler = AdaptTo.notNull(page.getContentResource(), LinkHandler.class);
    return linkHandler.get(page).urlMode(UrlModes.FULL_URL).buildUrl();
  }

}
