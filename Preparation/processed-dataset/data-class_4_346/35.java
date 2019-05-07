private void writeOutCleanChars(final char[] chars, int i, int lastProcessed) throws IOException {
    int startClean;
    startClean = lastProcessed + 1;
    if (startClean < i) {
        int lengthClean = i - startClean;
        m_writer.write(chars, startClean, lengthClean);
    }
}
