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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jdom2.Attribute;
import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.ImmutableList;

import io.wcm.handler.commons.dom.Anchor;
import io.wcm.handler.link.spi.LinkType;
import io.wcm.handler.media.Asset;
import io.wcm.handler.media.Rendition;
import io.wcm.wcm.commons.util.ToStringStyle;

/**
 * Holds information about a link processed and resolved by {@link LinkHandler}.
 */
@ProviderType
@JsonInclude(Include.NON_NULL)
public final class Link {

  private final @NotNull LinkType linkType;
  private @NotNull LinkRequest linkRequest;
  private boolean linkReferenceInvalid;
  private Anchor anchor;
  private Function<Link, Anchor> anchorBuilder;
  private String url;
  private Page targetPage;
  private Asset targetAsset;
  private Rendition targetRendition;
  private List<Page> redirectPages;

  /**
   * @param linkType Link type
   * @param linkRequest Processed link reference
   */
  public Link(@NotNull LinkType linkType, @NotNull LinkRequest linkRequest) {
    this.linkRequest = linkRequest;
    this.linkType = linkType;
  }

  /**
   * @return Link type
   */
  @JsonUnwrapped
  public @NotNull LinkType getLinkType() {
    return this.linkType;
  }

  /**
   * @return Link request
   */
  @JsonIgnore
  public @NotNull LinkRequest getLinkRequest() {
    return this.linkRequest;
  }

  /**
   * @param linkRequest Link request
   */
  public void setLinkRequest(@NotNull LinkRequest linkRequest) {
    this.linkRequest = linkRequest;
  }

  /**
   * @return true if a link reference was set, but the reference was invalid and could not be resolved
   */
  @JsonIgnore
  public boolean isLinkReferenceInvalid() {
    return this.linkReferenceInvalid;
  }

  /**
   * @param linkReferenceInvalid true if a link reference was set, but the reference was invalid and could not be
   *          resolved
   */
  public void setLinkReferenceInvalid(boolean linkReferenceInvalid) {
    this.linkReferenceInvalid = linkReferenceInvalid;
  }

  /**
   * @return Anchor element
   */
  @JsonIgnore
  public Anchor getAnchor() {
    if (this.anchor == null && this.anchorBuilder != null) {
      this.anchor = this.anchorBuilder.apply(this);
      this.anchorBuilder = null;
    }
    return this.anchor;
  }

  /**
   * @return Map with all attributes of the anchor element. Returns null if anchor element is null.
   */
  @JsonIgnore
  @SuppressWarnings("java:S1168")
  public Map<String, String> getAnchorAttributes() {
    Anchor a = getAnchor();
    if (a == null) {
      return null;
    }
    Map<String, String> attributes = new HashMap<>();
    for (Attribute attribute : a.getAttributes()) {
      attributes.put(attribute.getName(), attribute.getValue());
    }
    return attributes;
  }

  /**
   * @param anchor Anchor element
   */
  public void setAnchor(Anchor anchor) {
    this.anchor = anchor;
  }

  /**
   * @param anchorBuilder Function that builds an anchor representation on demand
   */
  public void setAnchorBuilder(Function<Link, Anchor> anchorBuilder) {
    this.anchorBuilder = anchorBuilder;
  }

  /**
   * @return Link markup (only the opening anchor tag) or null if resolving was not successful.
   */
  @JsonIgnore
  public String getMarkup() {
    Anchor a = getAnchor();
    if (a != null) {
      return StringUtils.removeEnd(a.toString(), "</a>");
    }
    else {
      return null;
    }
  }

  /**
   * @return Link URL
   */
  public String getUrl() {
    return this.url;
  }

  /**
   * @param url Link URL
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return Target page referenced by the link (applies only for internal links)
   */
  @JsonIgnore
  public Page getTargetPage() {
    return this.targetPage;
  }

  /**
   * @param targetPage Target page referenced by the link (applies only for internal links)
   */
  public void setTargetPage(Page targetPage) {
    this.targetPage = targetPage;
  }

  /**
   * @return Target media item (applies only for media links)
   */
  @JsonIgnore
  public Asset getTargetAsset() {
    return this.targetAsset;
  }

  /**
   * @param targetAsset Target media item (applies only for media links)
   */
  public void setTargetAsset(Asset targetAsset) {
    this.targetAsset = targetAsset;
  }

  /**
   * @return Target media rendition (applies only for media links)
   */
  @JsonIgnore
  public Rendition getTargetRendition() {
    return this.targetRendition;
  }

  /**
   * @param targetRendition Target media rendition (applies only for media links)
   */
  public void setTargetRendition(Rendition targetRendition) {
    this.targetRendition = targetRendition;
  }

  /**
   * During link resolution one or multiple redirect pages may get resolved and replaced by the referenced
   * link target. This page list gives access to all redirect pages that where visited and resolved
   * during the link resolution process.
   * @return List of links in the "resolve history".
   */
  @JsonIgnore
  public @NotNull List<Page> getRedirectPages() {
    if (redirectPages == null) {
      return ImmutableList.of();
    }
    else {
      return ImmutableList.copyOf(redirectPages);
    }
  }

  /**
   * Add page to list of redirect pages (at first position of the list).
   * @param redirectPage Redirect page
   */
  public void addRedirectPage(@NotNull Page redirectPage) {
    if (redirectPages == null) {
      redirectPages = new LinkedList<>();
    }
    redirectPages.add(0, redirectPage);
  }

  /**
   * @return true if link is valid and was resolved successfully
   */
  @SuppressWarnings({ "null", "java:S2589" }) // extra null checks for backward compatibility
  public boolean isValid() {
    return getLinkType() != null
        && getUrl() != null
        && !StringUtils.equals(getUrl(), LinkHandler.INVALID_LINK);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_OMIT_NULL_STYLE);
  }

}
