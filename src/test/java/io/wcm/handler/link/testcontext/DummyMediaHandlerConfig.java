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
package io.wcm.handler.link.testcontext;

import java.util.List;

import io.wcm.handler.media.spi.MediaHandlerConfig;
import io.wcm.handler.media.spi.MediaSource;

/**
 * Dummy media configuration
 */
@SuppressWarnings("null")
public class DummyMediaHandlerConfig extends MediaHandlerConfig {

  private static final List<Class<? extends MediaSource>> MEDIA_SOURCES = List.of(
      DummyMediaSource.class);

  @Override
  public List<Class<? extends MediaSource>> getSources() {
    return MEDIA_SOURCES;
  }

}
