package com.scm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstants;
import com.scm.services.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String fileName) {



        try {
            byte data[] = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);

            cloudinary.uploader().upload(data, ObjectUtils.asMap(
               "public_id", fileName
            ));

            return this.getUrlFromPublicId(fileName);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {

        return cloudinary.url()
                .transformation(
                        new Transformation<>()
                                .width(AppConstants.CONTACT_IMAGE_WIDTH)
                                .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                                .crop(AppConstants.CONTACT_IMAGE_CROP)
                )
                .generate(publicId);
    }

    @Override
    public void delete(String cloudinaryImagePublicId) {

        try {
            if (cloudinaryImagePublicId != null && !cloudinaryImagePublicId.isEmpty()) {

                var result = cloudinary.uploader().destroy(
                        cloudinaryImagePublicId,
                        ObjectUtils.emptyMap()
                );

                System.out.println("Delete result: " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}