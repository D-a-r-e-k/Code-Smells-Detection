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
@Override
public String getContextSensitiveProperty(int offset, String name) {
    Object value = super.getContextSensitiveProperty(offset, name);
    if (value == null) {
        ParserRuleSet rules = getRuleSetAtOffset(offset);
        value = jEdit.getMode(rules.getModeName()).getProperty(name);
        if (value == null)
            value = mode.getProperty(name);
    }
    if (value == null)
        return null;
    else
        return String.valueOf(value);
}
