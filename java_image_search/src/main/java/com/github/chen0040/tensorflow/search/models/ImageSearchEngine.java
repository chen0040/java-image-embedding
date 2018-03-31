package com.github.chen0040.tensorflow.search.models;

import java.io.File;
import java.util.List;

public interface ImageSearchEngine {

    void purgeDb();

    ImageSearchEntry index(File file);

    void indexAll(File[] files);

    List<ImageSearchEntry> query(File file, int pageIndex, int pageSize);

    List<ImageSearchEntry> query(File file, int pageIndex, int pageSize, boolean skipPerfectMatch);

    boolean loadIndexDbIfExists();

    void saveIndexDb();
}
