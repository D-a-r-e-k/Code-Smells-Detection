// end of nested class  
private void setClassName(final String fullName) {
    if ($assert.ENABLED)
        $assert.ASSERT(fullName != null && fullName.length() > 0, "null or empty input: fullName");
    final int lastSlash = fullName.lastIndexOf('/');
    if (lastSlash < 0) {
        m_classPackageName = "";
        m_className = fullName;
    } else {
        if ($assert.ENABLED)
            $assert.ASSERT(lastSlash < fullName.length() - 1, "malformed class name [" + fullName + "]");
        m_classPackageName = fullName.substring(0, lastSlash);
        m_className = fullName.substring(lastSlash + 1);
    }
}
