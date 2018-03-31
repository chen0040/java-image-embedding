package com.github.chen0040.tensorflow.classifiers.images;

import com.github.chen0040.tensorflow.classifiers.images.models.inception.InceptionImageClassifier;
import com.github.chen0040.tensorflow.classifiers.images.utils.FileUtils;
import com.github.chen0040.tensorflow.classifiers.images.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class InceptionImageClassifierDemo {

    private static final Logger logger = LoggerFactory.getLogger(InceptionImageClassifierDemo.class);

    public static void main(String[] args) throws IOException {


        InceptionImageClassifier classifier = new InceptionImageClassifier();
        classifier.load_model(ResourceUtils.getInputStream("tf_models/tensorflow_inception_graph.pb"));
        classifier.load_labels(ResourceUtils.getInputStream("tf_models/imagenet_comp_graph_label_strings.txt"));



        for(File file : FileUtils.getImageFiles()) {
            String predicted_label = classifier.predict_image(file);
            logger.info("predicted class for {}: {}", file.getName(), predicted_label);
        }
    }
}
