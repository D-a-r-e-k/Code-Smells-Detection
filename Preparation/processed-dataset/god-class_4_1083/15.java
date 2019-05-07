private static void moveRectangle(PdfDictionary dic2, PdfReader r, int pageImported, PdfName key, String name) {
    Rectangle m = r.getBoxSize(pageImported, name);
    if (m == null)
        dic2.remove(key);
    else
        dic2.put(key, new PdfRectangle(m));
}
