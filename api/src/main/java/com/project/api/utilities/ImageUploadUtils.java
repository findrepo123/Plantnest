package com.project.api.utilities;

import com.project.api.entities.Image;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Component
public class ImageUploadUtils {

    public String uploadImgBase64(String folderName, Image image) {
        try {
            String base64Prefix = "data:image/";
            String base64Image = image.getImageUrl();

            // Detect image format from the data URI prefix
            String imageFormat = "jpeg"; // Default to JPEG format
            if (base64Image.startsWith(base64Prefix + "jpeg;base64,")) {
                imageFormat = "jpeg";
            } else if (base64Image.startsWith(base64Prefix + "png;base64,")) {
                imageFormat = "png";
            }

            // Remove the data URI prefix to get the actual Base64 string
            base64Image = base64Image.substring(base64Image.indexOf(",") + 1);

            // Generate a unique filename and extension
            String name = UUID.randomUUID().toString().replace("-", "");
            String extension = "." + imageFormat;
            String fileName = name + extension;

            // Decode the Base64-encoded image data to a byte array
            byte[] imageByte = Base64.getDecoder().decode(base64Image);

            // Save the decoded image data to the specified folder
            Path path = Paths.get("upload/" + folderName + "/" + fileName);
            Files.write(path, imageByte);

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String upload(String folderName, MultipartFile file) {
        try {
            String name = UUID.randomUUID().toString().replace("-", "");
            int lastIndex = file.getOriginalFilename().lastIndexOf(".");
            String extension = file.getOriginalFilename().substring(lastIndex);
            String fileName = name + extension;

            Path path = Paths.get("upload/" + folderName + "/"+ fileName);
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);

            return fileName;
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(String folderName, String fileName) {
        try {
            Path path = Paths.get("upload/" + folderName + "/"+ fileName);
            if(Files.exists(path)) {
                Files.delete(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
