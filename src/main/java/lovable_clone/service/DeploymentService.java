package lovable_clone.service;


import lovable_clone.dto.deploy.DeployResponse;



public interface DeploymentService {

    DeployResponse deploy(Long projectId);
}
