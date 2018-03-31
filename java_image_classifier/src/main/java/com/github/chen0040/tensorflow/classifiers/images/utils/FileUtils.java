package com.github.chen0040.tensorflow.classifiers.images.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static File[] getImageFiles() {
        List<File> result = new ArrayList<>();
        File dir = new File("image_samples");
        System.out.println(dir.getAbsolutePath());

        getImageFiles(dir, result);

        File[] files = new File[result.size()];
        for(int i=0; i < files.length;++i) {
            files[i] = result.get(i);
        }
        return files;
    }

    private static void getImageFiles(File dir, List<File> result) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if(f.isDirectory()){
                    getImageFiles(f, result);
                } else {
                    String file_path = f.getAbsolutePath();
                    if (file_path.endsWith(".png")) {
                        result.add(f);
                    }
                }
            }
        }
    }
}
