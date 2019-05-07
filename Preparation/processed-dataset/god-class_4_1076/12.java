// Checks all the member variables are set properly. Returns true on success. Otherwise, returns 
// false. 
public boolean isValid() {
    return // First check if all version are not uninitialized. 
    mode != null && ecLevel != null && version != -1 && matrixWidth != -1 && maskPattern != -1 && numTotalBytes != -1 && numDataBytes != -1 && numECBytes != -1 && numRSBlocks != -1 && // Then check them in other ways.. 
    isValidMaskPattern(maskPattern) && numTotalBytes == numDataBytes + numECBytes && // ByteMatrix stuff. 
    matrix != null && matrixWidth == matrix.getWidth() && // See 7.3.1 of JISX0510:2004 (p.5). 
    matrix.getWidth() == matrix.getHeight();
}
