public void setArguments(Expression[] nodes) {
    switch(funcType) {
        case FUNC_OVERLAY_CHAR:
            {
                Expression start = nodes[1];
                Expression length = nodes[2];
                nodes[1] = nodes[3];
                nodes[2] = start;
                nodes[3] = length;
                break;
            }
        case FUNC_EXTRACT:
            {
                Expression[] newNodes = new Expression[2];
                newNodes[0] = new ExpressionValue(ValuePool.getInt(extractSpec), Type.SQL_INTEGER);
                newNodes[1] = nodes[0];
                nodes = newNodes;
                break;
            }
        case FUNC_TRIM_CHAR:
            {
                Expression[] newNodes = new Expression[3];
                newNodes[0] = new ExpressionValue(ValuePool.getInt(extractSpec), Type.SQL_INTEGER);
                newNodes[1] = new ExpressionValue(" ", Type.SQL_CHAR);
                newNodes[2] = nodes[0];
                nodes = newNodes;
            }
    }
    super.setArguments(nodes);
}
