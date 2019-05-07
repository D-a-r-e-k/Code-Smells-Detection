//}}}  
//{{{ getContextSensitiveProperty() method  
/**
	 * Some settings, like comment start and end strings, can
	 * vary between different parts of a buffer (HTML text and inline
	 * JavaScript, for example).
	 * @param offset The offset
	 * @param name The property name
	 * @since jEdit 4.0pre3
	 */
public String getContextSensitiveProperty(int offset, String name) {
    ParserRuleSet rules = getRuleSetAtOffset(offset);
    Object value = null;
    Map<String, String> rulesetProps = rules.getProperties();
    if (rulesetProps != null)
        value = rulesetProps.get(name);
    if (value == null)
        return null;
    else
        return String.valueOf(value);
}
