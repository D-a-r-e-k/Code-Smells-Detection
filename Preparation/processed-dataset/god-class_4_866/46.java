// class LocationItem 
/**
     * To detect if 2 encoding are compatible, both must be able to read the meta tag specifying
     * the new encoding. This means that the byte representation of some minimal html markup must
     * be the same in both encodings
     */
boolean isEncodingCompatible(final String encoding1, final String encoding2) {
    final String reference = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=";
    try {
        final byte[] bytesEncoding1 = reference.getBytes(encoding1);
        final String referenceWithEncoding2 = new String(bytesEncoding1, encoding2);
        return reference.equals(referenceWithEncoding2);
    } catch (final UnsupportedEncodingException e) {
        return false;
    }
}
