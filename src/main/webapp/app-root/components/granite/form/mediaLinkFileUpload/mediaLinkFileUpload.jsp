<%--
  #%L
  wcm.io
  %%
  Copyright (C) 2019 wcm.io
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  --%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.TreeSet"%>
<%@page import="com.adobe.granite.ui.components.Config"%>
<%@page import="org.apache.sling.api.resource.Resource"%>
<%@page import="org.apache.sling.api.resource.ValueMap"%>
<%@page import="org.apache.sling.api.request.RequestDispatcherOptions"%>
<%@page import="org.apache.sling.api.wrappers.ValueMapDecorator"%>
<%@page import="io.wcm.handler.media.format.MediaFormat"%>
<%@page import="io.wcm.handler.media.format.MediaFormatHandler"%>
<%@page import="io.wcm.handler.link.LinkNameConstants"%>
<%@page import="io.wcm.handler.link.spi.LinkHandlerConfig"%>
<%@page import="io.wcm.handler.link.type.MediaLinkType"%>
<%@page import="io.wcm.wcm.ui.granite.resource.GraniteUiSyntheticResource"%>
<%@page import="io.wcm.wcm.ui.granite.util.GraniteUi"%>
<%@include file="../../global/global.jsp" %>
<%@include file="../../global/linkRootPathDetection.jsp" %><%--###

wcm.io Media Handler FileUpload for selecting a download link reference
======================================================================

A field component for uploading or selecting files from an authoring dialog context.

It extends `/apps/wcm-io/handler/media/components/granite/form/fileupload` component.

It supports the same properties as it's super component. The following properties
are overwritten or added.

.. gnd:gnd::

  /**
   * Default property name as defined for this link type.
   */
  - name (String) = {default property name for link type}

  /**
   * The path of the root of the pathfield. If not set, it's value is set automatically
   * to the root path for media link type as returned by the current context's Link Handler configuration.
   */
  - rootPath (StringEL) = {link type-dependent root path}

  /**
   * The root path that is used as fallback when no root path could be detected dynamically.
   */
  - fallbackRootPath (StringEL) = "/content/dam"

  /**
   * Appendix path added to the (usually auto-detected) root path.
   */
  - appendPath (StringEL) = {path appendix}

  /**
   * List of media formats for assets that can be linked upon.
   * If not set the property value is set to the list of all media formats with "download" flag.
   */
  - mediaFormats (String[]) = {download media format names}

###--%><%

Config cfg = cmp.getConfig();

Map<String,Object> overwriteProperties = new HashMap<>();
overwriteProperties.put("name", cfg.get("name", "./" + LinkNameConstants.PN_LINK_MEDIA_REF));

// detect root path
overwriteProperties.putAll(getRootPathProperties(cmp, slingRequest,
    MediaLinkType.ID, LinkHandlerConfig.DEFAULT_ROOT_PATH_MEDIA));

// get all media formats with "download" flag
Set<String> mediaFormatNames = new TreeSet<String>();
Resource contentResource = GraniteUi.getContentResourceOrParent(request);
if (contentResource != null) {
  MediaFormatHandler mediaFormatHandler = contentResource.adaptTo(MediaFormatHandler.class);
  for (MediaFormat mediaFormat : mediaFormatHandler.getMediaFormats()) {
    if (mediaFormat.isDownload()) {
      mediaFormatNames.add(mediaFormat.getName());
    }
  }
}
String[] mediaFormats = cfg.get("mediaFormats", mediaFormatNames.toArray(new String[mediaFormatNames.size()]));

overwriteProperties.put("mediaFormats", mediaFormats);
overwriteProperties.put("mediaFormatsMandatory", new String[0]);
overwriteProperties.put("mediaCropAuto", false);

// simulate resource for dialog field def with new rootPath instead of configured one
Resource resourceWrapper = GraniteUiSyntheticResource.wrapMerge(resource, new ValueMapDecorator(overwriteProperties));

RequestDispatcherOptions options = new RequestDispatcherOptions();
options.setForceResourceType("wcm-io/handler/media/components/granite/form/fileupload");
RequestDispatcher dispatcher = slingRequest.getRequestDispatcher(resourceWrapper, options);
dispatcher.include(slingRequest, slingResponse);

%>
