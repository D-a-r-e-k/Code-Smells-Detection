// endEntity()  
/**
     * Returns the IANA encoding name that is auto-detected from
     * the bytes specified, with the endian-ness of that encoding where appropriate.
     *
     * @param b4    The first four bytes of the input.
     * @param count The number of bytes actually read.
     * @return an instance of EncodingInfo which represents the auto-detected encoding.
     */
protected EncodingInfo getEncodingInfo(byte[] b4, int count) {
    if (count < 2) {
        return EncodingInfo.UTF_8;
    }
    // UTF-16, with BOM  
    int b0 = b4[0] & 0xFF;
    int b1 = b4[1] & 0xFF;
    if (b0 == 0xFE && b1 == 0xFF) {
        // UTF-16, big-endian  
        return EncodingInfo.UTF_16_BIG_ENDIAN_WITH_BOM;
    }
    if (b0 == 0xFF && b1 == 0xFE) {
        // UTF-16, little-endian  
        return EncodingInfo.UTF_16_LITTLE_ENDIAN_WITH_BOM;
    }
    // default to UTF-8 if we don't have enough bytes to make a  
    // good determination of the encoding  
    if (count < 3) {
        return EncodingInfo.UTF_8;
    }
    // UTF-8 with a BOM  
    int b2 = b4[2] & 0xFF;
    if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
        return EncodingInfo.UTF_8_WITH_BOM;
    }
    // default to UTF-8 if we don't have enough bytes to make a  
    // good determination of the encoding  
    if (count < 4) {
        return EncodingInfo.UTF_8;
    }
    // other encodings  
    int b3 = b4[3] & 0xFF;
    if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x3C) {
        // UCS-4, big endian (1234)  
        return EncodingInfo.UCS_4_BIG_ENDIAN;
    }
    if (b0 == 0x3C && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
        // UCS-4, little endian (4321)  
        return EncodingInfo.UCS_4_LITTLE_ENDIAN;
    }
    if (b0 == 0x00 && b1 == 0x00 && b2 == 0x3C && b3 == 0x00) {
        // UCS-4, unusual octet order (2143)  
        // REVISIT: What should this be?  
        return EncodingInfo.UCS_4_UNUSUAL_BYTE_ORDER;
    }
    if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x00) {
        // UCS-4, unusual octect order (3412)  
        // REVISIT: What should this be?  
        return EncodingInfo.UCS_4_UNUSUAL_BYTE_ORDER;
    }
    if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F) {
        // UTF-16, big-endian, no BOM  
        // (or could turn out to be UCS-2...  
        // REVISIT: What should this be?  
        return EncodingInfo.UTF_16_BIG_ENDIAN;
    }
    if (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00) {
        // UTF-16, little-endian, no BOM  
        // (or could turn out to be UCS-2...  
        return EncodingInfo.UTF_16_LITTLE_ENDIAN;
    }
    if (b0 == 0x4C && b1 == 0x6F && b2 == 0xA7 && b3 == 0x94) {
        // EBCDIC  
        // a la xerces1, return CP037 instead of EBCDIC here  
        return EncodingInfo.EBCDIC;
    }
    // default encoding  
    return EncodingInfo.UTF_8;
}
