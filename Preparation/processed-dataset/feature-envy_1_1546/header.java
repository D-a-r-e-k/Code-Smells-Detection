void method0() { 
protected ViewerCanvas view;
protected BufferedImage theImage;
protected Graphics2D imageGraphics;
protected int pixel[], zbuffer[];
protected boolean hideBackfaces;
protected int templatePixel[];
protected Rectangle bounds;
private static Vec2 reuseVec2[];
private static WeakHashMap<Image, SoftReference<ImageRecord>> imageMap = new WeakHashMap<Image, SoftReference<ImageRecord>>();
private static WeakHashMap<Image, SoftReference<RenderingMesh>> imageMeshMap = new WeakHashMap<Image, SoftReference<RenderingMesh>>();
private static final int MODE_COPY = 0;
private static final int MODE_ADD = 1;
private static final int MODE_SUBTRACT = 2;
}
