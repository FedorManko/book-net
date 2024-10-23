package com.manko.book.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@UtilityClass
public class FileUtils {

  public static byte[] readFileFromLocation(String fileUrl) {
    if (StringUtils.isBlank(fileUrl)) {
      return new byte[0];
    }
    try {
      Path filePath = new File(fileUrl).toPath();
      return Files.readAllBytes(filePath);
    } catch (IOException e) {
      log.warn("Nou file found in the path {}", fileUrl);
    }
    return new byte[0];
  }
}
