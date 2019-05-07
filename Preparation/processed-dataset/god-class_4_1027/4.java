protected Resource getSkinImage(String screen, String key) {
    ByteArrayOutputStream baos = Server.getServer().getSkin().getImage(this.getClass().getName(), screen, key);
    if (mLastObject != null && mLastResource != null) {
        if (mLastObject == baos) {
            return mLastResource;
        }
    }
    mLastObject = baos;
    if (baos != null) {
        /*
             * if (image.getWidth() > 640 || image.getHeight() > 480) image = ImageManipulator.getScaledImage(image,
             * 640, 480);
             */
        try {
            mLastResource = createImage(baos.toByteArray());
            return mLastResource;
        } catch (Exception ex) {
            Tools.logException(DefaultApplication.class, ex);
        }
    }
    mLastResource = createImage(Tools.getDefaultImage());
    return mLastResource;
}
