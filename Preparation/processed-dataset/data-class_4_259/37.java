private void checkPRStreamLength(PRStream stream) throws IOException {
    int fileLength = tokens.length();
    int start = stream.getOffset();
    boolean calc = false;
    int streamLength = 0;
    PdfObject obj = getPdfObjectRelease(stream.get(PdfName.LENGTH));
    if (obj != null && obj.type() == PdfObject.NUMBER) {
        streamLength = ((PdfNumber) obj).intValue();
        if (streamLength + start > fileLength - 20)
            calc = true;
        else {
            tokens.seek(start + streamLength);
            String line = tokens.readString(20);
            if (!line.startsWith("\nendstream") && !line.startsWith("\r\nendstream") && !line.startsWith("\rendstream") && !line.startsWith("endstream"))
                calc = true;
        }
    } else
        calc = true;
    if (calc) {
        byte tline[] = new byte[16];
        tokens.seek(start);
        while (true) {
            int pos = tokens.getFilePointer();
            if (!tokens.readLineSegment(tline))
                break;
            if (equalsn(tline, endstream)) {
                streamLength = pos - start;
                break;
            }
            if (equalsn(tline, endobj)) {
                tokens.seek(pos - 16);
                String s = tokens.readString(16);
                int index = s.indexOf("endstream");
                if (index >= 0)
                    pos = pos - 16 + index;
                streamLength = pos - start;
                break;
            }
        }
    }
    stream.setLength(streamLength);
}
