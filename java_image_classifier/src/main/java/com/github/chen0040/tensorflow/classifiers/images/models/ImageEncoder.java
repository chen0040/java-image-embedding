package com.github.chen0040.tensorflow.classifiers.images.models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ImageEncoder extends TrainedModelLoader {
    float[] encode_image(BufferedImage image, int imgWidth, int imgHeight);
    float[] encode_image(BufferedImage image);

    default float[] encode_image(File file) {
        try{
            BufferedImage img = ImageIO.read(file);
            return encode_image(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
