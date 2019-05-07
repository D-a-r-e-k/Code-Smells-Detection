/** Attempts to set the coloring on the graphics based upon the content of the styles list
    * returns false if the point is not in the list.  Only runs in event thread.
    */
public boolean setColoring(int point, Graphics g) {
    synchronized (_stylesList) {
        for (Pair<Pair<Integer, Integer>, String> p : _stylesList) {
            Pair<Integer, Integer> loc = p.first();
            if (loc.first() <= point && loc.second() >= point) {
                if (p.second().equals(InteractionsDocument.ERROR_STYLE)) {
                    //DrJava.consoleErr().println("Error Style"); 
                    g.setColor(ERROR_COLOR);
                    g.setFont(g.getFont().deriveFont(Font.BOLD));
                } else if (p.second().equals(InteractionsDocument.DEBUGGER_STYLE)) {
                    //DrJava.consoleErr().println("Debugger Style"); 
                    g.setColor(DEBUGGER_COLOR);
                    g.setFont(g.getFont().deriveFont(Font.BOLD));
                } else if (p.second().equals(ConsoleDocument.SYSTEM_OUT_STYLE)) {
                    //DrJava.consoleErr().println("System.out Style"); 
                    g.setColor(INTERACTIONS_SYSTEM_OUT_COLOR);
                    g.setFont(MAIN_FONT);
                } else if (p.second().equals(ConsoleDocument.SYSTEM_IN_STYLE)) {
                    //DrJava.consoleErr().println("System.in Style"); 
                    g.setColor(INTERACTIONS_SYSTEM_IN_COLOR);
                    g.setFont(MAIN_FONT);
                } else if (p.second().equals(ConsoleDocument.SYSTEM_ERR_STYLE)) {
                    //DrJava.consoleErr().println("System.err Style"); 
                    g.setColor(INTERACTIONS_SYSTEM_ERR_COLOR);
                    g.setFont(MAIN_FONT);
                } else if (p.second().equals(InteractionsDocument.OBJECT_RETURN_STYLE)) {
                    g.setColor(NORMAL_COLOR);
                    g.setFont(MAIN_FONT);
                } else if (p.second().equals(InteractionsDocument.STRING_RETURN_STYLE)) {
                    g.setColor(DOUBLE_QUOTED_COLOR);
                    g.setFont(MAIN_FONT);
                } else if (p.second().equals(InteractionsDocument.NUMBER_RETURN_STYLE)) {
                    g.setColor(NUMBER_COLOR);
                    g.setFont(MAIN_FONT);
                } else if (p.second().equals(InteractionsDocument.CHARACTER_RETURN_STYLE)) {
                    g.setColor(SINGLE_QUOTED_COLOR);
                    g.setFont(MAIN_FONT);
                } else
                    return false;
                /* Normal text color */
                return true;
            }
        }
        return false;
    }
}
