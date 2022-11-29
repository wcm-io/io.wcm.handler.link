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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;

import io.wcm.handler.commons.dom.Anchor;
import io.wcm.handler.link.spi.LinkType;
import io.wcm.handler.media.Asset;
import io.wcm.handler.media.Rendition;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class LinkTest {

  @Mock
  private LinkType linkType;
  private LinkRequest linkRequest = new LinkRequest(null, null, null);

  private Link underTest;

  @BeforeEach
  void setUp() {
    underTest = new Link(linkType, linkRequest);
  }

  @Test
  void testLinkTypeRequest() {
    assertSame(linkType, underTest.getLinkType());
    assertSame(linkRequest, underTest.getLinkRequest());
  }

  @Test
  void testLinkReferenceInvalid() {
    underTest.setLinkReferenceInvalid(true);
    assertTrue(underTest.isLinkReferenceInvalid());
  }

  @Test
  void testAnchor() {
    assertNull(underTest.getAnchorAttributes());
    assertNull(underTest.getMarkup());

    Anchor anchor = new Anchor("http://dummy");
    underTest.setAnchor(anchor);
    assertSame(anchor, underTest.getAnchor());
    assertEquals("http://dummy", underTest.getAnchorAttributes().get("href"));
    assertEquals("<a href=\"http://dummy\">", underTest.getMarkup());
  }

  @Test
  void testUrlAndValid() {
    assertFalse(underTest.isValid());
    underTest.setUrl("http://dummy");
    assertEquals("http://dummy", underTest.getUrl());
    assertTrue(underTest.isValid());
  }

  @Test
  void testTargetPage() {
    Page page = mock(Page.class);
    underTest.setTargetPage(page);
    assertSame(page, underTest.getTargetPage());
  }

  @Test
  void testTargetAsset() {
    Asset asset = mock(Asset.class);
    underTest.setTargetAsset(asset);
    assertSame(asset, underTest.getTargetAsset());
  }

  @Test
  void testTargetRendition() {
    Rendition rendition = mock(Rendition.class);
    underTest.setTargetRendition(rendition);
    assertSame(rendition, underTest.getTargetRendition());
  }

  @Test
  void testToString() {
    assertEquals("Link[linkType=linkType,linkRequest=LinkRequest[linkArgs=LinkArgs[dummyLink=false,disableSuffixSelector=false]],linkReferenceInvalid=false]",
        underTest.toString());
  }

}
