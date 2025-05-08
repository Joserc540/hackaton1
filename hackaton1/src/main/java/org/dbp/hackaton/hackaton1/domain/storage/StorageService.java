package org.dbp.hackaton.hackaton1.domain.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String save(MultipartFile file) throws IOException;
}
