package org.activiti.cloud.app.services;

import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDescriptor;
import org.activiti.cloud.app.model.deployments.ApplicationDeploymentDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeploymentsService {

    @Autowired
    private RestTemplate restTemplate;

    private final String configServerURL = "http://localhost:8761/config";
    private final String appsEntryPoint = "apps";
    private final String activitiCloudAppsNamespace = "/activiti-cloud-apps/dev/master/";

    public ApplicationDeploymentDescriptor getDeploymentDescriptorByAppName(String app) {
        ResponseEntity<ApplicationDeploymentDescriptor> appDeploymentDescr = restTemplate.getForEntity(configServerURL +
                                                                                                               activitiCloudAppsNamespace +
                                                                                                               app +
                                                                                                               ".json",
                                                                                                       ApplicationDeploymentDescriptor.class);
        return appDeploymentDescr.getBody();
    }

    public ApplicationDeploymentDirectory getDirectory() {
        ResponseEntity<ApplicationDeploymentDirectory> appDeploymentDirectory = restTemplate.getForEntity(configServerURL +
                                                                                                                  activitiCloudAppsNamespace +
                                                                                                                  appsEntryPoint +
                                                                                                                  ".json",
                                                                                                          ApplicationDeploymentDirectory.class);
        return appDeploymentDirectory.getBody();
    }
}
