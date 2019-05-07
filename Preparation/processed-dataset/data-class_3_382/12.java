private static boolean segmentWasUpgraded(Directory directory, SegmentInfo si) {
    // Check marker file: 
    String markerFileName = IndexFileNames.segmentFileName(si.name, "upgraded", Lucene3xSegmentInfoFormat.UPGRADED_SI_EXTENSION);
    IndexInput in = null;
    try {
        in = directory.openInput(markerFileName, IOContext.READONCE);
        if (CodecUtil.checkHeader(in, SEGMENT_INFO_UPGRADE_CODEC, SEGMENT_INFO_UPGRADE_VERSION, SEGMENT_INFO_UPGRADE_VERSION) == 0) {
            return true;
        }
    } catch (IOException ioe) {
    } finally {
        if (in != null) {
            IOUtils.closeWhileHandlingException(in);
        }
    }
    return false;
}
