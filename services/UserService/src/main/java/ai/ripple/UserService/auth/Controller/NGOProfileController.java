package ai.ripple.UserService.auth.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ai.ripple.UserService.auth.Entity.Account;
import ai.ripple.UserService.auth.Entity.Verification;
import ai.ripple.UserService.auth.Entity.VerificationStatus;
import ai.ripple.UserService.auth.FileStorage.FileStorage;
import ai.ripple.UserService.auth.Repository.AccountRepository;
import ai.ripple.UserService.auth.Repository.VerificationRepository;

import java.util.List;

@RestController
@RequestMapping("/auth/ngo/profile")
public class NGOProfileController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorage fileStorageService;

    // ---------------------------------------------
    // Get NGO profile
    // ---------------------------------------------
    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("X-User-Email") String email) {
        Account ngo = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("NGO not found"));
        return ResponseEntity.ok(ngo);
    }

    // ---------------------------------------------
    // Update NGO profile details
    // ---------------------------------------------
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestHeader("X-User-Email") String email,
                                           @RequestBody Account updatedData) {
        Account ngo = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        if (updatedData.getName() != null) ngo.setName(updatedData.getName());
        if (updatedData.getPhone() != null) ngo.setPhone(updatedData.getPhone());
        if (updatedData.getAddress() != null) ngo.setAddress(updatedData.getAddress());
        if (updatedData.getRegistrationNumber() != null) ngo.setRegistrationNumber(updatedData.getRegistrationNumber());

        accountRepository.save(ngo);
        return ResponseEntity.ok("Profile updated successfully");
    }

    // ---------------------------------------------
    // Submit or update verification documents
    // ---------------------------------------------
    @PutMapping("/documents")
    public ResponseEntity<?> updateDocuments(@RequestHeader("X-User-Email") String email,
                                             @RequestBody String submittedDocs) {
        Account ngo = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        // Check for existing pending/verified record
        List<Verification> existing = verificationRepository.findByNgoAndStatusNot(ngo, VerificationStatus.Rejected);

        Verification verification;
        if (existing.isEmpty()) {
            verification = new Verification();
            verification.setNgo(ngo);
        } else {
            verification = existing.get(0);
        }

        verification.setSubmittedDocs(submittedDocs);
        verification.setStatus(VerificationStatus.Pending);

        verificationRepository.save(verification);
        return ResponseEntity.ok("Documents submitted successfully");
    }

    // ---------------------------------------------
    // Get own verification status
    // ---------------------------------------------
    @GetMapping("/verification")
    public ResponseEntity<?> getVerificationStatus(@RequestHeader("X-User-Email") String email) {
        Account ngo = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        List<Verification> records = verificationRepository.findByNgo(ngo);
        if (records.isEmpty()) {
            return ResponseEntity.ok("No verification records found");
        }
        return ResponseEntity.ok(records);
    }

    // ---------------------------------------------
    // Update profile photo
    // ---------------------------------------------
    @PostMapping("/photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestHeader("X-User-Email") String email,
                                                @RequestParam("file") MultipartFile file) {
        Account ngo = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileStorageService.uploadFile(file, "profile_photos");
            ngo.setProfilePhotoUrl(fileUrl);
            accountRepository.save(ngo);
            return ResponseEntity.ok("Profile photo updated successfully");
        } else {
            return ResponseEntity.badRequest().body("No file provided");
        }
    }

    // ---------------------------------------------
    // Change password
    // ---------------------------------------------
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestHeader("X-User-Email") String email,
                                            @RequestBody String newPassword) {
        Account ngo = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        ngo.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(ngo);
        return ResponseEntity.ok("Password updated successfully");
    }

    // ---------------------------------------------
    // Delete NGO account
    // ---------------------------------------------
    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@RequestHeader("X-User-Email") String email) {
        Account ngo = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        accountRepository.delete(ngo);
        return ResponseEntity.ok("NGO account deleted successfully");
    }
}
