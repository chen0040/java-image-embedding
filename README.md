# java-image-embedding

Image embedding in Java

The current project attempts to develop a pure Java image encoder that can be used in pure Java or Android program. 
Such an image encoder can be used for image classification or image search, or image recommend-er.

The current project contains currently two deep learning networks:

* inception resnet
* cifar



# Usage


### Run image classifier in Java
 
The [sample codes](java_image_classifier/src/main/java/com/github/chen0040/tensorflow/classifiers/images/Cifar10ImageClassifierDemo.java) 
below shows how to use the cifar image classifier to predict the genres of image:

```java
import com.github.chen0040.tensorflow.classifiers.models.cifar10.Cifar10ImageClassifier;
import com.github.chen0040.tensorflow.classifiers.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

InputStream inputStream = ResourceUtils.getInputStream("tf_models/cnn_cifar10.pb");
Cifar10ImageClassifier classifier = new Cifar10ImageClassifier();
classifier.load_model(inputStream);

List<String> paths = getImageFiles();

Collections.shuffle(paths);

for (String path : paths) {
    System.out.println("Predicting " + path + " ...");
    File f = new File(path);
    String label = classifier.predict_image(f);

    System.out.println("Predicted: " + label);
}
```  

 
The [sample codes](java_image_classifier/src/main/java/com/github/chen0040/tensorflow/classifiers/images/InceptionImageClassifierDemo.java) 
below shows how to use the resnet v2 image classifier to predict the genres of image:

```java
import com.github.chen0040.tensorflow.classifiers.images.models.inception.InceptionImageClassifier;
import com.github.chen0040.tensorflow.classifiers.images.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

InceptionImageClassifier classifier = new InceptionImageClassifier();
classifier.load_model();

List<String> paths = getImageFiles();

Collections.shuffle(paths);

for (String path : paths) {
    System.out.println("Predicting " + path + " ...");
    File f = new File(path);
    String label = classifier.predict_image(f);

    System.out.println("Predicted: " + label);
}
```  

### Extract features from image in Java

The [sample codes](java_image_classifier/src/main/java/com/github/chen0040/tensorflow/classifiers/demo/Cifar10ImageEncoderDemo.java) 
below shows how to use the cifar image classifier to encode an image file into an float array:

```java
import com.github.chen0040.tensorflow.classifiers.images.models.cifar10.Cifar10ImageClassifier;
import com.github.chen0040.tensorflow.classifiers.images.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

InputStream inputStream = ResourceUtils.getInputStream("tf_models/cnn_cifar10.pb");
Cifar10ImageClassifier classifier = new Cifar10ImageClassifier();
classifier.load_model(inputStream);

List<String> paths = getImageFiles();

Collections.shuffle(paths);

for (String path : paths) {
    System.out.println("Encoding " + path + " ...");
    File f = new File(path);
    float[] encoded_image = classifier.encode_image(f);

    System.out.println("Encoded: " + Arrays.toString(encoded_image));
}
```  

 
The [sample codes](java_image_classifier/src/main/java/com/github/chen0040/tensorflow/classifiers/demo/ResNetV2ImageEncoderDemo.java) 
below shows how to the resnet v2 image classifier to encode an image file into an float array:

```java
import com.github.chen0040.tensorflow.classifiers.images.models.inception.InceptionImageClassifier;
import com.github.chen0040.tensorflow.classifiers.images.models.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

InceptionImageClassifier classifier = new InceptionImageClassifier();
classifier.load_model();

List<String> paths = getImageFiles();

Collections.shuffle(paths);

for (String path : paths) {
    System.out.println("Encoding " + path + " ...");
    File f = new File(path);
    float[] encoded_image = classifier.encode_image(f);

    System.out.println("Encoded: " + Arrays.toString(encoded_image));
}
```  

### Image Search Engine

The [sample codes](java_image_search/src/main/java/com/github/chen0040/tensorflow/search/ImageSearchEngineDemo.java) 
below shows how to index and search for image file using the [ImageSearchEngine](java_image_search/src/main/java/com/github/chen0040/tensorflow/search/models/ImageSearchEngine.java) class:

```java
ImageSearchEngine searchEngine = new ImageSearchEngineInception();
if(!searchEngine.loadIndexDbIfExists()) {
    searchEngine.indexAll(new File("image_samples").listFiles());
    searchEngine.saveIndexDb();
}

int pageIndex = 0;
int pageSize = 20;
boolean skipPerfectMatch = true;
for(File f : new File("image_samples").listFiles()) {
    System.out.println("querying similar image to " + f.getName());
    List<ImageSearchEntry> result = searchEngine.query(f, pageIndex, pageSize, skipPerfectMatch);
    for(int i=0; i < result.size(); ++i){
        System.out.println("# " + i + ": " + result.get(i).getPath() + " (distSq: " + result.get(i).getDistanceSq() + ")");
    }
}
```  

### Image Recommend-er

The [sample codes](java_image_recommender/src/main/java/com/github/chen0040/tensorflow/search/KnnImageRecommenderDemo.java) 
below shows how to recommend images based on user's image history using the [KnnImageRecommender](java_image_recommender/src/main/java/com/github/chen0040/tensorflow/search/models/KnnImageRecommender.java) class:

```java
ImageUserHistory userHistory = new ImageUserHistory();

List<String> imageFiles = FileUtils.getImageFiles();
Collections.shuffle(imageFiles);

for(int i=0; i < 40; ++i){
    String filePath = imageFiles.get(i);
    userHistory.logImage(filePath);
    try {
        Thread.sleep(100L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

KnnImageRecommender recommender = new KnnImageRecommender();
if(!recommender.loadIndexDbIfExists()) {
    recommender.indexAll(new File("image_samples").listFiles(a -> a.getAbsolutePath().toLowerCase().endsWith(".au")));
    recommender.saveIndexDb();
}

System.out.println(userHistory.head(10));

int k = 10;
List<ImageSearchEntry> result = recommender.recommends(userHistory.getHistory(), k);

for(int i=0; i < result.size(); ++i){
    ImageSearchEntry entry = result.get(i);
    System.out.println("Search Result #" + (i+1) + ": " + entry.getPath());
}

```