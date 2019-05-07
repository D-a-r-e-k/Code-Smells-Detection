// Return the value of the module (cell) pointed by "x" and "y" in the matrix of the QR Code. They 
// call cells in the matrix "modules". 1 represents a black cell, and 0 represents a white cell. 
public int at(int x, int y) {
    // The value must be zero or one. 
    int value = matrix.get(x, y);
    if (!(value == 0 || value == 1)) {
        // this is really like an assert... not sure what better exception to use? 
        throw new RuntimeException("Bad value");
    }
    return value;
}
