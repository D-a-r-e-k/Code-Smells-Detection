public void InsertValidKind(int kind) {
    validKinds[kind / 64] |= (1L << (kind % 64));
    validKindCnt++;
}
