/**
   * Adds Jext internal key bindings.
   */
private static void addJextKeyBindings() {
    inputHandler.addKeyBinding("CA+UP", new ScrollUp());
    inputHandler.addKeyBinding("CA+PAGE_UP", new ScrollPageUp());
    inputHandler.addKeyBinding("CA+DOWN", new ScrollDown());
    inputHandler.addKeyBinding("CA+PAGE_DOWN", new ScrollPageDown());
    inputHandler.addKeyBinding("C+UP", new PrevLineIndent());
    inputHandler.addKeyBinding("C+DOWN", new NextLineIndent());
    inputHandler.addKeyBinding("ENTER", new IndentOnEnter());
    inputHandler.addKeyBinding("TAB", new IndentOnTab());
    inputHandler.addKeyBinding("S+TAB", new LeftIndent());
    inputHandler.addKeyBinding("C+INSERT", getAction("copy"));
    inputHandler.addKeyBinding("S+INSERT", getAction("paste"));
    inputHandler.addKeyBinding("CA+LEFT", new CsWord(CsWord.NO_ACTION, TextUtilities.BACKWARD));
    inputHandler.addKeyBinding("CA+RIGHT", new CsWord(CsWord.NO_ACTION, TextUtilities.FORWARD));
    inputHandler.addKeyBinding("CAS+LEFT", new CsWord(CsWord.SELECT, TextUtilities.BACKWARD));
    inputHandler.addKeyBinding("CAS+RIGHT", new CsWord(CsWord.SELECT, TextUtilities.FORWARD));
    inputHandler.addKeyBinding("CA+BACK_SPACE", new CsWord(CsWord.DELETE, TextUtilities.BACKWARD));
    inputHandler.addKeyBinding("CAS+BACK_SPACE", new CsWord(CsWord.DELETE, TextUtilities.FORWARD));
    if (Utilities.JDK_VERSION.charAt(2) >= '4') {
        inputHandler.addKeyBinding("C+PAGE_UP", new TabSwitcher(false));
        inputHandler.addKeyBinding("C+PAGE_DOWN", new TabSwitcher(true));
    }
}
