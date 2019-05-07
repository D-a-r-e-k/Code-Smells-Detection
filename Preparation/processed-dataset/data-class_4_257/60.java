void setBoxSize(String boxName, Rectangle size) {
    if (size == null)
        boxSize.remove(boxName);
    else
        boxSize.put(boxName, new PdfRectangle(size));
}
