public String startRow() {
    StringBuffer sb = new StringBuffer();
    for (Iterator iterator = headerRows.iterator(); iterator.hasNext(); ) {
        StringBuffer stringBuffer = (StringBuffer) iterator.next();
        sb.append(stringBuffer);
    }
    return sb.toString();
}
