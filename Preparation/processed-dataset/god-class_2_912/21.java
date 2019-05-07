public String finishRow() {
    String returnValue = "";
    if (containsTotaledColumns) {
        if (innermostGroup > 0 && deepestResetGroup != NO_RESET_GROUP) {
            StringBuffer out = new StringBuffer();
            // Starting with the deepest group, print the current total and reset. Do not reset unaffected groups. 
            for (int i = innermostGroup; i >= deepestResetGroup; i--) {
                Integer groupNumber = new Integer(i);
                GroupTotals totals = (GroupTotals) groupNumberToGroupTotal.get(groupNumber);
                if (totals == null) {
                    logger.warn("There is a gap in the defined groups - no group defined for " + groupNumber);
                    continue;
                }
                totals.printTotals(getListIndex(), out);
                totals.setStartRow(getListIndex() + 1);
            }
            returnValue = out.toString();
        } else {
            returnValue = null;
        }
        deepestResetGroup = NO_RESET_GROUP;
        headerRows.clear();
        if (isLastRow()) {
            returnValue = StringUtils.defaultString(returnValue);
            returnValue += totalAllRows();
        }
    }
    return returnValue;
}
