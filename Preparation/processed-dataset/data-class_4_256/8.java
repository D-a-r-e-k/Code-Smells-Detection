/**
     * @param reader
     */
public void unRegisterReader(PdfReader reader) {
    if (!readers2intrefs.containsKey(reader))
        return;
    readers2intrefs.remove(reader);
    RandomAccessFileOrArray raf = readers2file.get(reader);
    if (raf == null)
        return;
    readers2file.remove(reader);
    try {
        raf.close();
    } catch (Exception e) {
    }
}
