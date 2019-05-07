void checkLobUsage() {
    if (!isDistinctSelect && !isGrouped) {
        return;
    }
    for (int i = 0; i < indexStartHaving; i++) {
        if (exprColumns[i].dataType.isLobType()) {
            throw Error.error(ErrorCode.X_42534);
        }
    }
}
