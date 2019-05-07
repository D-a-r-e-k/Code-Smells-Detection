/**
	 * gets an instance of an Image
	 *
	 * @param image
	 *            an Image object
	 * @return a new Image object
	 */
public static Image getInstance(Image image) {
    if (image == null)
        return null;
    try {
        Class<? extends Image> cs = image.getClass();
        Constructor<? extends Image> constructor = cs.getDeclaredConstructor(new Class[] { Image.class });
        return constructor.newInstance(new Object[] { image });
    } catch (Exception e) {
        throw new ExceptionConverter(e);
    }
}
