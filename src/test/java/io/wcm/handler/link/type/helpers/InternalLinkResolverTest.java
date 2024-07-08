/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2015 wcm.io
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
package io.wcm.handler.link.type.helpers;

import static io.wcm.handler.link.LinkNameConstants.PN_LINK_CONTENT_REF;
import static io.wcm.handler.link.LinkNameConstants.PN_LINK_TYPE;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkRequest;
import io.wcm.handler.link.SyntheticLinkResource;
import io.wcm.handler.link.testcontext.AppAemContext;
import io.wcm.handler.link.testcontext.DummyAppTemplate;
import io.wcm.handler.link.type.InternalLinkType;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.sling.commons.resource.ImmutableValueMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Most of the logic of {@link InternalLinkResolver} is tested in the InternalLinkTypeTest.
 * This tests only some special feature not used by InternalLinkType.
 */
@ExtendWith(AemContextExtension.class)
class InternalLinkResolverTest {

  private final AemContext context = AppAemContext.newAemContext();

  @BeforeEach
  void setUp() {

    // create current page in site context
    context.currentPage(context.create().page("/content/unittest/de_test/brand/de/section/page",
        DummyAppTemplate.CONTENT.getTemplatePath()));

    // create internal pages for unit tests
    context.create().page("/content/unittest/de_test/brand/de/section/content",
        DummyAppTemplate.CONTENT.getTemplatePath());
    context.create().page("/content/unittest/en_test/brand/en/section/content",
        DummyAppTemplate.CONTENT.getTemplatePath());

  }


  @Test
  void testTargetPage_RewritePathToContext() {
    InternalLinkResolver resolver = AdaptTo.notNull(context.request(), InternalLinkResolver.class);

    SyntheticLinkResource linkResource = new SyntheticLinkResource(context.resourceResolver(),
        "/content/dummy-path",
        ImmutableValueMap.builder()
            .put(PN_LINK_TYPE, InternalLinkType.ID)
            .put(PN_LINK_CONTENT_REF, "/content/unittest/en_test/brand/en/section/content")
            .build());

    LinkRequest linkRequest = new LinkRequest(linkResource, null, null);
    Link link = new Link(new InternalLinkType(), linkRequest);

    link = resolver.resolveLink(link, new InternalLinkResolverOptions()
        .rewritePathToContext(true));

    assertTrue(link.isValid(), "link valid");
    assertFalse(link.isLinkReferenceInvalid(), "link ref invalid");
    assertEquals("http://www.dummysite.org/content/unittest/de_test/brand/de/section/content.html", link.getUrl(), "link url");
  }

  @Test
  void testTargetPageOtherSite_NoRewritePathToContext() {
    InternalLinkResolver resolver = AdaptTo.notNull(context.request(), InternalLinkResolver.class);

    SyntheticLinkResource linkResource = new SyntheticLinkResource(context.resourceResolver(),
        "/content/dummy-path",
        ImmutableValueMap.builder()
            .put(PN_LINK_TYPE, InternalLinkType.ID)
            .put(PN_LINK_CONTENT_REF, "/content/unittest/en_test/brand/en/section/content")
            .build());

    LinkRequest linkRequest = new LinkRequest(linkResource, null, null);
    Link link = new Link(new InternalLinkType(), linkRequest);

    link = resolver.resolveLink(link, new InternalLinkResolverOptions()
        .rewritePathToContext(false));

    assertTrue(link.isValid(), "link valid");
    assertFalse(link.isLinkReferenceInvalid(), "link ref invalid");
    assertEquals("http://en.dummysite.org/content/unittest/en_test/brand/en/section/content.html", link.getUrl(), "link url");
  }

  @Test
  void testTargetPage_RewritePathToContext_ExperienceFragment() {
    Page xfPage = context.create().page("/content/experience-fragments/level1/level2/level3/level4/xf1");
    Resource linkResource = context.create().resource(xfPage, "link",
        PROPERTY_RESOURCE_TYPE, "/dummy/resourcetype",
        PN_LINK_TYPE, InternalLinkType.ID,
        PN_LINK_CONTENT_REF, "/content/unittest/en_test/brand/en/section/content");

    context.currentPage(xfPage);
    context.currentResource(linkResource);

    InternalLinkResolver resolver = AdaptTo.notNull(context.request(), InternalLinkResolver.class);

    LinkRequest linkRequest = new LinkRequest(linkResource, null, null);
    Link link = new Link(new InternalLinkType(), linkRequest);

    link = resolver.resolveLink(link, new InternalLinkResolverOptions()
        .rewritePathToContext(true));

    assertTrue(link.isValid(), "link valid");
    assertFalse(link.isLinkReferenceInvalid(), "link ref invalid");
    assertEquals("http://en.dummysite.org/content/unittest/en_test/brand/en/section/content.html", link.getUrl(), "link url");
  }

}
