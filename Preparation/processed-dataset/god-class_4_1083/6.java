@Override
RandomAccessFileOrArray getReaderFile(PdfReader reader) {
    if (readers2intrefs.containsKey(reader)) {
        RandomAccessFileOrArray raf = readers2file.get(reader);
        if (raf != null)
            return raf;
        return reader.getSafeFile();
    }
    if (currentPdfReaderInstance == null)
        return file;
    else
        return currentPdfReaderInstance.getReaderFile();
}
