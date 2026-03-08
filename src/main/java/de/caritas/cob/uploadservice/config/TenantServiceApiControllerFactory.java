package de.caritas.cob.uploadservice.config;

import de.caritas.cob.uploadservice.tenantservice.generated.web.TenantControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TenantServiceApiControllerFactory {

  @Value("${tenant.service.api.url}")
  private String tenantServiceApiUrl;

  @Autowired private RestTemplate restTemplate;

  public TenantControllerApi createControllerApi() {
    var apiClient = new TenantServiceApiClient(restTemplate).setBasePath(this.tenantServiceApiUrl);
    TenantControllerApi controllerApi = new TenantControllerApi(apiClient);
    controllerApi.setApiClient(apiClient);
    return controllerApi;
  }
}
