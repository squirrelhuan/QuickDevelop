package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model;

import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.SimplePictureAdapter;

/**
 * Created by Squirrel桓 on 2018/11/29.
 */
public class PictureModel {

    private Image image;
    private int position;
    private SimplePictureAdapter.ViewHolder viewHolder;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SimplePictureAdapter.ViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(SimplePictureAdapter.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
}
