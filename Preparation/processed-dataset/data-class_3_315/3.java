public void setInputSource(InputSource inputSource) {
    if (inputSource != null) {
        setPublicId(inputSource.getPublicId());
        setSystemId(inputSource.getSystemId());
        setByteStream(inputSource.getByteStream());
        setCharacterStream(inputSource.getCharacterStream());
        setEncoding(inputSource.getEncoding());
    } else {
        setPublicId(null);
        setSystemId(null);
        setByteStream(null);
        setCharacterStream(null);
        setEncoding(null);
    }
    fInputSource = inputSource;
}
