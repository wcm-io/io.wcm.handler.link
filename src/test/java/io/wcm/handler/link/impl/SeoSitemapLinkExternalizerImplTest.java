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

import static io.wcm.handler.link.testcontext.AppAemContext.ROOTPATH_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Constants;

import com.adobe.aem.wcm.seo.sitemap.externalizer.SitemapLinkExternalizer;
import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.testcontext.AppAemContext;
import io.wcm.handler.url.SiteConfig;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
class SeoSitemapLinkExternalizerImplTest {

  final AemContext context = AppAemContext.newAemContext();

  @Mock
  private SitemapLinkExternalizer aemSitemapLinkExternalizer;

  private SitemapLinkExternalizer underTest;

  private Page page1;

  @BeforeEach
  void setUp() {

    // simulate AEM built-in SitemapLinkExternalizer with lower service ranking
    context.registerService(SitemapLinkExternalizer.class, aemSitemapLinkExternalizer,
        Constants.SERVICE_RANKING, 100);

    underTest = context.registerInjectActivateService(SeoSitemapLinkExternalizerImpl.class);

    page1 = context.create().page(ROOTPATH_CONTENT + "/page1");
  }

  @Test
  void testExternalizeSlingHttpServletRequestString() {
    when(aemSitemapLinkExternalizer.externalize(context.request(), page1.getPath())).thenReturn("defaultResult");
    String result = underTest.externalize(context.request(), page1.getPath());
    assertEquals("defaultResult", result);
    verifyNoMoreInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResource_Page_WithSiteConfig() {
    MockContextAwareConfig.writeConfiguration(context, ROOTPATH_CONTENT, SiteConfig.class,
        "siteUrl", "https://myhost");

    String result = underTest.externalize(page1.adaptTo(Resource.class));
    assertEquals("https://myhost" + page1.getPath(), result); // .html is removed because it's added by AEM

    verifyNoInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResource_PageContent_WithSiteConfig() {
    MockContextAwareConfig.writeConfiguration(context, ROOTPATH_CONTENT, SiteConfig.class,
        "siteUrl", "https://myhost");

    String result = underTest.externalize(page1.getContentResource());
    assertEquals("https://myhost" + page1.getPath(), result); // .html is removed because it's added by AEM

    verifyNoInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResource_Page_NoSiteConfig() {
    MockContextAwareConfig.writeConfiguration(context, ROOTPATH_CONTENT, SiteConfig.class,
        "siteUrl", "");

    Resource resource = page1.adaptTo(Resource.class);

    when(aemSitemapLinkExternalizer.externalize(resource)).thenReturn("defaultResult");

    String result = underTest.externalize(resource);
    assertEquals("defaultResult", result);

    verifyNoMoreInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResource_Page_NoContentResource() {
    MockContextAwareConfig.writeConfiguration(context, ROOTPATH_CONTENT, SiteConfig.class,
        "siteUrl", "https://myhost");

    // When a page is published implicitly i.e. when it's child is published but the page itself not,
    // AEM creates a page node without jcr:content
    Resource resource = context.create().resource(ROOTPATH_CONTENT + "/page2", "jcr:primaryType", "cq:Page");

    when(aemSitemapLinkExternalizer.externalize(resource)).thenReturn("defaultResult");

    String result = underTest.externalize(resource);
    assertEquals("defaultResult", result);

    verifyNoMoreInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResource_NotPage() {
    Resource resource = context.create().resource("/content/resource1");

    when(aemSitemapLinkExternalizer.externalize(resource)).thenReturn("defaultResult");

    String result = underTest.externalize(resource);
    assertEquals("defaultResult", result);

    verifyNoMoreInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResourceResolverString_Page_WithSiteConfig() {
    MockContextAwareConfig.writeConfiguration(context, ROOTPATH_CONTENT, SiteConfig.class,
        "siteUrl", "https://myhost");

    String path = page1.getPath() + ".html"; // .html added by AEM
    String result = underTest.externalize(context.resourceResolver(), path);
    assertEquals("https://myhost" + page1.getPath() + ".html", result);

    verifyNoInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResourceResolverString_Page_NoSiteConfig() {
    MockContextAwareConfig.writeConfiguration(context, ROOTPATH_CONTENT, SiteConfig.class,
        "siteUrl", "");

    String path = page1.getPath() + ".html"; // .html added by AEM
    when(aemSitemapLinkExternalizer.externalize(context.resourceResolver(), path)).thenReturn("defaultResult");

    String result = underTest.externalize(context.resourceResolver(), path);
    assertEquals("defaultResult", result);

    verifyNoMoreInteractions(aemSitemapLinkExternalizer);
  }

  @Test
  void testExternalizeResourceResolverString_NotPage() {
    Resource resource = context.create().resource("/content/resource1");

    String path = resource.getPath() + ".html"; // .html added by AEM
    when(aemSitemapLinkExternalizer.externalize(context.resourceResolver(), path)).thenReturn("defaultResult");

    String result = underTest.externalize(context.resourceResolver(), path);
    assertEquals("defaultResult", result);

    verifyNoMoreInteractions(aemSitemapLinkExternalizer);
  }

}
