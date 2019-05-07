void resolveTypesForOverlaps() {
    if (nodes[LEFT].nodes[0].isUnresolvedParam()) {
        nodes[LEFT].nodes[0].dataType = nodes[RIGHT].nodes[0].dataType;
    }
    if (nodes[RIGHT].nodes[0].isUnresolvedParam()) {
        nodes[RIGHT].nodes[0].dataType = nodes[LEFT].nodes[0].dataType;
    }
    if (nodes[LEFT].nodes[0].dataType == null) {
        nodes[LEFT].nodes[0].dataType = nodes[RIGHT].nodes[0].dataType = Type.SQL_TIMESTAMP;
    }
    if (nodes[LEFT].nodes[1].isUnresolvedParam()) {
        nodes[LEFT].nodes[1].dataType = nodes[RIGHT].nodes[0].dataType;
    }
    if (nodes[RIGHT].nodes[1].isUnresolvedParam()) {
        nodes[RIGHT].nodes[1].dataType = nodes[LEFT].nodes[0].dataType;
    }
    if (!DTIType.isValidDatetimeRange(nodes[LEFT].nodes[0].dataType, nodes[LEFT].nodes[1].dataType) || !DTIType.isValidDatetimeRange(nodes[RIGHT].nodes[0].dataType, nodes[RIGHT].nodes[1].dataType)) {
        throw Error.error(ErrorCode.X_42563);
    }
    if (!DTIType.isValidDatetimeRange(nodes[LEFT].nodes[0].dataType, nodes[LEFT].nodes[1].dataType)) {
        throw Error.error(ErrorCode.X_42563);
    }
    nodes[LEFT].nodeDataTypes[0] = nodes[LEFT].nodes[0].dataType;
    nodes[LEFT].nodeDataTypes[1] = nodes[LEFT].nodes[1].dataType;
    nodes[RIGHT].nodeDataTypes[0] = nodes[RIGHT].nodes[0].dataType;
    nodes[RIGHT].nodeDataTypes[1] = nodes[RIGHT].nodes[1].dataType;
}
