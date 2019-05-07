void method0() { 
// static final membervariables 
/** this is a kind of image alignment. */
public static final int DEFAULT = 0;
/** this is a kind of image alignment. */
public static final int RIGHT = 2;
/** this is a kind of image alignment. */
public static final int LEFT = 0;
/** this is a kind of image alignment. */
public static final int MIDDLE = 1;
/** this is a kind of image alignment. */
public static final int TEXTWRAP = 4;
/** this is a kind of image alignment. */
public static final int UNDERLYING = 8;
/** This represents a coordinate in the transformation matrix. */
public static final int AX = 0;
/** This represents a coordinate in the transformation matrix. */
public static final int AY = 1;
/** This represents a coordinate in the transformation matrix. */
public static final int BX = 2;
/** This represents a coordinate in the transformation matrix. */
public static final int BY = 3;
/** This represents a coordinate in the transformation matrix. */
public static final int CX = 4;
/** This represents a coordinate in the transformation matrix. */
public static final int CY = 5;
/** This represents a coordinate in the transformation matrix. */
public static final int DX = 6;
/** This represents a coordinate in the transformation matrix. */
public static final int DY = 7;
/** type of image */
public static final int ORIGINAL_NONE = 0;
/** type of image */
public static final int ORIGINAL_JPEG = 1;
/** type of image */
public static final int ORIGINAL_PNG = 2;
/** type of image */
public static final int ORIGINAL_GIF = 3;
/** type of image */
public static final int ORIGINAL_BMP = 4;
/** type of image */
public static final int ORIGINAL_TIFF = 5;
/** type of image */
public static final int ORIGINAL_WMF = 6;
/** type of image */
public static final int ORIGINAL_PS = 7;
/** type of image */
public static final int ORIGINAL_JPEG2000 = 8;
/**
	 * type of image
	 * @since	2.1.5
	 */
public static final int ORIGINAL_JBIG2 = 9;
// member variables 
/** The image type. */
protected int type;
/** The URL of the image. */
protected URL url;
/** The raw data of the image. */
protected byte rawData[];
/** The bits per component of the raw image. It also flags a CCITT image. */
protected int bpc = 1;
/** The template to be treated as an image. */
protected PdfTemplate template[] = new PdfTemplate[1];
/** The alignment of the Image. */
protected int alignment;
/** Text that can be shown instead of the image. */
protected String alt;
/** This is the absolute X-position of the image. */
protected float absoluteX = Float.NaN;
/** This is the absolute Y-position of the image. */
protected float absoluteY = Float.NaN;
/** This is the width of the image without rotation. */
protected float plainWidth;
/** This is the width of the image without rotation. */
protected float plainHeight;
/** This is the scaled width of the image taking rotation into account. */
protected float scaledWidth;
/** This is the original height of the image taking rotation into account. */
protected float scaledHeight;
/**
     * The compression level of the content streams.
     * @since	2.1.3
     */
protected int compressionLevel = PdfStream.DEFAULT_COMPRESSION;
/** an iText attributed unique id for this image. */
protected Long mySerialId = getSerialId();
// image from indirect reference 
/**
     * Holds value of property directReference.
     * An image is embedded into a PDF as an Image XObject.
     * This object is referenced by a PdfIndirectReference object.
     */
private PdfIndirectReference directReference;
// serial stamping 
/** a static that is used for attributing a unique id to each image. */
static long serialId = 0;
// rotation, note that the superclass also has a rotation value. 
/** This is the rotation of the image in radians. */
protected float rotationRadians;
/** Holds value of property initialRotation. */
private float initialRotation;
// indentations 
/** the indentation to the left. */
protected float indentationLeft = 0;
/** the indentation to the right. */
protected float indentationRight = 0;
/** The spacing before the image. */
protected float spacingBefore;
/** The spacing after the image. */
protected float spacingAfter;
// widthpercentage (for the moment only used in ColumnText) 
/**
	 * Holds value of property widthPercentage.
	 */
private float widthPercentage = 100;
// annotation 
/** if the annotation is not null the image will be clickable. */
protected Annotation annotation = null;
// Optional Content 
/** Optional Content layer to which we want this Image to belong. */
protected PdfOCG layer;
// interpolation 
/** Holds value of property interpolation. */
protected boolean interpolation;
// original type and data 
/** Holds value of property originalType. */
protected int originalType = ORIGINAL_NONE;
/** Holds value of property originalData. */
protected byte[] originalData;
// the following values are only set for specific types of images. 
/** Holds value of property deflated. */
protected boolean deflated = false;
// DPI info 
/** Holds value of property dpiX. */
protected int dpiX = 0;
/** Holds value of property dpiY. */
protected int dpiY = 0;
// XY Ratio 
/** Holds value of property XYRatio. */
private float XYRatio = 0;
// color, colorspaces and transparency 
/** this is the colorspace of a jpeg-image. */
protected int colorspace = -1;
/** Image color inversion */
protected boolean invert = false;
/** ICC Profile attached */
protected ICC_Profile profile = null;
/** a dictionary with additional information */
private PdfDictionary additional = null;
/** Is this image a mask? */
protected boolean mask = false;
/** The image that serves as a mask for this image. */
protected Image imageMask;
/** Holds value of property smask. */
private boolean smask;
/** this is the transparency information of the raw image */
protected int transparency[];
}
