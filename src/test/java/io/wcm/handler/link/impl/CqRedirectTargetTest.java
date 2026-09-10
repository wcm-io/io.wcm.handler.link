/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2026 wcm.io
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Constants;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkHandler;
import io.wcm.handler.link.spi.LinkHandlerConfig;
import io.wcm.handler.link.spi.LinkType;
import io.wcm.handler.link.testcontext.AppAemContext;
import io.wcm.handler.link.testcontext.DummyAppTemplate;
import io.wcm.handler.link.testcontext.DummyLinkHandlerConfig;
import io.wcm.handler.link.type.ExternalLinkType;
import io.wcm.handler.link.type.InternalCrossContextLinkType;
import io.wcm.handler.link.type.InternalLinkType;
import io.wcm.handler.link.type.MediaLinkType;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test link handler and link type implementations in context of cq:redirectTarget property handling.
 */
@ExtendWith(AemContextExtension.class)
class CqRedirectTargetTest {

  final AemContext context = AppAemContext.newAemContext();

  private Page targetPage;
  private Page targetPageOtherContext;

  @BeforeEach
  void setUp() {

    // create current page in site context
    context.currentPage(context.create().page(AppAemContext.ROOTPATH_CONTENT + "/section/page",
        DummyAppTemplate.CONTENT.getTemplatePath()));

    // create internal pages for unit tests
    targetPage = context.create().page(AppAemContext.ROOTPATH_CONTENT + "/section/content",
        DummyAppTemplate.CONTENT.getTemplatePath());
    targetPageOtherContext = context.create().page(AppAemContext.ROOTPATH_CONTENT_OTHER_SITE + "/section/content",
        DummyAppTemplate.CONTENT.getTemplatePath());

  }

  @Test
  void internalLinkSameContext() {
    LinkHandler linkHandler = AdaptTo.notNull(context.request(), LinkHandler.class);

    // redirect page using cq:redirectTarget property
    Page redirectInternalPage = context.create().page("/content/unittest/de_test/brand/de/section/redirectInternal", null,
        NameConstants.PN_REDIRECT_TARGET, targetPage.getPath());

    Link link = linkHandler.get(redirectInternalPage).build();

    assertTrue(link.isValid());
    assertEquals(InternalLinkType.ID, link.getLinkType().getId());
    assertEquals("http://www.dummysite.org/content/unittest/de_test/brand/de/section/content.html", link.getUrl());
  }

  @Test
  void internalLinkOtherContext_Rewritten() {
    LinkHandler linkHandler = AdaptTo.notNull(context.request(), LinkHandler.class);

    // redirect page using cq:redirectTarget property
    Page redirectInternalPage = context.create().page("/content/unittest/de_test/brand/de/section/redirectInternal", null,
        NameConstants.PN_REDIRECT_TARGET, targetPageOtherContext.getPath());

    Link link = linkHandler.get(redirectInternalPage).build();

    assertTrue(link.isValid());
    assertEquals(InternalLinkType.ID, link.getLinkType().getId());
    assertEquals("http://www.dummysite.org/content/unittest/de_test/brand/de/section/content.html", link.getUrl());
  }

  @Test
  void internalLinkOtherContext_InternalCrossContextLinkTypeHigherPrecedence() {
    context.registerService(LinkHandlerConfig.class, new DummyLinkHandlerConfig() {
      // switch order of link types to make InternalCrossContextLinkType higher precedence than InternalLinkType
      // this will cause internal link based on cq:redirectTarget to be resolved as an internal cross context link
      @Override
      public List<Class<? extends LinkType>> getLinkTypes() {
        return List.of(
            InternalCrossContextLinkType.class,
            InternalLinkType.class,
            ExternalLinkType.class,
            MediaLinkType.class);
      }
    }, Constants.SERVICE_RANKING, 1000);

    LinkHandler linkHandler = AdaptTo.notNull(context.request(), LinkHandler.class);

    // redirect page using cq:redirectTarget property
    Page redirectInternalPage = context.create().page("/content/unittest/de_test/brand/de/section/redirectInternal", null,
        NameConstants.PN_REDIRECT_TARGET, targetPageOtherContext.getPath());

    Link link = linkHandler.get(redirectInternalPage).build();

    assertTrue(link.isValid());
    assertEquals(InternalCrossContextLinkType.ID, link.getLinkType().getId());
    assertEquals("http://en.dummysite.org/content/unittest/en_test/brand/en/section/content.html", link.getUrl());
  }

  @Test
  void externalLink() {
    LinkHandler linkHandler = AdaptTo.notNull(context.request(), LinkHandler.class);

    // redirect page using cq:redirectTarget property
    Page redirectExternal = context.create().page("/content/unittest/de_test/brand/de/section/redirectExternal", null,
        NameConstants.PN_REDIRECT_TARGET, "https://external-site.com");

    Link link = linkHandler.get(redirectExternal).build();

    assertTrue(link.isValid());
    assertEquals(ExternalLinkType.ID, link.getLinkType().getId());
    assertEquals("https://external-site.com", link.getUrl());
  }

}
