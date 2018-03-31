package com.github.chen0040.tensorflow.classifiers.images.models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ImageClassifier extends ImageEncoder {
    String predict_image(BufferedImage image);
    String predict_image(BufferedImage image, int imgWidth, int imgHeight);

    default String predict_image(File file) {
        try{
            BufferedImage img = ImageIO.read(file);
            return predict_image(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
