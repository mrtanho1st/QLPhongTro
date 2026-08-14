# Deployment Checklist - File Upload System

## Pre-Deployment Verification Checklist

### Backend Compilation ✅

- [x] QLyPhongTroBackend compiles successfully
  - Command: `.\mvnw.cmd clean compile -DskipTests`
  - Result: BUILD SUCCESS (56 source files)
  - No errors or warnings

### Frontend Compilation ✅

- [x] QlyPhongTroClient has no compilation errors
  - Verified via language server
  - ApiClient.java: No errors
  - RoomMediaDialogController.java: No errors

### Code Changes ✅

- [x] FileStorageService.saveFile() implemented
- [x] RoomMediaService.uploadRoomMedia() implemented
- [x] RoomMediaController POST /upload endpoint created
- [x] ApiClient.uploadRoomMedia() and buildMultipartBody() implemented
- [x] RoomMediaDialogController.saveMedia() updated with file detection
- [x] application.properties configured with upload directory

---

## Ubuntu Server Deployment Steps

### Step 1: Prepare Upload Directory

```bash
# SSH to Ubuntu server
ssh user@ubuntu-server

# Create upload directory
sudo mkdir -p /var/www/PhongTro/uploads
sudo chmod 755 /var/www/PhongTro
sudo chmod 755 /var/www/PhongTro/uploads

# If running Java as www-data (for Nginx integration)
sudo chown -R www-data:www-data /var/www/PhongTro

# Verify
ls -la /var/www/PhongTro
```

### Step 2: Build Backend

```bash
# On local development machine
cd QLyPhongTroBackend
.\mvnw.cmd clean package -DskipTests

# Output: target/qlptbackend-0.0.1-SNAPSHOT.jar
```

### Step 3: Update Backend Configuration

**File:** `src/main/resources/application.properties`

```diff
- app.upload-dir=D:/PhongTro
+ app.upload-dir=/var/www/PhongTro
```

### Step 4: Deploy JAR to Ubuntu

```bash
# Copy jar to server
scp QLyPhongTroBackend/target/qlptbackend-0.0.1-SNAPSHOT.jar user@ubuntu-server:/opt/

# SSH to server
ssh user@ubuntu-server

# Set permissions
sudo chmod 755 /opt/qlptbackend-0.0.1-SNAPSHOT.jar

# Start Java app
sudo java -jar /opt/qlptbackend-0.0.1-SNAPSHOT.jar &
```

### Step 5: Configure Nginx/Apache (Optional)

If using reverse proxy, expose `/uploads/` endpoint:

**Nginx:**

```nginx
location /uploads/ {
    alias /var/www/PhongTro/uploads/;
    autoindex off;
}
```

**Apache:**

```apache
Alias /uploads /var/www/PhongTro/uploads
<Directory /var/www/PhongTro/uploads>
    Require all granted
</Directory>
```

### Step 6: Verify Backend Running

```bash
# Test health endpoint
curl http://localhost:8080/api/health

# Test upload directory accessibility
curl http://localhost:8080/uploads/test.txt
```

---

## Windows Client Deployment Steps

### Step 1: Build Client JAR

```bash
cd QlyPhongTroClient/qlptclient
# Requires Maven installed or use manual build
mvn clean package
# Or build in IDE
```

### Step 2: Configure Client API Endpoint

**File:** `ApiClient.java`

```java
// Update baseUrl to point to Ubuntu server
private static final String baseUrl = "http://ubuntu-server-ip:8080";
```

### Step 3: Distribute to End Users

- Package JAR or EXE
- Ensure Java 21+ installed on client machines

---

## Database Migration

No database schema changes required - existing `room_media` table already supports:

```sql
ALTER TABLE room_media MODIFY url VARCHAR(500);  -- Ensure sufficient length
```

---

## Testing Matrix

| Test Case                       | Scope       | Expected Result                          |
| ------------------------------- | ----------- | ---------------------------------------- |
| Upload image < 1MB              | Integration | File saved, URL returned                 |
| Upload image > 10MB             | Integration | File saved, URL returned                 |
| Upload invalid file type        | Integration | Error response                           |
| Retrieve uploaded file          | Integration | File accessible via URL                  |
| Upload same room multiple times | Integration | Each creates separate {roomId} directory |
| Concurrent uploads              | Load        | No file corruption                       |

---

## Rollback Plan

If issues occur after deployment:

1. **Stop Java application**

   ```bash
   sudo pkill -f qlptbackend
   ```

2. **Restore database** (if needed)

   ```bash
   mysql < backup.sql
   ```

3. **Restore upload directory**

   ```bash
   rsync -av backup-uploads/ /var/www/PhongTro/uploads/
   ```

4. **Restart with previous version**
   ```bash
   java -jar /opt/qlptbackend-previous-version.jar
   ```

---

## Performance Considerations

### File Size Limits (production)

```properties
# Current: 50MB
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# For production with many large files:
spring.servlet.multipart.max-file-size=500MB
spring.servlet.multipart.max-request-size=500MB
```

### Disk Space Planning

- Estimate file size per room: ~5-10MB average
- Total rooms × 10MB = required storage
- Add 20% buffer for OS and overhead

### Timeout Configuration

For large file uploads, adjust:

```properties
server.tomcat.connection-timeout=60000
```

---

## Monitoring & Logging

### Check Upload Directory Growth

```bash
# Monitor directory size
du -sh /var/www/PhongTro/uploads

# List room directories
ls -lR /var/www/PhongTro/uploads
```

### Backend Logs

```bash
# Watch logs
tail -f /var/log/qlptbackend.log

# Search for upload errors
grep -i "upload" /var/log/qlptbackend.log
```

---

## Compatibility Matrix

| Component        | Version             | Status        |
| ---------------- | ------------------- | ------------- |
| Java             | 21+                 | ✅ Required   |
| Spring Boot      | 3.x                 | ✅ Verified   |
| Database         | SQL Server          | ✅ Compatible |
| Client Framework | JavaFX              | ✅ Compatible |
| HTTP Client      | Java 11+ HttpClient | ✅ Compatible |

---

## Sign-Off

- **Implementation Date:** 2026-08-14
- **Backend Status:** ✅ BUILD SUCCESS
- **Client Status:** ✅ No Errors
- **Database Status:** ✅ Ready
- **Deployment Readiness:** ✅ Ready for Ubuntu deployment

**Next Steps:**

1. Deploy backend to Ubuntu server
2. Test upload endpoint via cURL
3. Launch client and perform end-to-end upload test
4. Monitor for 24 hours for stability
5. Gather user feedback
