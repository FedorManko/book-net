package com.manko.book.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

  String saveFile(MultipartFile sourceFile, Long userId);

}
