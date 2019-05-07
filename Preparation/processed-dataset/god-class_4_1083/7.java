/**
     * @param reader
     * @param openFile
     * @throws IOException
     */
public void registerReader(PdfReader reader, boolean openFile) throws IOException {
    if (readers2intrefs.containsKey(reader))
        return;
    readers2intrefs.put(reader, new IntHashtable());
    if (openFile) {
        RandomAccessFileOrArray raf = reader.getSafeFile();
        readers2file.put(reader, raf);
        raf.reOpen();
    }
}
