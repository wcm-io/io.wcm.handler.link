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
package io.wcm.handler.link.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.handler.commons.dom.Anchor;
import io.wcm.handler.link.Link;
import io.wcm.handler.link.spi.LinkProcessor;
import io.wcm.handler.link.testcontext.AppAemContext;
import io.wcm.handler.link.type.ExternalLinkType;
import io.wcm.handler.link.type.InternalCrossContextLinkType;
import io.wcm.handler.link.type.InternalLinkType;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Test {@link AbstractInternalLinkInheritUrlParamLinkPostProcessor}.
 */
@ExtendWith(AemContextExtension.class)
@SuppressWarnings("null")
class InternalLinkInheritUrlParamLinkPostProcessorTest {

  final AemContext context = AppAemContext.newAemContext();

  protected Adaptable adaptable() {
    return context.request();
  }

  @Test
  void testInternalLinkWithDefaultParameterList() {
    LinkProcessor postProcessor = AdaptTo.notNull(adaptable(), DefaultInternalLinkInheritUrlParamLinkPostProcessor.class);

    Link link = new Link(new InternalLinkType(), null);
    link.setUrl("/sample.html");
    link.setAnchorBuilder(l -> new Anchor().setHRef(l.getUrl()));

    // test without url parameters
    postProcessor.process(link);
    assertEquals("/sample.html", link.getUrl());

    // test with url parameters
    context.request().setQueryString("debugClientLibs=true&abc=123");

    postProcessor.process(link);
    if (adaptable() instanceof SlingHttpServletRequest) {
      assertEquals("/sample.html?debugClientLibs=true", link.getUrl());
    }
    else {
      assertEquals("/sample.html", link.getUrl());
    }
  }

  @Test
  void testInternalLinkFragment() {
    LinkProcessor postProcessor = AdaptTo.notNull(adaptable(), DefaultInternalLinkInheritUrlParamLinkPostProcessor.class);

    Link link = new Link(new InternalLinkType(), null);
    link.setUrl("/sample.html#fragment1");
    link.setAnchorBuilder(l -> new Anchor().setHRef(l.getUrl()));

    // test without url parameters
    postProcessor.process(link);
    assertEquals("/sample.html#fragment1", link.getUrl());

    // test with url parameters
    context.request().setQueryString("debugClientLibs=true&abc=123");

    postProcessor.process(link);
    if (adaptable() instanceof SlingHttpServletRequest) {
      assertEquals("/sample.html?debugClientLibs=true#fragment1", link.getUrl());
    }
    else {
      assertEquals("/sample.html#fragment1", link.getUrl());
    }
  }

  @Test
  void testInternalLinkFullUrl() {
    LinkProcessor postProcessor = AdaptTo.notNull(adaptable(), DefaultInternalLinkInheritUrlParamLinkPostProcessor.class);

    Link link = new Link(new InternalLinkType(), null);
    link.setUrl("https://host1/sample.html#fragment1");
    link.setAnchorBuilder(l -> new Anchor().setHRef(l.getUrl()));

    // test without url parameters
    postProcessor.process(link);
    assertEquals("https://host1/sample.html#fragment1", link.getUrl());

    // test with url parameters
    context.request().setQueryString("debugClientLibs=true&abc=123");

    postProcessor.process(link);
    if (adaptable() instanceof SlingHttpServletRequest) {
      assertEquals("https://host1/sample.html?debugClientLibs=true#fragment1", link.getUrl());
    }
    else {
      assertEquals("https://host1/sample.html#fragment1", link.getUrl());
    }
  }

  @Test
  void testInternalLinkFullUrlWithPort() {
    LinkProcessor postProcessor = AdaptTo.notNull(adaptable(), DefaultInternalLinkInheritUrlParamLinkPostProcessor.class);

    Link link = new Link(new InternalLinkType(), null);
    link.setUrl("https://host1:8080/sample.html#fragment1");
    link.setAnchorBuilder(l -> new Anchor().setHRef(l.getUrl()));

    // test without url parameters
    postProcessor.process(link);
    assertEquals("https://host1:8080/sample.html#fragment1", link.getUrl());

    // test with url parameters
    context.request().setQueryString("debugClientLibs=true&abc=123");

    postProcessor.process(link);
    if (adaptable() instanceof SlingHttpServletRequest) {
      assertEquals("https://host1:8080/sample.html?debugClientLibs=true#fragment1", link.getUrl());
    }
    else {
      assertEquals("https://host1:8080/sample.html#fragment1", link.getUrl());
    }
  }

  @Test
  void testInternalLinkWithCustomParameterList() {
    LinkProcessor postProcessor = AdaptTo.notNull(adaptable(), AbcInternalLinkInheritUrlParamLinkPostProcessor.class);

    Link link = new Link(new InternalLinkType(), null);
    link.setUrl("/sample.html");
    link.setAnchorBuilder(l -> new Anchor().setHRef(l.getUrl()));

    // test without url parameters
    postProcessor.process(link);
    assertEquals("/sample.html", link.getUrl());

    // test with url parameters
    context.request().setQueryString("debugClientLibs=true&abc=123");

    postProcessor.process(link);
    if (adaptable() instanceof SlingHttpServletRequest) {
      assertEquals("/sample.html?abc=123", link.getUrl());
    }
    else {
      assertEquals("/sample.html", link.getUrl());
    }
  }

  @Test
  void testExternalLinkWithDefaultParameterList() {
    LinkProcessor postProcessor = AdaptTo.notNull(adaptable(), DefaultInternalLinkInheritUrlParamLinkPostProcessor.class);

    Link link = new Link(new ExternalLinkType(), null);
    link.setUrl("/sample.html");
    link.setAnchorBuilder(l -> new Anchor().setHRef(l.getUrl()));

    // test without url parameters
    postProcessor.process(link);
    assertEquals("/sample.html", link.getUrl());

    // test with url parameters
    context.request().setQueryString("debugClientLibs=true&abc=123");

    postProcessor.process(link);
    assertEquals("/sample.html", link.getUrl());
  }

  @Test
  void testInternalCrossContextLinkWithDefaultParameterList() {
    LinkProcessor postProcessor = AdaptTo.notNull(adaptable(), DefaultInternalLinkInheritUrlParamLinkPostProcessor.class);

    Link link = new Link(new InternalCrossContextLinkType(), null);
    link.setUrl("/sample.html");
    link.setAnchorBuilder(l -> new Anchor().setHRef(l.getUrl()));

    // test without url parameters
    postProcessor.process(link);
    assertEquals("/sample.html", link.getUrl());

    // test with url parameters
    context.request().setQueryString("debugClientLibs=true&abc=123");

    postProcessor.process(link);
    if (adaptable() instanceof SlingHttpServletRequest) {
      assertEquals("/sample.html?debugClientLibs=true", link.getUrl());
    }
    else {
      assertEquals("/sample.html", link.getUrl());
    }
  }


  @Model(adaptables = {
      SlingHttpServletRequest.class, Resource.class
  })
  public static class AbcInternalLinkInheritUrlParamLinkPostProcessor extends AbstractInternalLinkInheritUrlParamLinkPostProcessor {
    //CHECKSTYLE:OFF
    public AbcInternalLinkInheritUrlParamLinkPostProcessor() {
      //CHECKSTYLE_ON
      super(Set.of("abc"));
    }
  }

}
