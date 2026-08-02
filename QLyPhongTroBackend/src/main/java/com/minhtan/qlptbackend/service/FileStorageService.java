package com.minhtan.qlptbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Service
public class FileStorageService {

    private final Path uploadRoot;

    public FileStorageService(
            @Value("${app.upload-dir}") String uploadDir) {

        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Chuyển mọi loại đường dẫn thành đường dẫn tương đối.
     *
     * Hỗ trợ:
     * D:\PhongTro\1\img.png
     * file:/D:/PhongTro/1/img.png
     * /uploads/1/img.png
     * 1/img.png
     *
     * Kết quả:
     * 1/img.png
     */
    public String toRelativePath(String path) {

        if (path == null || path.isBlank()) {
            return "";
        }

        path = path.trim();

        // Đã là URL public
        if (path.startsWith("/uploads/")) {
            return path.substring("/uploads/".length());
        }

        // file:/D:/...
        if (path.startsWith("file:")) {
            try {
                path = Path.of(URI.create(path))
                        .toString();
            } catch (Exception e) {
                return "";
            }
        }

        try {

            Path p = Path.of(path);

            // Đã là đường dẫn tương đối
            if (!p.isAbsolute()) {
                return p.normalize()
                        .toString()
                        .replace("\\", "/");
            }

            // Đường dẫn tuyệt đối nằm trong thư mục upload
            if (p.startsWith(uploadRoot)) {

                return uploadRoot.relativize(p)
                        .toString()
                        .replace("\\", "/");
            }

            throw new IllegalArgumentException(
                    "Đường dẫn không thuộc thư mục upload: " + path);

        } catch (InvalidPathException e) {

            throw new IllegalArgumentException(
                    "Đường dẫn không hợp lệ: " + path,
                    e);
        }
    }

    /**
     * Chuyển đường dẫn tương đối thành đường dẫn tuyệt đối.
     */
    public Path toAbsolutePath(String path) {

        if (path == null || path.isBlank()) {
            return uploadRoot;
        }

        path = toRelativePath(path);

        return uploadRoot.resolve(path)
                .normalize();
    }

    /**
     * Sinh URL public.
     *
     * Ví dụ:
     * 1/img.png
     * ->
     * /uploads/1/img.png
     */
    public String toPublicUrl(String path) {

        String relative = toRelativePath(path);

        if (relative.isBlank()) {
            return "";
        }

        return "/uploads/" + relative;
    }

    /**
     * Xóa file.
     */
    public void delete(String path) {

        if (path == null || path.isBlank()) {
            return;
        }

        try {

            Path file = toAbsolutePath(path);

            Files.deleteIfExists(file);

        }
        // Dữ liệu cũ không hợp lệ thì bỏ qua
        catch (InvalidPathException e) {
            System.out.println("Đường dẫn không hợp lệ: " + path);
        } catch (IllegalArgumentException e) {
            System.out.println("Bỏ qua đường dẫn cũ: " + path);
        }
        // File đã bị xóa thì bỏ qua
        catch (java.nio.file.NoSuchFileException e) {

            System.out.println("Không tìm thấy file: " + path);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Không thể xóa file: " + path,
                    e);
        }
    }
}