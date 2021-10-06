package com.tutuka.reconciliation.utils;

import com.tutuka.reconciliation.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileUtils {

    private FileUtils() {
        //DO NOT INITIALIZE
    }

    public static File moveToTempDir(MultipartFile file)  {
        Path tempDirectory;
        File tempFile;
        try {
            tempDirectory = Files.createTempDirectory("");
            tempFile = tempDirectory.resolve(Objects.requireNonNull(file.getOriginalFilename())).toFile();
            file.transferTo(tempFile);
        } catch (Exception e) {
            log.error("", e);
           throw new IllegalArgumentException("Error Moving file to Temp Directory");
        }
        return tempFile;
    }

    public static boolean isFileTypeValid(MultipartFile file) {
        String fileType = file.getContentType();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        return "csv".equalsIgnoreCase(fileType) || !"..".contains(fileName);
    }

    public static boolean isFileHeaderValid(String[] givenHeaderColumns) {
        String[] validHeaderColumns = Transaction.getHeaderColumns();
        return Arrays.equals(validHeaderColumns, givenHeaderColumns);
    }

    public static List<String> validateFiles(MultipartFile... files) {
        List<String> errorMessages = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (!FileUtils.isFileTypeValid(file)) {
                errorMessages.add(String.format("%s has %s ", fileName, "invalid format/name"));
            }
        }

        return errorMessages;
    }


}
