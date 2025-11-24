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

import java.util.HashMap;
import java.util.Map;

import io.wcm.handler.url.VanityMode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.handler.url.UrlHandler;
import io.wcm.handler.url.UrlMode;
import io.wcm.wcm.commons.util.ToStringStyle;

/**
 * Holds parameters to influence the link resolving process.
 */
@ProviderType
public final class LinkArgs implements Cloneable {

  private UrlMode urlMode;
  private VanityMode vanityMode;
  private boolean dummyLink;
  private String dummyLinkUrl;
  private String selectors;
  private String extension;
  private String suffix;
  private String queryString;
  private String fragment;
  private String windowTarget;
  private boolean disableSuffixSelector;
  private ValueMap properties;
  private String[] linkTargetUrlFallbackProperty;
  private String[] linkTargetWindowTargetFallbackProperty;


  /**
   * Get URL mode.
   * @return URL mode for externalizing the URL
   */
  public @Nullable UrlMode getUrlMode() {
    return this.urlMode;
  }

  /**
   * Get vanity mode.
   * @return Vanity mode for building the URL
   */
  public @Nullable VanityMode getVanityMode() {
    return this.vanityMode;
  }

  /**
   * Set URL mode.
   * @param value URL mode for externalizing the URL
   * @return this
   */
  public @NotNull LinkArgs urlMode(@Nullable UrlMode value) {
    this.urlMode = value;
    return this;
  }

  /**
   * Set vanity mode.
   * @param value Vanity mode for building the URL
   * @return this
   */
  public @NotNull LinkArgs vanityMode(@Nullable VanityMode value) {
    this.vanityMode = value;
    return this;
  }

  /**
   * Check if dummy link mode is enabled.
   * @return If set to true, link handler returns a dummy link in edit mode when link is invalid.
   */
  public boolean isDummyLink() {
    return this.dummyLink;
  }

  /**
   * Set dummy link mode.
   * @param value If set to true, link handler returns a dummy link in edit mode when link is invalid.
   * @return this
   */
  public @NotNull LinkArgs dummyLink(boolean value) {
    this.dummyLink = value;
    return this;
  }

  /**
   * Get dummy link URL.
   * @return Custom dummy link url. If null default dummy url is used.
   */
  public @Nullable String getDummyLinkUrl() {
    return this.dummyLinkUrl;
  }

  /**
   * Set dummy link URL.
   * @param value Custom dummy link url. If null default dummy url is used.
   * @return this
   */
  public @NotNull LinkArgs dummyLinkUrl(@Nullable String value) {
    this.dummyLinkUrl = value;
    return this;
  }

  /**
   * Get selectors.
   * @return Selector string
   */
  public @Nullable String getSelectors() {
    return this.selectors;
  }

  /**
   * Set selectors.
   * @param value Selector string
   * @return this
   */
  public @NotNull LinkArgs selectors(@Nullable String value) {
    this.selectors = value;
    return this;
  }

  /**
   * Get extension.
   * @return File extension
   */
  public @Nullable String getExtension() {
    return this.extension;
  }

  /**
   * Set extension.
   * @param value File extension
   * @return this
   */
  public @NotNull LinkArgs extension(@Nullable String value) {
    this.extension = value;
    return this;
  }

  /**
   * Get suffix.
   * @return Suffix string
   */
  public @Nullable String getSuffix() {
    return this.suffix;
  }

  /**
   * Set suffix.
   * @param value Suffix string
   * @return this
   */
  public @NotNull LinkArgs suffix(@Nullable String value) {
    this.suffix = value;
    return this;
  }

  /**
   * Get query string.
   * @return Query parameters string (properly url-encoded)
   */
  public @Nullable String getQueryString() {
    return this.queryString;
  }

  /**
   * Set query string.
   * @param value Query parameters string (properly url-encoded)
   * @return this
   */
  public @NotNull LinkArgs queryString(@Nullable String value) {
    this.queryString = value;
    return this;
  }

  /**
   * Get fragment.
   * @return Fragment identifier
   */
  public @Nullable String getFragment() {
    return this.fragment;
  }

  /**
   * Set fragment.
   * @param value Fragment identifier
   * @return this
   */
  public @NotNull LinkArgs fragment(@Nullable String value) {
    this.fragment = value;
    return this;
  }

  /**
   * Get window target.
   * @return Link window target
   */
  public @Nullable String getWindowTarget() {
    return this.windowTarget;
  }

  /**
   * Set window target.
   * @param value Link window target
   * @return this
   */
  public @NotNull LinkArgs windowTarget(@Nullable String value) {
    this.windowTarget = value;
    return this;
  }

  /**
   * Disable the automatic addition of an additional selector {@link UrlHandler#SELECTOR_SUFFIX}
   * in case a suffix is present for building the URL. Although recommended as best practice, this can
   * be omitted if you are sure your URLs are always either include a suffix or never do, so there is no risk
   * for file name clashes in dispatcher cache.
   * @return If set to true, no additional suffix selector is added
   */
  public boolean isDisableSuffixSelector() {
    return this.disableSuffixSelector;
  }

  /**
   * Disable the automatic addition of an additional selector {@link UrlHandler#SELECTOR_SUFFIX}
   * in case a suffix is present for building the URL. Although recommended as best practice, this can
   * be omitted if you are sure your URLs are always either include a suffix or never do, so there is no risk
   * for file name clashes in dispatcher cache.
   * @param value If set to true, no additional suffix selector is added
   * @return this
   */
  public @NotNull LinkArgs disableSuffixSelector(boolean value) {
    this.disableSuffixSelector = value;
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @param map Property map. Is merged with properties already set.
   * @return this
   */
  @SuppressWarnings({ "null", "unused" })
  public @NotNull LinkArgs properties(@NotNull Map<String, Object> map) {
    if (map == null) {
      throw new IllegalArgumentException("Map argument must not be null.");
    }
    getProperties().putAll(map);
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @param key Property key
   * @param value Property value
   * @return this
   */
  @SuppressWarnings({ "null", "unused" })
  public @NotNull LinkArgs property(@NotNull String key, @Nullable Object value) {
    if (key == null) {
      throw new IllegalArgumentException("Key argument must not be null.");
    }
    getProperties().put(key, value);
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @return Value map
   */
  public @NotNull ValueMap getProperties() {
    if (this.properties == null) {
      this.properties = new ValueMapDecorator(new HashMap<>());
    }
    return this.properties;
  }


  /**
   * Defines a "fallback" property name that is used to load link target information from a single property
   * instead of the link type + link type depending property name. This property is used for migration
   * from components that do not support Link Handler. It is only used for reading, and never written back to.
   * When opened and saved in the link dialog, the property is removed and instead the dedicated properties are used.
   * @param propertyNames Property name(s)
   * @return this
   */
  public @NotNull LinkArgs linkTargetUrlFallbackProperty(@NotNull String @Nullable... propertyNames) {
    this.linkTargetUrlFallbackProperty = propertyNames;
    return this;
  }

  /**
   * Get link target URL fallback property.
   * @return Property name(s)
   */
  public @Nullable String[] getLinkTargetUrlFallbackProperty() {
    return this.linkTargetUrlFallbackProperty;
  }

  /**
   * Defines a "fallback" property name that is used to load the "windows target" information from
   * instead of the the default property. This property is used for migration
   * from components that do not support Link Handler. It is only used for reading, and never written back to.
   * When opened and saved in the link dialog, the property is removed and instead the dedicated properties are used.
   * @param propertyNames Property name(s)
   * @return this
   */
  public @NotNull LinkArgs linkTargetWindowTargetFallbackProperty(@NotNull String @Nullable... propertyNames) {
    this.linkTargetWindowTargetFallbackProperty = propertyNames;
    return this;
  }

  /**
   * Get link target window target fallback property.
   * @return Property name(s)
   */
  public @Nullable String[] getLinkTargetWindowTargetFallbackProperty() {
    return this.linkTargetWindowTargetFallbackProperty;
  }


  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_OMIT_NULL_STYLE);
  }

  /**
   * Custom clone-method for {@link LinkArgs}
   * @return the cloned {@link LinkArgs}
   */
  @Override
  @SuppressWarnings({ "java:S2975", "java:S1182", "checkstyle:SuperCloneCheck" }) // ignore clone warnings
  public LinkArgs clone() { //NOPMD
    LinkArgs clone = new LinkArgs();

    clone.urlMode = this.urlMode;
    clone.dummyLink = this.dummyLink;
    clone.dummyLinkUrl = this.dummyLinkUrl;
    clone.selectors = this.selectors;
    clone.extension = this.extension;
    clone.suffix = this.suffix;
    clone.queryString = this.queryString;
    clone.fragment = this.fragment;
    clone.windowTarget = this.windowTarget;
    clone.disableSuffixSelector = this.disableSuffixSelector;
    clone.linkTargetUrlFallbackProperty = ArrayUtils.clone(this.linkTargetUrlFallbackProperty);
    clone.linkTargetWindowTargetFallbackProperty = ArrayUtils.clone(this.linkTargetWindowTargetFallbackProperty);
    if (this.properties != null) {
      clone.properties = new ValueMapDecorator(new HashMap<>(this.properties));
    }

    return clone;
  }

}
