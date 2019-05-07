void rollbackSegmentInfos(List<SegmentInfoPerCommit> infos) {
    this.clear();
    this.addAll(infos);
}
