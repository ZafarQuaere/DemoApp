package com.zaf.econnecto.model;

public class ImageUpdateModelListener {

    public interface ImageUpdateListener {
        public void isImageAdded();
    }

    private static ImageUpdateModelListener imgModelInstance;
    private ImageUpdateListener imgUpdateListener;
    private boolean isImageUpdated = false;

    private ImageUpdateModelListener(){}

    public static ImageUpdateModelListener getInstance(){
        if (imgModelInstance == null)
            imgModelInstance = new ImageUpdateModelListener();
        return imgModelInstance;
    }

    public void setImageUpdateListener(ImageUpdateListener listener){
        imgUpdateListener = listener;
    }

    public void changeState(boolean state) {
        if(imgUpdateListener != null) {
            isImageUpdated = state;
            notifyStateChange();
        }
    }

    public boolean getState() {
        return isImageUpdated;
    }

    private void notifyStateChange() {
        imgUpdateListener.isImageAdded();
    }

}
