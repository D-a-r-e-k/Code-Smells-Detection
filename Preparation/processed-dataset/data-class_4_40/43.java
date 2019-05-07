// synthesizedAugs():Augmentations 
// 
// Protected static methods 
// 
/** Modifies the given name based on the specified mode. */
protected static final String modifyName(String name, short mode) {
    switch(mode) {
        case NAMES_UPPERCASE:
            return name.toUpperCase();
        case NAMES_LOWERCASE:
            return name.toLowerCase();
    }
    return name;
}
