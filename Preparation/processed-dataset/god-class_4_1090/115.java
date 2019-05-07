//}}}  
//{{{ getIndentRules() method  
private List<IndentRule> getIndentRules(int line) {
    String modeName = null;
    TokenMarker.LineContext ctx = lineMgr.getLineContext(line);
    if (ctx != null && ctx.rules != null)
        modeName = ctx.rules.getModeName();
    if (modeName == null)
        modeName = tokenMarker.getMainRuleSet().getModeName();
    return ModeProvider.instance.getMode(modeName).getIndentRules();
}
