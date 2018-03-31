package com.github.chen0040.tensorflow.recommenders.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImageUserHistory {
    private List<ImageMemo> history = new ArrayList<>();
    public void logImage(String filePath){
        history.add(new ImageMemo(filePath));
    }

    public String head(int n) {
        StringBuilder sb = new StringBuilder();
        for(int i=history.size()-1; i >= history.size()-n && i >= 0; --i) {
            sb.append("# ").append(history.size() - i).append(": ").append(history.get(i).getImagePath()).append("\n");
        }
        return sb.toString().trim();
    }
}
