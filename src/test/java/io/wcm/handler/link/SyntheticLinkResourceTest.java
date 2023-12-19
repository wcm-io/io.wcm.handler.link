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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;

import io.wcm.sling.commons.resource.ImmutableValueMap;

@ExtendWith(MockitoExtension.class)
class SyntheticLinkResourceTest {

  @Mock
  private ResourceResolver resourceResolver;

  @Test
  void testSimpleConstructor() {
    Resource underTest = new SyntheticLinkResource(resourceResolver, "/content/dummy-path");
    ValueMap props = underTest.getValueMap();
    assertTrue(props.isEmpty());
  }

  @Test
  void testWithMap() {
    ValueMap givenProps = ImmutableValueMap.of("prop1", "value1");
    Resource underTest = new SyntheticLinkResource(resourceResolver, "/content/dummy-path", givenProps);
    ValueMap props = underTest.getValueMap();
    assertEquals(givenProps, ImmutableValueMap.copyOf(props));
  }

  @Test
  void testAdaptTo() {
    Resource underTest = new SyntheticLinkResource(resourceResolver, "/content/dummy-path");
    Page page = underTest.adaptTo(Page.class);
    assertNull(page);
  }

}
