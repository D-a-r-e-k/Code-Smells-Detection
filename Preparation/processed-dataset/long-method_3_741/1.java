/**
     * generic method for adding or removing key / values in multi-value
     * maps
     */
protected Object addOrRemoveMultiVal(long longKey, long longValue, Object objectKey, Object objectValue, boolean removeKey, boolean removeValue) {
    int hash = (int) longKey;
    if (isObjectKey) {
        if (objectKey == null) {
            return null;
        }
        hash = objectKey.hashCode();
    }
    int index = hashIndex.getHashIndex(hash);
    int lookup = hashIndex.hashTable[index];
    int lastLookup = -1;
    Object returnValue = null;
    boolean multiValue = false;
    for (; lookup >= 0; lastLookup = lookup, lookup = hashIndex.getNextLookup(lookup)) {
        if (isObjectKey) {
            if (objectKeyTable[lookup].equals(objectKey)) {
                if (removeKey) {
                    while (true) {
                        objectKeyTable[lookup] = null;
                        returnValue = objectValueTable[lookup];
                        objectValueTable[lookup] = null;
                        hashIndex.unlinkNode(index, lastLookup, lookup);
                        multiValueTable[lookup] = false;
                        lookup = hashIndex.hashTable[index];
                        if (lookup < 0 || !objectKeyTable[lookup].equals(objectKey)) {
                            return returnValue;
                        }
                    }
                } else {
                    if (objectValueTable[lookup].equals(objectValue)) {
                        if (removeValue) {
                            objectKeyTable[lookup] = null;
                            returnValue = objectValueTable[lookup];
                            objectValueTable[lookup] = null;
                            hashIndex.unlinkNode(index, lastLookup, lookup);
                            multiValueTable[lookup] = false;
                            lookup = lastLookup;
                            return returnValue;
                        } else {
                            return objectValueTable[lookup];
                        }
                    }
                }
                multiValue = true;
            }
        } else if (isIntKey) {
            if (longKey == intKeyTable[lookup]) {
                if (removeKey) {
                    while (true) {
                        if (longKey == 0) {
                            hasZeroKey = false;
                            zeroKeyIndex = -1;
                        }
                        intKeyTable[lookup] = 0;
                        intValueTable[lookup] = 0;
                        hashIndex.unlinkNode(index, lastLookup, lookup);
                        multiValueTable[lookup] = false;
                        lookup = hashIndex.hashTable[index];
                        if (lookup < 0 || longKey != intKeyTable[lookup]) {
                            return null;
                        }
                    }
                } else {
                    if (intValueTable[lookup] == longValue) {
                        return null;
                    }
                }
                multiValue = true;
            }
        } else if (isLongKey) {
            if (longKey == longKeyTable[lookup]) {
                if (removeKey) {
                    while (true) {
                        if (longKey == 0) {
                            hasZeroKey = false;
                            zeroKeyIndex = -1;
                        }
                        longKeyTable[lookup] = 0;
                        longValueTable[lookup] = 0;
                        hashIndex.unlinkNode(index, lastLookup, lookup);
                        multiValueTable[lookup] = false;
                        lookup = hashIndex.hashTable[index];
                        if (lookup < 0 || longKey != longKeyTable[lookup]) {
                            return null;
                        }
                    }
                } else {
                    if (intValueTable[lookup] == longValue) {
                        return null;
                    }
                }
                multiValue = true;
            }
        }
    }
    if (removeKey || removeValue) {
        return returnValue;
    }
    if (hashIndex.elementCount >= threshold) {
        // should throw maybe, if reset returns false? 
        if (reset()) {
            return addOrRemoveMultiVal(longKey, longValue, objectKey, objectValue, removeKey, removeValue);
        } else {
            return null;
        }
    }
    lookup = hashIndex.linkNode(index, lastLookup);
    // type dependent block 
    if (isObjectKey) {
        objectKeyTable[lookup] = objectKey;
    } else if (isIntKey) {
        intKeyTable[lookup] = (int) longKey;
        if (longKey == 0) {
            hasZeroKey = true;
            zeroKeyIndex = lookup;
        }
    } else if (isLongKey) {
        longKeyTable[lookup] = longKey;
        if (longKey == 0) {
            hasZeroKey = true;
            zeroKeyIndex = lookup;
        }
    }
    if (isObjectValue) {
        objectValueTable[lookup] = objectValue;
    } else if (isIntValue) {
        intValueTable[lookup] = (int) longValue;
    } else if (isLongValue) {
        longValueTable[lookup] = longValue;
    }
    if (multiValue) {
        multiValueTable[lookup] = true;
    }
    // 
    if (accessTable != null) {
        accessTable[lookup] = ++accessCount;
    }
    return returnValue;
}
