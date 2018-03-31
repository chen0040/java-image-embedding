package com.github.chen0040.tensorflow.recommenders;

import com.github.chen0040.tensorflow.classifiers.images.utils.FileUtils;
import com.github.chen0040.tensorflow.recommenders.models.ImageUserHistory;
import com.github.chen0040.tensorflow.recommenders.models.KnnImageRecommender;
import com.github.chen0040.tensorflow.search.models.ImageSearchEntry;

import java.util.Collections;
import java.util.List;

public class KnnImageRecommenderDemo {
    public static void main(String[] args) {
        ImageUserHistory userHistory = new ImageUserHistory();

        List<String> audioFiles = FileUtils.getImageFilePaths();
        Collections.shuffle(audioFiles);

        for(int i=0; i < 40; ++i){
            String filePath = audioFiles.get(i);
            userHistory.logImage(filePath);
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        KnnImageRecommender recommender = new KnnImageRecommender();
        if(!recommender.loadIndexDbIfExists()) {
            recommender.indexAll(FileUtils.getImageFiles());
            recommender.saveIndexDb();
        }

        System.out.println(userHistory.head(10));

        int k = 10;
        List<ImageSearchEntry> result = recommender.recommends(userHistory.getHistory(), k);

        for(int i=0; i < result.size(); ++i){
            ImageSearchEntry entry = result.get(i);
            System.out.println("Search Result #" + (i+1) + ": " + entry.getPath());
        }




    }
}
