package ai.ripple.UserService.auth.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ai.ripple.UserService.auth.Entity.Account;
import ai.ripple.UserService.auth.FileStorage.FileStorageService;
import ai.ripple.UserService.auth.Repository.AccountRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth/user/profile")
public class UserProfileController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    // ---------------------------------------------
    // Get user profile
    // ---------------------------------------------
    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("X-User-Email") String email) {
        System.out.println("Received X-User-Email: " + email);
        Account user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    // ---------------------------------------------
    // Update profile details
    // ---------------------------------------------
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestHeader("X-User-Email") String email,
                                           @RequestBody Account updatedData) {
        Account user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update allowed fields
        if (updatedData.getName() != null) user.setName(updatedData.getName());
        if (updatedData.getPhone() != null) user.setPhone(updatedData.getPhone());
        if (updatedData.getAddress() != null) user.setAddress(updatedData.getAddress());

        accountRepository.save(user);
        return ResponseEntity.ok("Profile updated successfully");
    }

    // ---------------------------------------------
    // Update profile photo
    // ---------------------------------------------
    @PostMapping("/photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestHeader("X-User-Email") String email,
                                                @RequestParam("file") MultipartFile file) {
        Account user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileStorageService.uploadFile(file, "profile_photos");
            user.setProfilePhotoUrl(fileUrl);
            accountRepository.save(user);
            return ResponseEntity.ok("Profile photo updated successfully");
        } else {
            return ResponseEntity.badRequest().body("No file provided");
        }
    }

    // ---------------------------------------------
    // Update password
    // ---------------------------------------------
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestHeader("X-User-Email") String email,
                                            @RequestParam("oldPassword") String oldPassword,
                                            @RequestParam("newPassword") String newPassword) {
        Account user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }

    // ---------------------------------------------
    // Delete account
    // ---------------------------------------------
    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@RequestHeader("X-User-Email") String email) {
        Account user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        accountRepository.delete(user);
        return ResponseEntity.ok("User account deleted successfully");
    }
}
