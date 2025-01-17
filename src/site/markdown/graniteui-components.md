## Link Handler Granite UI components


### Link Reference Container

![Link Reference Container](images/linkRefContainer-component.png)

A component that defines the full set of fields for defining a link with it's properties to be included in a dialog tab. The link types are displayed dynamically as configured in the link handler configuration. It is possible to add your own fields.

```json-jcr
{
  "jcr:primaryType": "nt:unstructured",
  "sling:resourceType": "granite/ui/components/coral/foundation/fixedcolumns",
  "jcr:title": "Link",
  "margin": true,
  "cq:showOnCreate": false,
  "items": {
    "column": {
      "sling:resourceType": "granite/ui/components/coral/foundation/container",
      "items": {
        "linkRef": {
          "sling:resourceType": "wcm-io/handler/link/components/granite/form/linkRefContainer",
          "showLinkTitle": true
        }
      }
    }
  }
}
```

The link source input will be dynamically shown/hidden according to the link type selected. This functionality is included in a clientlib. It may be required to clear the clientlibs cache to see the changes immediately.

`http://localhost:4502/libs/granite/ui/content/dumplibs.rebuild.html`

Properties:
- `showLinkTitle` (boolean), default: `false`\
  Show text field with link title.
- `linkTypes` (String[])\
  Filter link types allowed in the dialog.
  Only link types allowed in the link handler configuration are possible.
  Set this property when not all configured link types should be displayed, but only those also
  contained in this list.
- `namePrefix` (String) default: "./"\
  Prefix for all property names in the link reference dialog.
  Use this to store the link properties in another child resource e.g. to "./mySubNode/".
- `internalLinkFields` (Resources)\
  Additional Granite UI components to be displayed for "internal" link type.
- `internalCrossContextLinkFields` (Resources)\
  Additional Granite UI components to be displayed for "internalCrossContext" link type.
- `externalLinkFields` (Resources)\
  Additional Granite UI components to be displayed for "external" link type.
- `mediaLinkFields` (Resources)\
  Additional Granite UI components to be displayed for "media" link type.
- `allLinkTypeFields` (Resources)\
  Additional Granite UI components to be displayed for all link types.
- `required` (Boolean), default: `false`\
  It is required to select any link target and a link title (if a link title field is shown).
- `requiredLink` (Boolean), default: `false`\
  It is required to select any link target.
- `requiredTitle` (Boolean), default: `false`\
  If showLinkTitle is set to true, it is set to required.

### Internal Link Type Path Field

This is a customized Path Field that always sets the root path to the link root path as defined by the Link Handler configuration for internal links. By default, this is the site root path as defined by the URL Handler configuration.

```json-jcr
"field": {
  "sling:resourceType": "wcm-io/handler/link/components/granite/form/internalLinkPathField",
  "fieldLabel": "Internal page (same site)"
}
```

This component extends the [wcm.io Granite UI components Path Field][wcmio-wcm-ui-granite-pathfield]. Enhancements over this version:

* Dynamically sets `rootPath` to the link root path as returned by the Link Handler configuration
* Dynamically sets `name` to the default property name for internal links

Properties:
- `name` (String), default: "./linkContentRef"\
  Property name for internal link reference.
- `rootPath` (String)\
  The root path for the pathfield. If not set, it's value is set automatically
  to the root path for internal link type as returned by the current context's Link Handler configuration.
- `fallbackRootPath` (String), default: "/content"\
  The root path that is used as fallback when no root path could be detected dynamically,
  e.g. because outside any site or within experience fragments.
- `appendPath` (String)\
  Appendix path added to the (usually auto-detected) root path.

### Internal Link Cross Context Type Path Field

This is a customized Path Field that always sets the root path to the link root path as defined by the Link Handler configuration for internal cross-context links. By default, this is `/content`.

```json-jcr
"field": {
  "sling:resourceType": "wcm-io/handler/link/components/granite/form/internalCrossContextLinkPathField",
  "fieldLabel": "Internal Page (other site)"
}
```

This component extends the [wcm.io Granite UI components Path Field][wcmio-wcm-ui-granite-pathfield]. Enhancements over this version:

* Dynamically sets `rootPath` to the link root path as returned by the Link Handler configuration
* Dynamically sets `name` to the default property name for internal cross-context links

Properties:
- `name` (String), default: "./linkCrossContextContentRef"\
  Property name for internal cross-context link reference.
- `rootPath` (String)\
  The root path for the pathfield. If not set, it's value is set automatically
  to the root path for internal cross-context link type as returned by the current context's Link Handler configuration.
- `fallbackRootPath` (String), default: "/content"\
  The root path that is used as fallback when no root path could be detected dynamically,
  e.g. because outside any site or within experience fragments.
- `appendPath` (String)\
  Appendix path added to the (usually auto-detected) root path.

### Media Link Type Path Field

This is a customized Path Field that always sets the root path to the link root path as defined by the Link Handler configuration for media links. By default, this is `/content/dam`.

```json-jcr
"field": {
  "sling:resourceType": "wcm-io/handler/link/components/granite/form/mediaLinkPathField",
  "fieldLabel": "Asset reference"
}
```

This component extends the [Media Handler-aware Path Field][wcmio-handler-media-pathfield]. Enhancements over this version:

* Dynamically sets `rootPath` to the link root path as returned by the Link Handler configuration
* Dynamically sets `name` to the default property name for media links
* Dynamically sets `mediaFormats` to a list of all media formats with "download" flag

Properties:
- `name` (String), default: "./linkMediaRef"\
  Property name for media link reference.
- `rootPath` (String)\
  The root path for the pathfield. If not set, it's value is set automatically
  to the root path for media link type as returned by the current context's Link Handler configuration.
- `fallbackRootPath` (String), default: "/content/dam"
  The root path that is used as fallback when no root path could be detected dynamically,
  e.g. because outside any site or within experience fragments.
- `appendPath` (String)\
  Appendix path added to the (usually auto-detected) root path.
- `mediaFormats` (String[])\
  List of media formats for assets that can be linked upon.
  If not set the property value is set to the list of all media formats with "download" flag.

[wcmio-handler-media-pathfield]: https://wcm.io/handler/media/graniteui-components.html#Media_Handler-aware_Path_Field
[wcmio-wcm-ui-granite-pathfield]: https://wcm.io/wcm/ui/granite/components.html#Path_Field
