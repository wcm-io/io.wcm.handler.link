/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkBuilder;
import io.wcm.handler.link.LinkHandler;
import io.wcm.handler.link.LinkNameConstants;
import io.wcm.handler.link.SyntheticLinkResource;
import io.wcm.handler.link.testcontext.AppAemContext;
import io.wcm.handler.link.testcontext.DummyAppTemplate;
import io.wcm.handler.url.VanityMode;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.sling.commons.resource.ImmutableValueMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test {@link InternalCrossContextLinkType} methods.
 * Most of the test cases are identical to {@link InternalLinkTypeTest}, so they are not duplicated here.
 */
@ExtendWith(AemContextExtension.class)
class InternalCrossContextLinkTypeTest {

  final AemContext context = AppAemContext.newAemContext();

  private Page targetPage;

  protected Adaptable adaptable() {
    return context.request();
  }

  @BeforeEach
  void setUp() throws Exception {

    // create current page in site context
    context.currentPage(context.create().page("/content/unittest/de_test/brand/de/section/page",
        DummyAppTemplate.CONTENT.getTemplatePath()));

    // create internal pages for unit tests
    targetPage = context.create().page("/content/unittest/de_test/brand/de/section/content",
        DummyAppTemplate.CONTENT.getTemplatePath());
    context.create().page("/content/unittest/en_test/brand/en/section/content",
        DummyAppTemplate.CONTENT.getTemplatePath());

  }

  @Test
  void testTargetPage() {
    LinkHandler linkHandler = AdaptTo.notNull(adaptable(), LinkHandler.class);

    SyntheticLinkResource linkResource = new SyntheticLinkResource(context.resourceResolver(),
        "/content/dummy-path",
        ImmutableValueMap.builder()
            .put(LinkNameConstants.PN_LINK_TYPE, InternalCrossContextLinkType.ID)
            .put(LinkNameConstants.PN_LINK_CROSSCONTEXT_CONTENT_REF, targetPage.getPath())
            .build());

    Link link = linkHandler.get(linkResource).build();

    assertTrue(link.isValid(), "link valid");
    assertFalse(link.isLinkReferenceInvalid(), "link ref invalid");
    assertEquals("http://www.dummysite.org/content/unittest/de_test/brand/de/section/content.html", link.getUrl(), "link url");
    assertNotNull(link.getAnchor(), "anchor");
  }

  @Test
  void testTargetPageOtherSite() {
    LinkHandler linkHandler = AdaptTo.notNull(adaptable(), LinkHandler.class);

    SyntheticLinkResource linkResource = new SyntheticLinkResource(context.resourceResolver(),
        "/content/dummy-path",
        ImmutableValueMap.builder()
            .put(LinkNameConstants.PN_LINK_TYPE, InternalCrossContextLinkType.ID)
            .put(LinkNameConstants.PN_LINK_CROSSCONTEXT_CONTENT_REF, "/content/unittest/en_test/brand/en/section/content")
            .build());

    Link link = linkHandler.get(linkResource).build();

    assertTrue(link.isValid(), "link valid");
    assertEquals("http://en.dummysite.org/content/unittest/en_test/brand/en/section/content.html",
        link.getUrl(), "link url");
    assertNotNull(link.getAnchor(), "anchor");
  }

  @Test
  void testGetSyntheticLinkResource() {
    Resource resource = InternalCrossContextLinkType.getSyntheticLinkResource(context.resourceResolver(),
        "/content/dummy-path",
        "/page/ref");
    ValueMap expected = ImmutableValueMap.of(LinkNameConstants.PN_LINK_TYPE, InternalCrossContextLinkType.ID,
        LinkNameConstants.PN_LINK_CROSSCONTEXT_CONTENT_REF, "/page/ref");
    assertEquals(expected, ImmutableValueMap.copyOf(resource.getValueMap()));
  }

  @Test
  void testVanityMode() {
    String vanityPath = "/content/unittest/de_test/brand/de/vanity-path";
    targetPage = context.create().page("/content/unittest/de_test/brand/de/section/content-vanity",
        DummyAppTemplate.CONTENT.getTemplatePath(),
        "sling:vanityPath", vanityPath);

    SyntheticLinkResource linkResource = new SyntheticLinkResource(context.resourceResolver(),
        "/content/dummy-path",
        ImmutableValueMap.builder()
            .put(LinkNameConstants.PN_LINK_TYPE, InternalCrossContextLinkType.ID)
            .put(LinkNameConstants.PN_LINK_CROSSCONTEXT_CONTENT_REF, targetPage.getPath())
            .build());

    LinkHandler linkHandler = AdaptTo.notNull(adaptable(), LinkHandler.class);
    LinkBuilder linkBuilder = linkHandler.get(linkResource)
        .vanityMode(VanityMode.ALWAYS);
    assertEquals("http://www.dummysite.org/content/unittest/de_test/brand/de/vanity-path.html", linkBuilder.buildUrl());
  }

}
