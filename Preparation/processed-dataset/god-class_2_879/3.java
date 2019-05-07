/** 
     * Routine called by Ant to set the formatter using an alias.  This 
     * is an attribute of the formatter element.  Example
     * <pre>
     *
     *      &lt;formatter type="plain"&gt;
     *
     * </pre>
     * @param t 'Type' of the formatter (brief, plain, summary, xml)
     */
public void setType(String t) {
    if (!types.containsKey(t)) {
        throw new BuildException("unknown formatter type " + t);
    }
    classname = (String) types.get(t);
    extension = (String) extensions.get(classname);
}
