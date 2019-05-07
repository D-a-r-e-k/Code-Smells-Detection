public void InsertFinalKind(int kind) {
    finalKinds[kind / 64] |= (1L << (kind % 64));
    finalKindCnt++;
}
