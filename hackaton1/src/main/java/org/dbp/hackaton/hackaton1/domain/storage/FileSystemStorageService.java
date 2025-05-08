package org.dbp.hackaton.hackaton1.domain.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path storageLocation;

    public FileSystemStorageService(
            @Value("${storage.location:uploads}") String storageDir
    ) throws IOException {
        this.storageLocation = Paths.get(storageDir).toAbsolutePath().normalize();
        Files.createDirectories(this.storageLocation);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path target = storageLocation.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
