package com.example.login_app.controller;


import com.example.login_app.entity.Resource;
import com.example.login_app.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources() {
        List<Resource> resources = resourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Resource>> getResourcesByCategory(@PathVariable String category) {
        List<Resource> resources = resourceService.getResourcesByCategory(category);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Resource>> searchResources(@RequestParam String query) {
        List<Resource> resources = resourceService.searchResources(query);
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<Resource> createResource(@RequestBody Resource resource) {
        Resource created = resourceService.createResource(resource);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/download/{id}")
    public ResponseEntity<Resource> downloadResource(@PathVariable Long id) {
        Resource resource = resourceService.downloadResource(id);
        return resource != null ? ResponseEntity.ok(resource) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        boolean success = resourceService.deleteResource(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}