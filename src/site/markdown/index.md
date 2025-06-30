## About Link Handler

Link resolving, processing and markup generation.

[![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.link)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.link/)


### Documentation

* [Usage][usage]
* [General concepts][general-concepts]
* [Granite UI components][graniteui-components]
* [API documentation][apidocs]
* [Changelog][changelog]


### Overview

The Link Handler provides:

* Build URLs for links of different types (based on [URL Handler][url-handler])
* Pluggable link types (with default implementations for link to content pages, link to media assets, external links)
* Pluggable markup builders for links
* Pluggable link pre- and postprocessing to further tailoring the link handling process
* Generic Sling Models for usage in views: [Sling Models][ui-package]
* Generic [Granite UI components][graniteui-components] that can be used in component dialogs

Read the [general concepts][general-concepts] to get an overview of the functionality.


### AEM Version Support Matrix

|Link Handler version |AEM version supported
|---------------------|----------------------
|2.0.x or higher      |AEM 6.5.17+, AEMaaCS
|1.9.4 or higher      |AEM 6.5.7+, AEMaaCS
|1.9.0 - 1.9.2        |AEM 6.5+, AEMaaCS
|1.6.x - 1.8.x        |AEM 6.4.5+, AEMaaCS
|1.5.x                |AEM 6.3.3+, AEM 6.4.5+
|1.1.x - 1.4.x        |AEM 6.2+
|1.0.x                |AEM 6.1+
|0.x                  |AEM 6.0+


### Dependencies

To use this module you have to deploy also:

|---|---|---|
| [wcm.io Sling Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.sling.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) |
| [wcm.io AEM Sling Models Extensions](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.models/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.sling.models)](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.models/) |
| [wcm.io WCM Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.wcm.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.commons/) |
| [wcm.io WCM Granite UI Extensions](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.ui.granite/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.wcm.ui.granite)](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.ui.granite/) |
| [wcm.io Handler Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.commons/) |
| [wcm.io URL Handler](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.url/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.url)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.url/) |
| [wcm.io Media Handler](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.media/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.media)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.media/) |


### GitHub Repository

Sources: https://github.com/wcm-io/io.wcm.handler.link


[usage]: usage.html
[general-concepts]: general-concepts.html
[apidocs]: apidocs/
[graniteui-components]: graniteui-components.html
[changelog]: changes.html
[url-handler]: ../url/
[ui-package]: apidocs/io/wcm/handler/link/ui/package-summary.html
