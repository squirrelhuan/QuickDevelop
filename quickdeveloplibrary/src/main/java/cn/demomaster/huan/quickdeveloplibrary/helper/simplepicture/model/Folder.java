package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model;

/**
 * Created by Squirrel桓 on 2018/11/28.
 */

import android.text.TextUtils;

import com.google.zxing.common.StringUtils;

import java.util.ArrayList;

/**
 * 图片文件夹实体类
 */
public class Folder {

    private String name;
    private ArrayList<Image> images;

    public Folder(String name) {
        this.name = name;
    }

    public Folder(String name, ArrayList<Image> images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void addImage(Image image) {
        if (image != null && TextUtils.isEmpty(image.getPath())) {
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(image);
        }
    }
}

