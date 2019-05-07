@Deprecated
public static String write3xInfo(Directory dir, SegmentInfo si, IOContext context) throws IOException {
    // NOTE: this is NOT how 3.x is really written... 
    String fileName = IndexFileNames.segmentFileName(si.name, "", Lucene3xSegmentInfoFormat.UPGRADED_SI_EXTENSION);
    si.addFile(fileName);
    //System.out.println("UPGRADE write " + fileName); 
    boolean success = false;
    IndexOutput output = dir.createOutput(fileName, context);
    try {
        // we are about to write this SI in 3.x format, dropping all codec information, etc. 
        // so it had better be a 3.x segment or you will get very confusing errors later. 
        assert si.getCodec() instanceof Lucene3xCodec : "broken test, trying to mix preflex with other codecs";
        CodecUtil.writeHeader(output, Lucene3xSegmentInfoFormat.UPGRADED_SI_CODEC_NAME, Lucene3xSegmentInfoFormat.UPGRADED_SI_VERSION_CURRENT);
        // Write the Lucene version that created this segment, since 3.1 
        output.writeString(si.getVersion());
        output.writeInt(si.getDocCount());
        output.writeStringStringMap(si.attributes());
        output.writeByte((byte) (si.getUseCompoundFile() ? SegmentInfo.YES : SegmentInfo.NO));
        output.writeStringStringMap(si.getDiagnostics());
        output.writeStringSet(si.files());
        output.close();
        success = true;
    } finally {
        if (!success) {
            IOUtils.closeWhileHandlingException(output);
            try {
                si.dir.deleteFile(fileName);
            } catch (Throwable t) {
            }
        }
    }
    return fileName;
}
