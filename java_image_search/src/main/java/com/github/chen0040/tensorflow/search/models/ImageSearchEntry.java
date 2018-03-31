package com.github.chen0040.tensorflow.search.models;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Date;

@Getter
@Setter
public class ImageSearchEntry {

    private String path;
    private float[] features;
    private long indexedTime;
    private double distance;
    private String error;

    public double getDistanceSq(float[] d) {
        double distanceSq = 0;
        for(int i=0; i < d.length; ++i){
            distanceSq += (d[i] - features[i]) * (d[i] - features[i]);
        }
        return distanceSq;
    }

    public ImageSearchEntry() {

    }

    public ImageSearchEntry(String path, float[] result) {
        this.indexedTime = new Date().getTime();
        this.features = result;
        this.path = path;
    }

    public boolean match(float[] d) {
        return getDistanceSq(d) == 0;
    }

    public String name() {
        return new File(path).getName();
    }

    public ImageSearchEntry makeCopy() {
        ImageSearchEntry result = new ImageSearchEntry(this.path, this.features);
        result.indexedTime = this.indexedTime;
        result.distance = this.distance;
        return result;
    }

    public static ImageSearchEntry createAlert(String error) {
        return new ImageSearchEntry().alert(error);
    }

    public ImageSearchEntry alert(String error) {
        this.error = error;
        return this;
    }

    public ImageSearchEntry withDistance(double distance) {
        this.distance = distance;
        return this;
    }
}
