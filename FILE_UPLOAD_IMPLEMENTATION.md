# File Upload Implementation - Documentation

## Overview

Đã implement hệ thống upload file từ client (QlyPhongTroClient) đến backend (QLyPhongTroBackend) với cơ chế:

- Client gửi file dưới dạng **MultipartFile** qua HTTP
- Backend lưu file vào disk tại `/var/www/PhongTro/uploads/{roomId}/`
- Backend lưu **đường dẫn tương đối** vào database (ví dụ: `1/1723645200000.jpg`)

---

## Backend Changes

### 1. **FileStorageService.java**

**Thêm method:** `saveFile(Integer roomId, String originalFilename, byte[] fileBytes)`

```java
/**
 * Lưu file từ MultipartFile.
 *
 * Trả về đường dẫn tương đối.
 * Ví dụ: 1/img.jpg
 */
public String saveFile(Integer roomId, String originalFilename, byte[] fileBytes) throws IOException {
    // 1. Tạo thư mục {roomId} nếu chưa tồn tại
    Path roomDir = uploadRoot.resolve(String.valueOf(roomId));
    Files.createDirectories(roomDir);

    // 2. Tạo tên file duy nhất (timestamp + extension)
    String fileName = System.currentTimeMillis() + extension;
    Path filePath = roomDir.resolve(fileName);

    // 3. Lưu file
    Files.write(filePath, fileBytes);

    // 4. Trả về đường dẫn tương đối
    return roomId + "/" + fileName;
}
```

### 2. **RoomMediaService.java**

**Thêm import:** `org.springframework.web.multipart.MultipartFile`

**Thêm method:** `uploadRoomMedia(Integer roomId, MultipartFile file, Byte mediaType, Integer sortOrder)`

```java
/**
 * Upload file từ MultipartFile và lưu vào database.
 */
public RoomMedia uploadRoomMedia(Integer roomId, MultipartFile file, Byte mediaType, Integer sortOrder)
        throws IOException {

    if (file.isEmpty()) {
        throw new IllegalArgumentException("File không được để trống");
    }

    // 1. Lưu file vào đĩa cứng → trả về đường dẫn tương đối
    String relativePath = fileStorageService.saveFile(roomId, file.getOriginalFilename(), file.getBytes());

    // 2. Tạo entity và lưu vào database
    RoomMedia roomMedia = new RoomMedia();
    roomMedia.setRoomId(roomId);
    roomMedia.setUrl(relativePath);  // Lưu đường dẫn tương đối
    roomMedia.setMediaType(mediaType);
    roomMedia.setSortOrder(sortOrder);

    RoomMedia saved = roomMediaRepository.save(roomMedia);

    return toPublic(saved);  // Chuyển đường dẫn thành URL public
}
```

### 3. **RoomMediaController.java**

**Thêm import:** `org.springframework.web.multipart.MultipartFile`

**Thêm endpoint:** `POST /api/room-media/upload`

```java
@PostMapping("/upload")
public ResponseEntity<RoomMedia> uploadRoomMedia(
        @RequestParam Integer roomId,
        @RequestParam MultipartFile file,
        @RequestParam Byte mediaType,
        @RequestParam(required = false) Integer sortOrder) {
    try {
        RoomMedia createdRoomMedia = roomMediaService.uploadRoomMedia(roomId, file, mediaType, sortOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomMedia);
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

### 4. **application.properties**

**Cập nhật:**

```properties
# File upload directory
# For development (Windows): D:/PhongTro
# For production (Ubuntu): /var/www/PhongTro
app.upload-dir=D:/PhongTro

# Allow large file uploads (max 50MB)
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

---

## Frontend Changes

### 5. **ApiClient.java**

**Thêm method:** `uploadRoomMedia(Integer roomId, java.io.File file, Byte mediaType, Integer sortOrder)`

Gửi request multipart/form-data với:

- `roomId` (form field)
- `mediaType` (form field)
- `sortOrder` (form field)
- `file` (file upload)

```java
public RoomMedia uploadRoomMedia(Integer roomId, java.io.File file, Byte mediaType, Integer sortOrder)
        throws IOException, InterruptedException {

    String boundary = "----FormBoundary" + System.currentTimeMillis();
    byte[] bodyBytes = buildMultipartBody(roomId, file, mediaType, sortOrder, boundary);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/api/room-media/upload"))
            .timeout(Duration.ofSeconds(30))
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .POST(HttpRequest.BodyPublishers.ofByteArray(bodyBytes))
            .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    ensureSuccess(response);
    return objectMapper.readValue(response.body(), RoomMedia.class);
}
```

### 6. **RoomMediaDialogController.java**

**Sửa method:** `saveMedia()`

Khi lưu media:

- Nếu URL là **file local** (tồn tại trên đĩa cứng) → gọi `uploadRoomMedia()` để upload
- Nếu URL là **link/path từ server** → gọi `createRoomMedia()` bình thường

```java
public void saveMedia() {
    // ...
    for (RoomMedia media : mediaItems) {
        // ...
        if (media.getMediaId() == null) {
            // Media mới - kiểm tra xem có phải file local hay không
            java.io.File localFile = new java.io.File(media.getUrl());
            if (localFile.exists() && localFile.isFile()) {
                // Upload file từ local
                apiClient.uploadRoomMedia(room.getRoomId(), localFile, media.getMediaType(), media.getSortOrder());
            } else {
                // URL từ link hoặc đã là path tương đối từ server
                apiClient.createRoomMedia(media);
            }
        } else {
            // Media đã tồn tại - chỉ update metadata
            apiClient.updateRoomMedia(media.getMediaId(), media);
        }
    }
    // ...
}
```

---

## Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│            Client (QlyPhongTroClient) - Desktop              │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ User selects image file
                       │ → File stored as absolute path in RoomMedia.url
                       │
                       ▼
        ┌──────────────────────────────────────────┐
        │    RoomMediaDialogController.saveMedia() │
        └──────────────────────────────────────────┘
                       │
                       ├─ Check: Is this local file?
                       │
         ┌─────────────┴──────────────┐
         │                            │
        YES                           NO
         │                            │
         ▼                            ▼
  Upload File             Use as URL/Link
   (multipart)            (createRoomMedia)
         │
         │  POST /api/room-media/upload
         │  Content-Type: multipart/form-data
         │  - roomId
         │  - file (binary)
         │  - mediaType
         │  - sortOrder
         │
         ▼
┌─────────────────────────────────────────────────────────────┐
│         Backend (QLyPhongTroBackend) - Ubuntu Server         │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
    ┌────────────────────────────────────────────┐
    │  RoomMediaController.uploadRoomMedia()    │
    └────────────────────────────────────────────┘
                       │
                       ▼
    ┌────────────────────────────────────────────┐
    │  RoomMediaService.uploadRoomMedia()       │
    └────────────────────────────────────────────┘
                       │
         ┌─────────────┴──────────────┐
         │                            │
         ▼                            ▼
    Save to Disk              Save to Database
  /var/www/PhongTro/        (relative path)
    uploads/1/               1/1723645200000.jpg
    1723645200000.jpg
         │                            │
         └─────────────┬──────────────┘
                       │
                       ▼
              Return RoomMedia with
              URL as /uploads/1/1723645200000.jpg
                       │
                       ▼
                Client receives it
```

---

## Deployment Guide

### Điều kiện triển khai lên Ubuntu Server

#### 1. **Chuẩn bị thư mục upload**

```bash
# Trên Ubuntu server
sudo mkdir -p /var/www/PhongTro/uploads
sudo chmod -R 755 /var/www/PhongTro
sudo chown -R www-data:www-data /var/www/PhongTro  # nếu dùng Nginx/Apache
```

#### 2. **Cập nhật application.properties**

```properties
# Thay đổi từ:
app.upload-dir=D:/PhongTro

# Sang:
app.upload-dir=/var/www/PhongTro
```

#### 3. **Cấu hình Nginx/Apache (nếu dùng reverse proxy)**

```nginx
# Nginx example
location /uploads/ {
    alias /var/www/PhongTro/uploads/;
    autoindex off;
}
```

#### 4. **Rebuild & Deploy**

```bash
cd QLyPhongTroBackend
./mvnw.cmd package -DskipTests
# Copy jar file to server
java -jar qlptbackend-0.0.1-SNAPSHOT.jar
```

---

## Database Schema

Database chỉ lưu **đường dẫn tương đối** trong bảng `room_media`:

```sql
CREATE TABLE room_media (
    media_id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INT NOT NULL,
    media_type TINYINT,  -- 1: Image, 2: Video
    url VARCHAR(500),    -- Ví dụ: "1/1723645200000.jpg"
    sort_order INT,
    FOREIGN KEY (room_id) REFERENCES room(room_id)
);
```

---

## File Size Limits

- **Max file size:** 50MB (configurable in `spring.servlet.multipart.max-file-size`)
- **Max request size:** 50MB (configurable in `spring.servlet.multipart.max-request-size`)

Có thể thay đổi trong `application.properties`:

```properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

---

## Testing

### Test Upload via Frontend

1. Mở QlyPhongTroClient
2. Chọn Room
3. Click vào tab Media
4. Click "Thêm media" → chọn file từ local
5. Click "Lưu"
6. Kiểm tra database: `room_media.url` sẽ là `1/1723645200000.jpg`

### Test via cURL (Backend)

```bash
curl -X POST http://localhost:8080/api/room-media/upload \
  -F "roomId=1" \
  -F "file=@/path/to/image.jpg" \
  -F "mediaType=1" \
  -F "sortOrder=1"
```

### Test via PowerShell (Client Desktop)

```powershell
$file = "C:\Users\YourName\Pictures\test.jpg"
$roomId = 1
$mediaType = 1
$sortOrder = 1

# Build multipart body manually and send via HttpClient
```

---

## Troubleshooting

| Issue                             | Solution                                                           |
| --------------------------------- | ------------------------------------------------------------------ |
| `NullPointerException` khi upload | Kiểm tra `app.upload-dir` trong `application.properties`           |
| File upload timeout               | Tăng `spring.servlet.multipart.max-file-size` hoặc timeout setting |
| Permission denied (Linux)         | Đảm bảo `/var/www/PhongTro` có quyền write cho user running Java   |
| URL không hiển thị                | Kiểm tra `StaticResourceConfig` đã configured `/uploads/**`        |

---

## Summary of Changes

| File                           | Change                       | Type     |
| ------------------------------ | ---------------------------- | -------- |
| FileStorageService.java        | Thêm `saveFile()`            | Backend  |
| RoomMediaService.java          | Thêm `uploadRoomMedia()`     | Backend  |
| RoomMediaController.java       | Thêm `POST /upload` endpoint | Backend  |
| application.properties         | Thêm multipart config        | Backend  |
| ApiClient.java                 | Thêm `uploadRoomMedia()`     | Frontend |
| RoomMediaDialogController.java | Sửa `saveMedia()`            | Frontend |

---

**Status:** ✅ Triển khai hoàn tất - Sẵn sàng kiểm thử  
**Ngày:** 2026-08-14  
**Version:** 1.0
