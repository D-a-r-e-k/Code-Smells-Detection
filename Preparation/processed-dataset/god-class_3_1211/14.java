/**
   * Returns a copy of this instance, also copying each
   * SegmentInfo.
   */
@Override
public SegmentInfos clone() {
    try {
        final SegmentInfos sis = (SegmentInfos) super.clone();
        // deep clone, first recreate all collections: 
        sis.segments = new ArrayList<SegmentInfoPerCommit>(size());
        for (final SegmentInfoPerCommit info : this) {
            assert info.info.getCodec() != null;
            // dont directly access segments, use add method!!! 
            sis.add(info.clone());
        }
        sis.userData = new HashMap<String, String>(userData);
        return sis;
    } catch (CloneNotSupportedException e) {
        throw new RuntimeException("should not happen", e);
    }
}
