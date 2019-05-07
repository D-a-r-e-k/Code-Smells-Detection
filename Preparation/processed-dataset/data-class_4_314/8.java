// get the next available array  
protected Object[] getAvailableArray() {
    // if no array left in the pool, increase the pool size  
    if (fArrayPool.length == fPoolPos) {
        // increase size  
        fArrayPool = new Object[fPoolPos + INC_POOL_SIZE][];
        // initialize each *new* array  
        for (int i = fPoolPos; i < fArrayPool.length; i++) fArrayPool[i] = new Object[ATTIDX_COUNT];
    }
    // get the next available one  
    Object[] retArray = fArrayPool[fPoolPos];
    // clear it from the pool. this is for GC: if a caller forget to  
    // return the array, we want that array to be GCed.  
    fArrayPool[fPoolPos++] = null;
    // to make sure that one array is not returned twice, we use  
    // the last entry to indicate whether an array is already returned  
    // now set it to false.  
    System.arraycopy(fTempArray, 0, retArray, 0, ATTIDX_COUNT - 1);
    retArray[ATTIDX_ISRETURNED] = Boolean.FALSE;
    return retArray;
}
