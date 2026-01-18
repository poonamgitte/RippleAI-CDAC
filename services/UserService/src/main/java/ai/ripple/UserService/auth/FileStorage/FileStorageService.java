package ai.ripple.UserService.auth.FileStorage;

import java.nio.file.Files;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    
      private final String uploadDir = "uploads/";

      public String uploadFile(MultipartFile file, String folder) {
        try {
            Path dirPath = Paths.get(uploadDir + folder);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            return "/" + uploadDir + folder + "/" + filename; // return relative path
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

}
