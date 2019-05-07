/** Gets the name of first class/interface/enum declared in file among the definitions anchored at:
    * @param indexOfClass  index in this of a top-level occurrence of class 
    * @param indexOfInterface  index in this of a top-level occurrence of interface
    * @param indexOfEnum index in this of a top-level occurrence of enum
    */
private String getFirstClassName(int indexOfClass, int indexOfInterface, int indexOfEnum) throws ClassNameNotFoundException {
    try {
        if ((indexOfClass == -1) && (indexOfInterface == -1) && (indexOfEnum == -1))
            throw ClassNameNotFoundException.DEFAULT;
        // should we convert this to a sorted queue or something like that? 
        // should we have to extend this past three keywords, it will get rather hard to maintain 
        if ((indexOfEnum == -1) || ((indexOfClass != -1) && (indexOfClass < indexOfEnum)) || ((indexOfInterface != -1) && (indexOfInterface < indexOfEnum))) {
            // either "enum" not found, or "enum" found after "class" or "interface" 
            // "enum" is irrelevant 
            // we know that at least one of indexOfClass and indexOfInterface is != -1 
            if ((indexOfInterface == -1) || ((indexOfClass != -1) && (indexOfClass < indexOfInterface))) {
                // either "interface" not found, or "interface" found after "class" 
                return getNextIdentifier(indexOfClass + "class".length());
            } else {
                // "interface" found, and found before "class" 
                return getNextIdentifier(indexOfInterface + "interface".length());
            }
        } else {
            // "enum" found, and found before "class" and "interface" 
            return getNextIdentifier(indexOfEnum + "enum".length());
        }
    } catch (IllegalStateException ise) {
        throw ClassNameNotFoundException.DEFAULT;
    }
}
