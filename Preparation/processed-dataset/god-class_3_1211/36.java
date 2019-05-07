List<SegmentInfoPerCommit> createBackupSegmentInfos() {
    final List<SegmentInfoPerCommit> list = new ArrayList<SegmentInfoPerCommit>(size());
    for (final SegmentInfoPerCommit info : this) {
        assert info.info.getCodec() != null;
        list.add(info.clone());
    }
    return list;
}
