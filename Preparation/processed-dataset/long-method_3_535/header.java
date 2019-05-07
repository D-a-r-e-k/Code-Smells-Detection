void method0() { 
private Scene scene;
private Display display;
// resolution  
private int imageWidth;
private int imageHeight;
// bucketing  
private String bucketOrderName;
private BucketOrder bucketOrder;
private int bucketSize;
private int bucketCounter;
private int[] bucketCoords;
private boolean dumpBuckets;
// anti-aliasing  
private int minAADepth;
private int maxAADepth;
private int superSampling;
private float contrastThreshold;
private boolean jitter;
private boolean displayAA;
// derived quantities  
private double invSuperSampling;
private int subPixelSize;
private int minStepSize;
private int maxStepSize;
private int[] sigma;
private float thresh;
private boolean useJitter;
// filtering  
private String filterName;
private Filter filter;
private int fs;
private float fhs;
}
