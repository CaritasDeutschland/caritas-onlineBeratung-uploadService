package de.caritas.cob.uploadservice.config;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/** Provides the SpringFox (API documentation generation) configuration. */
@Configuration
public class SpringFoxConfig {

  @Value("${springfox.docuTitle}")
  private String docuTitle;

  @Value("${springfox.docuDescription}")
  private String docuDescription;

  @Value("${springfox.docuVersion}")
  private String docuVersion;

  @Value("${springfox.docuTermsUrl}")
  private String docuTermsUrl;

  @Value("${springfox.docuContactName}")
  private String docuContactName;

  @Value("${springfox.docuContactUrl}")
  private String docuContactUrl;

  @Value("${springfox.docuContactEmail}")
  private String docuContactEmail;

  @Value("${springfox.docuLicense}")
  private String docuLicense;

  @Value("${springfox.docuLicenseUrl}")
  private String docuLicenseUrl;

  /**
   * Returns the API protocols (for documentation)
   *
   * @return
   */
  private Set<String> protocols() {
    Set<String> protocols = new HashSet<>();
    protocols.add("http"); // TODO remove for production mode
    protocols.add("https");
    return protocols;
  }

  /** Returns all content types which should be consumed/produced */
  private Set<String> getContentTypes() {
    Set<String> contentTypes = new HashSet<>();
    contentTypes.add("application/json");
    return contentTypes;
  }
}
