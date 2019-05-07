// Return debug String. 
public String toString() {
    StringBuffer result = new StringBuffer(200);
    result.append("<<\n");
    result.append(" mode: ");
    result.append(mode);
    result.append("\n ecLevel: ");
    result.append(ecLevel);
    result.append("\n version: ");
    result.append(version);
    result.append("\n matrixWidth: ");
    result.append(matrixWidth);
    result.append("\n maskPattern: ");
    result.append(maskPattern);
    result.append("\n numTotalBytes: ");
    result.append(numTotalBytes);
    result.append("\n numDataBytes: ");
    result.append(numDataBytes);
    result.append("\n numECBytes: ");
    result.append(numECBytes);
    result.append("\n numRSBlocks: ");
    result.append(numRSBlocks);
    if (matrix == null) {
        result.append("\n matrix: null\n");
    } else {
        result.append("\n matrix:\n");
        result.append(matrix.toString());
    }
    result.append(">>\n");
    return result.toString();
}
