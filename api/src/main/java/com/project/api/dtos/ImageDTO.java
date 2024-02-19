package com.project.api.dtos;

import com.project.api.entities.Image;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO implements Serializable {
    private Integer imageId;
    private String imageUrl;

    public ImageDTO(Image image) {
        this.imageId = image.getImageId();
        this.imageUrl = image.getImageUrl();
    }
}