package com.github.chen0040.tensorflow.search.models;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.tensorflow.classifiers.images.models.ImageEncoder;
import com.github.chen0040.tensorflow.classifiers.images.models.inception.InceptionImageClassifier;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class ImageSearchEngineInception implements ImageSearchEngine {
    private static final Logger logger = LoggerFactory.getLogger(ImageSearchEngine.class);
    private ImageEncoder encoder;
    private List<ImageSearchEntry> database = new ArrayList<>();
    private String indexDbPath = "/tmp/image_index_db.json";

    public ImageSearchEngineInception() {
        InceptionImageClassifier classifier = new InceptionImageClassifier();
        try {
            classifier.load_model();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encoder = classifier;
    }


    @Override
    public void purgeDb() {
        database.clear();
    }

    @Override
    public ImageSearchEntry index(File file) {
        logger.info("indexing file: " + file.getAbsolutePath());
        float[] result = encoder.encode_image(file);
        ImageSearchEntry entry = new ImageSearchEntry(file.getAbsolutePath(), result);
        database.add(entry);
        return entry;
    }

    @Override
    public void indexAll(File[] files) {
        for(File f : files) {
            index(f);
        }
    }

    @Override
    public List<ImageSearchEntry> query(File file, int pageIndex, int pageSize) {
        return query(file, pageIndex, pageSize, false);
    }

    @Override
    public List<ImageSearchEntry> query(File file, int pageIndex, int pageSize, boolean skipPerfectMatch) {
        float[] d = encoder.encode_image(file);
        List<ImageSearchEntry> temp = new ArrayList<>();
        for(ImageSearchEntry entry : database){
            if(!entry.match(d) || !skipPerfectMatch){
                temp.add(entry.makeCopy());
            }
        }
        for(ImageSearchEntry entry : temp){
            entry.setDistance(entry.getDistanceSq(d));
        }
        temp.sort(Comparator.comparingDouble(a -> a.getDistance()));

        List<ImageSearchEntry> result = new ArrayList<>();
        for(int i = pageIndex * pageSize; i < (pageIndex+1) * pageSize; ++i){
            if(i < temp.size()){
                result.add(temp.get(i));
            }
        }

        return result;
    }

    @Override
    public boolean loadIndexDbIfExists() {
        File file = new File(indexDbPath);
        if(file.exists()){
            String json = null;
            try (Stream<String> stream = Files.lines(Paths.get(indexDbPath))) {

                //1. filter line 3
                //2. convert all content to upper case
                //3. convert it into a List
                json = stream
                        .filter(line -> !line.startsWith("line3"))
                        .map(String::toUpperCase)
                        .collect(Collectors.joining());

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(json != null) {
                database.clear();
                database.addAll(JSON.parseArray(json, ImageSearchEntry.class));
            }
            return true;
        }
        return false;

    }

    @Override
    public void saveIndexDb() {
        File file = new File(indexDbPath);
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
            String json = JSON.toJSONString(database, SerializerFeature.BrowserCompatible);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
