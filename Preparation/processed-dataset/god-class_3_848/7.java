/* Added by Rick Mugridge, from FitNesse */
protected void getArgsForTable(Parse table) {
    ArrayList argumentList = new ArrayList();
    Parse parameters = table.parts.parts.more;
    for (; parameters != null; parameters = parameters.more) argumentList.add(parameters.text());
    args = (String[]) argumentList.toArray(new String[0]);
}
