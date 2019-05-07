public final FromNode SqlTableList(FromNode node) throws ParseException {
    FromNode temp = null;
    TableIdentifier curTable = null;
    label_15: while (true) {
        jj_consume_token(COMMA);
        if (temp != null) {
            temp.setLeft(node);
            node = temp;
            temp = null;
        }
        node.setCondition(null);
        node.setType(FromNode.TYPE_INNER);
        curTable = SqlTableRef();
        node.setRight(curTable);
        temp = new FromNode();
        if (jj_2_8(2)) {
            ;
        } else {
            break label_15;
        }
    }
    {
        if (true)
            return node;
    }
    throw new Error("Missing return statement in function");
}
