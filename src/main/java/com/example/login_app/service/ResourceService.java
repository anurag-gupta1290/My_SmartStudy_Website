package com.example.login_app.service;


import com.example.login_app.entity.Resource;
import com.example.login_app.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public List<Resource> getResourcesByCategory(String category) {
        return resourceRepository.findByCategory(category);
    }

    public List<Resource> searchResources(String query) {
        return resourceRepository.searchResources(query);
    }

    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource downloadResource(Long resourceId) {
        return resourceRepository.findById(resourceId).map(resource -> {
            resource.setDownloadCount(resource.getDownloadCount() + 1);
            return resourceRepository.save(resource);
        }).orElse(null);
    }

    public boolean deleteResource(Long resourceId) {
        if (resourceRepository.existsById(resourceId)) {
            resourceRepository.deleteById(resourceId);
            return true;
        }
        return false;
    }
}