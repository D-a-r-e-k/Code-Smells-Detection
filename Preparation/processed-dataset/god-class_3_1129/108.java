/**
     * Read response from the input stream, converting to MD5 digest if the useMD5 property is set.
     *
     * For the MD5 case, the result byte count is set to the size of the original response.
     * 
     * Closes the inputStream 
     * 
     * @param sampleResult
     * @param in input stream
     * @param length expected input length or zero
     * @return the response or the MD5 of the response
     * @throws IOException
     */
public byte[] readResponse(SampleResult sampleResult, InputStream in, int length) throws IOException {
    try {
        byte[] readBuffer = new byte[8192];
        // 8kB is the (max) size to have the latency ('the first packet') 
        int bufferSize = 32;
        // Enough for MD5 
        MessageDigest md = null;
        boolean asMD5 = useMD5();
        if (asMD5) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                log.error("Should not happen - could not find MD5 digest", e);
                asMD5 = false;
            }
        } else {
            if (length <= 0) {
                // may also happen if long value > int.max 
                bufferSize = 4 * 1024;
            } else {
                bufferSize = length;
            }
        }
        ByteArrayOutputStream w = new ByteArrayOutputStream(bufferSize);
        int bytesRead = 0;
        int totalBytes = 0;
        boolean first = true;
        while ((bytesRead = in.read(readBuffer)) > -1) {
            if (first) {
                sampleResult.latencyEnd();
                first = false;
            }
            if (asMD5 && md != null) {
                md.update(readBuffer, 0, bytesRead);
                totalBytes += bytesRead;
            } else {
                w.write(readBuffer, 0, bytesRead);
            }
        }
        if (first) {
            // Bug 46838 - if there was no data, still need to set latency 
            sampleResult.latencyEnd();
        }
        in.close();
        w.flush();
        if (asMD5 && md != null) {
            byte[] md5Result = md.digest();
            w.write(JOrphanUtils.baToHexBytes(md5Result));
            sampleResult.setBytes(totalBytes);
        }
        w.close();
        return w.toByteArray();
    } finally {
        IOUtils.closeQuietly(in);
    }
}
