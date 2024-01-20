/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2024 wcm.io
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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.adapter.Adaptable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkRequest;
import io.wcm.handler.link.spi.LinkType;
import io.wcm.handler.link.testcontext.AppAemContext;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class InvalidLinkTypeTest {

  final AemContext context = AppAemContext.newAemContext();

  protected Adaptable adaptable() {
    return context.request();
  }

  @Test
  void testInvalidLink() {
    LinkType underTest = AdaptTo.notNull(adaptable(), InvalidLinkType.class);

    assertEquals(InvalidLinkType.ID, underTest.getId());
    assertNull(underTest.getPrimaryLinkRefProperty());
    assertFalse(underTest.accepts(new LinkRequest(null, null, null)));

    Link link = underTest.resolveLink(new Link(underTest, new LinkRequest(null, null, null)));
    assertNotNull(link);
    assertFalse(link.isValid());
  }
}
