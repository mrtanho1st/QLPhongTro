package com.minhtan.qlptbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {

    private final Path uploadRoot;

    public FileStorageService(
            @Value("${app.upload-dir}") String uploadDir) {

        this.uploadRoot = Path.of(uploadDir);
    }

    /**
     * Chuyển đường dẫn tuyệt đối thành đường dẫn tương đối.
     *
     * Ví dụ:
     * D:\PhongTro\1\img1.png
     * ->
     * 1/img1.png
     */
    public String toRelativePath(String absolutePath) {

        if (absolutePath == null || absolutePath.isBlank()) {
            return "";
        }

        Path absolute = Path.of(absolutePath);

        return uploadRoot.relativize(absolute)
                .toString()
                .replace("\\", "/");
    }

    /**
     * Chuyển đường dẫn tương đối thành đường dẫn tuyệt đối.
     *
     * Ví dụ:
     * 1/img1.png
     * ->
     * D:\PhongTro\1\img1.png
     */
    public Path toAbsolutePath(String relativePath) {

        return uploadRoot.resolve(relativePath);
    }

    /**
     * Sinh URL để React hiển thị ảnh.
     *
     * Ví dụ:
     * 1/img1.png
     * ->
     * /uploads/1/img1.png
     */
    public String toPublicUrl(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return "";
        }

        return "/uploads/" + relativePath.replace("\\", "/");
    }

    /**
     * Xóa file.
     */
    public void delete(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return;
        }

        try {

            Path file = toAbsolutePath(relativePath);

            Files.deleteIfExists(file);

        }
        // Dữ liệu cũ: file:/D:/PhongTro/...
        catch (java.nio.file.InvalidPathException e) {
            System.out.println("Bỏ qua đường dẫn cũ: " + relativePath);
        }
        // File không tồn tại
        catch (java.nio.file.NoSuchFileException e) {
            System.out.println("Không tìm thấy file: " + relativePath);
        }
        // Các lỗi IO khác
        catch (IOException e) {
            throw new RuntimeException(
                    "Không thể xóa file: " + relativePath,
                    e);
        }
    }
}