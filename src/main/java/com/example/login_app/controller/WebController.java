package com.example.login_app.controller;

import com.example.login_app.entity.User;
import com.example.login_app.service.UserRegistrationService;
import com.example.login_app.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private DashboardService dashboardService;

    // Root URL -> redirect to login
    @GetMapping("/")
    public String root() {
        return "redirect:/auth/login";
    }

    // Registration Page redirect
    @GetMapping("/register")
    public String registerPage() {
        return "redirect:/auth/register";
    }

    // Dashboard - UPDATED with complete data
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> user = userRegistrationService.findByEmail(email);

            if (user.isPresent()) {
                // User basic info
                model.addAttribute("user", user.get());
                model.addAttribute("username", user.get().getUsername());

                // Dashboard data
                Long userId = user.get().getId();
                model.addAttribute("stats", dashboardService.getDashboardStats(userId));
                model.addAttribute("courses", dashboardService.getUserCourses(userId));
                model.addAttribute("assignments", dashboardService.getUserAssignments(userId));
                model.addAttribute("progress", dashboardService.getUserProgress(userId));

                return "dashboard"; // dashboard.html
            }
        }
        return "redirect:/auth/login";
    }

    // Additional mappings for other sections
    @GetMapping("/courses")
    public String courses(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> user = userRegistrationService.findByEmail(email);

            if (user.isPresent()) {
                model.addAttribute("username", user.get().getUsername());
                model.addAttribute("courses", dashboardService.getUserCourses(user.get().getId()));
                return "dashboard"; // Same template, will handle section switching via JavaScript
            }
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/assignments")
    public String assignments(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> user = userRegistrationService.findByEmail(email);

            if (user.isPresent()) {
                model.addAttribute("username", user.get().getUsername());
                model.addAttribute("assignments", dashboardService.getUserAssignments(user.get().getId()));
                return "dashboard"; // Same template, will handle section switching via JavaScript
            }
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/progress")
    public String progress(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> user = userRegistrationService.findByEmail(email);

            if (user.isPresent()) {
                model.addAttribute("username", user.get().getUsername());
                model.addAttribute("stats", dashboardService.getDashboardStats(user.get().getId()));
                model.addAttribute("courses", dashboardService.getUserCourses(user.get().getId()));
                return "dashboard"; // Same template, will handle section switching via JavaScript
            }
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/ai-tutor")
    public String aiTutor(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> user = userRegistrationService.findByEmail(email);

            if (user.isPresent()) {
                model.addAttribute("username", user.get().getUsername());
                return "dashboard"; // Same template, will handle section switching via JavaScript
            }
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/resources")
    public String resources(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> user = userRegistrationService.findByEmail(email);

            if (user.isPresent()) {
                model.addAttribute("username", user.get().getUsername());
                return "dashboard"; // Same template, will handle section switching via JavaScript
            }
        }
        return "redirect:/auth/login";
    }
}