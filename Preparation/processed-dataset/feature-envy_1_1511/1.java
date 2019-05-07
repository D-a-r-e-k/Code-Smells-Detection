public void decodeT6(byte[] buffer, byte[] compData, int startX, int height, long tiffT6Options) {
    this.data = compData;
    compression = 4;
    bitPointer = 0;
    bytePointer = 0;
    int scanlineStride = (w + 7) / 8;
    int a0, a1, b1, b2;
    int entry, code, bits;
    boolean isWhite;
    int currIndex;
    int temp[];
    // Return values from getNextChangingElement 
    int[] b = new int[2];
    // uncompressedMode - have written some code for this, but this 
    // has not been tested due to lack of test images using this optional 
    uncompressedMode = (int) ((tiffT6Options & 0x02) >> 1);
    // Local cached reference 
    int[] cce = currChangingElems;
    // Assume invisible preceding row of all white pixels and insert 
    // both black and white changing elements beyond the end of this 
    // imaginary scanline. 
    changingElemSize = 0;
    cce[changingElemSize++] = w;
    cce[changingElemSize++] = w;
    int lineOffset = 0;
    int bitOffset;
    for (int lines = 0; lines < height; lines++) {
        // a0 has to be set just before the start of the scanline. 
        a0 = -1;
        isWhite = true;
        // Assign the changing elements of the previous scanline to 
        // prevChangingElems and start putting this new scanline's 
        // changing elements into the currChangingElems. 
        temp = prevChangingElems;
        prevChangingElems = currChangingElems;
        cce = currChangingElems = temp;
        currIndex = 0;
        // Start decoding the scanline at startX in the raster 
        bitOffset = startX;
        // Reset search start position for getNextChangingElement 
        lastChangingElement = 0;
        // Till one whole scanline is decoded 
        while (bitOffset < w) {
            // Get the next changing element 
            getNextChangingElement(a0, isWhite, b);
            b1 = b[0];
            b2 = b[1];
            // Get the next seven bits 
            entry = nextLesserThan8Bits(7);
            // Run these through the 2DCodes table 
            entry = twoDCodes[entry] & 0xff;
            // Get the code and the number of bits used up 
            code = (entry & 0x78) >>> 3;
            bits = entry & 0x07;
            if (code == 0) {
                // Pass 
                // We always assume WhiteIsZero format for fax. 
                if (!isWhite) {
                    setToBlack(buffer, lineOffset, bitOffset, b2 - bitOffset);
                }
                bitOffset = a0 = b2;
                // Set pointer to only consume the correct number of bits. 
                updatePointer(7 - bits);
            } else if (code == 1) {
                // Horizontal 
                // Set pointer to only consume the correct number of bits. 
                updatePointer(7 - bits);
                // identify the next 2 alternating color codes. 
                int number;
                if (isWhite) {
                    // Following are white and black runs 
                    number = decodeWhiteCodeWord();
                    bitOffset += number;
                    cce[currIndex++] = bitOffset;
                    number = decodeBlackCodeWord();
                    setToBlack(buffer, lineOffset, bitOffset, number);
                    bitOffset += number;
                    cce[currIndex++] = bitOffset;
                } else {
                    // First a black run and then a white run follows 
                    number = decodeBlackCodeWord();
                    setToBlack(buffer, lineOffset, bitOffset, number);
                    bitOffset += number;
                    cce[currIndex++] = bitOffset;
                    number = decodeWhiteCodeWord();
                    bitOffset += number;
                    cce[currIndex++] = bitOffset;
                }
                a0 = bitOffset;
            } else if (code <= 8) {
                // Vertical 
                a1 = b1 + (code - 5);
                cce[currIndex++] = a1;
                // We write the current color till a1 - 1 pos, 
                // since a1 is where the next color starts 
                if (!isWhite) {
                    setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
                }
                bitOffset = a0 = a1;
                isWhite = !isWhite;
                updatePointer(7 - bits);
            } else if (code == 11) {
                if (nextLesserThan8Bits(3) != 7) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.encountered.while.decoding.2d.group.4.compressed.data"));
                }
                int zeros = 0;
                boolean exit = false;
                while (!exit) {
                    while (nextLesserThan8Bits(1) != 1) {
                        zeros++;
                    }
                    if (zeros > 5) {
                        // Exit code 
                        // Zeros before exit code 
                        zeros = zeros - 6;
                        if (!isWhite && (zeros > 0)) {
                            cce[currIndex++] = bitOffset;
                        }
                        // Zeros before the exit code 
                        bitOffset += zeros;
                        if (zeros > 0) {
                            // Some zeros have been written 
                            isWhite = true;
                        }
                        // Read in the bit which specifies the color of 
                        // the following run 
                        if (nextLesserThan8Bits(1) == 0) {
                            if (!isWhite) {
                                cce[currIndex++] = bitOffset;
                            }
                            isWhite = true;
                        } else {
                            if (isWhite) {
                                cce[currIndex++] = bitOffset;
                            }
                            isWhite = false;
                        }
                        exit = true;
                    }
                    if (zeros == 5) {
                        if (!isWhite) {
                            cce[currIndex++] = bitOffset;
                        }
                        bitOffset += zeros;
                        // Last thing written was white 
                        isWhite = true;
                    } else {
                        bitOffset += zeros;
                        cce[currIndex++] = bitOffset;
                        setToBlack(buffer, lineOffset, bitOffset, 1);
                        ++bitOffset;
                        // Last thing written was black 
                        isWhite = false;
                    }
                }
            } else {
                //micah_tessler@yahoo.com 
                //Microsoft TIFF renderers seem to treat unknown codes as line-breaks 
                //That is, they give up on the current line and move on to the next one 
                //set bitOffset to w to move on to the next scan line. 
                bitOffset = w;
                updatePointer(7 - bits);
            }
        }
        // Add the changing element beyond the current scanline for the 
        // other color too 
        //make sure that the index does not exceed the bounds of the array 
        if (currIndex < cce.length)
            cce[currIndex++] = bitOffset;
        // Number of changing elements in this scanline. 
        changingElemSize = currIndex;
        lineOffset += scanlineStride;
    }
}
