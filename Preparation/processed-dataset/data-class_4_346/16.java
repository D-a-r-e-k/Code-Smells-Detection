private void setOutputStreamInternal(OutputStream output, boolean setByUser) {
    m_outputStream = output;
    String encoding = getOutputProperty(OutputKeys.ENCODING);
    if (Encodings.DEFAULT_MIME_ENCODING.equalsIgnoreCase(encoding)) {
        // We wrap the OutputStream with a writer, but  
        // not one set by the user  
        setWriterInternal(new WriterToUTF8Buffered(output), false);
    } else if ("WINDOWS-1250".equals(encoding) || "US-ASCII".equals(encoding) || "ASCII".equals(encoding)) {
        setWriterInternal(new WriterToASCI(output), false);
    } else if (encoding != null) {
        Writer osw = null;
        try {
            osw = Encodings.getWriter(output, encoding);
        } catch (UnsupportedEncodingException uee) {
            osw = null;
        }
        if (osw == null) {
            System.out.println("Warning: encoding \"" + encoding + "\" not supported" + ", using " + Encodings.DEFAULT_MIME_ENCODING);
            encoding = Encodings.DEFAULT_MIME_ENCODING;
            setEncoding(encoding);
            try {
                osw = Encodings.getWriter(output, encoding);
            } catch (UnsupportedEncodingException e) {
                // We can't really get here, UTF-8 is always supported  
                // This try-catch exists to make the compiler happy  
                e.printStackTrace();
            }
        }
        setWriterInternal(osw, false);
    } else {
        // don't have any encoding, but we have an OutputStream  
        Writer osw = new OutputStreamWriter(output);
        setWriterInternal(osw, false);
    }
}
