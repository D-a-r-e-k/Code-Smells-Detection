// pushInputSource(XMLInputSource) 
private Reader getReader(final XMLInputSource inputSource) {
    Reader reader = inputSource.getCharacterStream();
    if (reader == null) {
        try {
            return new InputStreamReader(inputSource.getByteStream(), fJavaEncoding);
        } catch (final UnsupportedEncodingException e) {
        }
    }
    return reader;
}
