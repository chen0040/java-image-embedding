package com.github.chen0040.tensorflow.recommenders.models;

import com.github.chen0040.tensorflow.search.models.ImageSearchEngine;
import com.github.chen0040.tensorflow.search.models.ImageSearchEntry;

import java.util.List;

public interface ImageRecommender extends ImageSearchEngine {
    List<ImageSearchEntry> recommends(List<ImageMemo> userHistory, int k);
}
