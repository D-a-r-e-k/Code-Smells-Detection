// isSpace(int):  boolean  
public boolean characterData(String data, Augmentations augs) {
    characters(new XMLString(data.toCharArray(), 0, data.length()), augs);
    return true;
}
