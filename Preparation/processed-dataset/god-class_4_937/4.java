public String toString() {
    StringBuffer sb = new StringBuffer("[PoolElement: ");
    sb.append(id);
    sb.append("/");
    sb.append(sCnt);
    sb.append("/");
    sb.append(dbp.url).append("(").append(dbp.table).append(")");
    sb.append("]");
    return sb.toString();
}
