void method0() { 
/** UTF-8 **/
public static final EncodingInfo UTF_8 = new EncodingInfo("UTF-8", null, false);
/** UTF-8, with BOM **/
public static final EncodingInfo UTF_8_WITH_BOM = new EncodingInfo("UTF-8", null, true);
/** UTF-16, big-endian **/
public static final EncodingInfo UTF_16_BIG_ENDIAN = new EncodingInfo("UTF-16", Boolean.TRUE, false);
/** UTF-16, big-endian with BOM **/
public static final EncodingInfo UTF_16_BIG_ENDIAN_WITH_BOM = new EncodingInfo("UTF-16", Boolean.TRUE, true);
/** UTF-16, little-endian **/
public static final EncodingInfo UTF_16_LITTLE_ENDIAN = new EncodingInfo("UTF-16", Boolean.FALSE, false);
/** UTF-16, little-endian with BOM **/
public static final EncodingInfo UTF_16_LITTLE_ENDIAN_WITH_BOM = new EncodingInfo("UTF-16", Boolean.FALSE, true);
/** UCS-4, big-endian **/
public static final EncodingInfo UCS_4_BIG_ENDIAN = new EncodingInfo("ISO-10646-UCS-4", Boolean.TRUE, false);
/** UCS-4, little-endian **/
public static final EncodingInfo UCS_4_LITTLE_ENDIAN = new EncodingInfo("ISO-10646-UCS-4", Boolean.FALSE, false);
/** UCS-4, unusual byte-order (2143) or (3412) **/
public static final EncodingInfo UCS_4_UNUSUAL_BYTE_ORDER = new EncodingInfo("ISO-10646-UCS-4", null, false);
/** EBCDIC **/
public static final EncodingInfo EBCDIC = new EncodingInfo("CP037", null, false);
public final String encoding;
public final Boolean isBigEndian;
public final boolean hasBOM;
}
