package com.example.login_app.controller;

import com.example.login_app.entity.User;
import com.example.login_app.repository.UserRepository;
import com.example.login_app.security.GoogleJwtVerifier;
import com.example.login_app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Constructor Injection
    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/auth/login";
    }

    // Login Page
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "success", required = false) String success,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully.");
        }
        if (success != null) {
            model.addAttribute("success", "Registration successful! Please login.");
        }

        return "login";
    }

    // Registration Page
    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    // Registration Processing
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model,
                               HttpServletRequest request) { // Added for email debugging

        // Validate password match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }

        // Check username availability
        Optional<User> existingUsername = userRepository.findByUsername(username);
        if (existingUsername.isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        // Check email availability
        Optional<User> existingEmail = userRepository.findByEmail(email);
        if (existingEmail.isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }

        // Validate password strength
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long!");
            return "register";
        }

        try {
            // Create new user
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("USER");

            User savedUser = userRepository.save(user);
            System.out.println("✅ User registered: " + savedUser.getUsername() + " (" + savedUser.getEmail() + ")");

            // Send welcome email with improved error handling
            try {
                emailService.sendEmail(
                        email,
                        "🎉 Welcome to StudySmart Pro",
                        "Hello " + username + ",\n\n" +
                                "Your registration was successful! 🎉\n\n" +
                                "Account Details:\n" +
                                "• Username: " + username + "\n" +
                                "• Email: " + email + "\n" +
                                "• Registration Date: " + new java.util.Date() + "\n\n" +
                                "Login here: http://localhost:" + request.getServerPort() + "/auth/login\n\n" +
                                "Need help? Contact our support team.\n\n" +
                                "Best regards,\nTeam StudySmart Pro"
                );
                System.out.println("✅ Welcome email sent to: " + email);
            } catch (Exception emailError) {
                // Don't fail registration if email fails
                System.err.println("⚠️ Email sending failed (but registration successful): " + emailError.getMessage());
                // You could log this to a file or database for later review
            }

            // Send test email to admin for debugging
            try {
                emailService.sendEmail(
                        "2023408565.anurag@ug.sharda.ac.in", // Send to yourself too
                        "📧 New User Registration - StudySmart Pro",
                        "New user registered:\n" +
                                "• Username: " + username + "\n" +
                                "• Email: " + email + "\n" +
                                "• Time: " + new java.util.Date()
                );
            } catch (Exception e) {
                // Ignore test email errors
            }

            return "redirect:/auth/login?success";

        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    // Logout handler
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/auth/login?logout";
    }

    // GOOGLE LOGIN CALLBACK - IMPROVED VERSION
    @PostMapping("/google/callback")
    public String googleCallback(@RequestParam("credential") String credential,
                                 HttpServletRequest request) {

        System.out.println("📢 Google callback received at: " + new java.util.Date());

        try {
            // 1. Token verification
            String email = GoogleJwtVerifier.verify(credential);
            if (email == null || email.isEmpty()) {
                System.err.println("❌ Google token verification failed or returned empty email");
                return "redirect:/auth/login?error=Google+verification+failed";
            }

            System.out.println("✅ Google verified email: " + email);

            // 2. Check if user exists or create new
            User user = userRepository.findByEmail(email).orElse(null);
            boolean isNewUser = false;

            if (user == null) {
                System.out.println("🆕 Creating new user for: " + email);
                user = new User();

                // Generate username from email (remove everything after @)
                String username = email.split("@")[0];
                // Make sure username is unique
                String baseUsername = username;
                int counter = 1;
                while (userRepository.findByUsername(username).isPresent()) {
                    username = baseUsername + counter;
                    counter++;
                }

                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("GOOGLE_OAUTH_" + System.currentTimeMillis() + "_" + email));
                user.setRole("USER");
                user.setOauthProvider("GOOGLE");

                user = userRepository.save(user);
                System.out.println("✅ New Google user created: " + user.getUsername());
                isNewUser = true;
            } else {
                System.out.println("✅ Existing user found: " + user.getUsername());
                isNewUser = false;
            }

            // 3. Create authentication token
            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole())
            );

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), // Principal
                    null, // Credentials (null for OAuth)
                    authorities
            );

            // 4. Set authentication details
            auth.setDetails(new WebAuthenticationDetails(request));

            // 5. Save in SecurityContext
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            // 6. Save in HttpSession (CRITICAL for session persistence)
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);

            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            System.out.println("✅ Authentication saved in session");
            System.out.println("✅ Session ID: " + session.getId());
            System.out.println("✅ User authenticated: " + auth.getName());
            System.out.println("✅ Authorities: " + auth.getAuthorities());
            System.out.println("✅ Is new user: " + isNewUser);

            // 7. Send welcome email for new Google users
            if (isNewUser) {
                try {
                    emailService.sendEmail(
                            email,
                            "🎉 Welcome to StudySmart Pro (Google Sign-In)",
                            "Hello " + user.getUsername() + ",\n\n" +
                                    "Welcome to StudySmart Pro! You've successfully signed up using Google.\n\n" +
                                    "You can now access all features with your Google account.\n\n" +
                                    "Best regards,\nTeam StudySmart Pro"
                    );
                } catch (Exception emailError) {
                    System.err.println("⚠️ Welcome email failed for Google user: " + emailError.getMessage());
                }
            }

            // 8. Redirect to dashboard
            return "redirect:/dashboard";

        } catch (Exception e) {
            System.err.println("❌ Google callback error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/auth/login?error=Internal+server+error";
        }
    }

    // Add this method for testing session
    @GetMapping("/test-session")
    @ResponseBody
    public String testSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return "Session exists. ID: " + session.getId() +
                    ", Created: " + new java.util.Date(session.getCreationTime()) +
                    ", Last Accessed: " + new java.util.Date(session.getLastAccessedTime());
        }
        return "No active session";
    }
}