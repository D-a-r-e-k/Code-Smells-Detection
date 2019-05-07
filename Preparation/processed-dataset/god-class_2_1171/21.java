/**
   * Get a variable based on it's qualified name.
   * This is for external use only.
   *
   * @param xctxt The XPath context, which must be passed in order to
   * lazy evaluate variables.
   * 
   * @param qname The qualified name of the variable.
   *
   * @return The evaluated value of the variable.
   *
   * @throws javax.xml.transform.TransformerException
   */
public XObject getVariableOrParam(XPathContext xctxt, org.apache.xml.utils.QName qname) throws javax.xml.transform.TransformerException {
    org.apache.xml.utils.PrefixResolver prefixResolver = xctxt.getNamespaceContext();
    // Get the current ElemTemplateElement, which must be pushed in as the   
    // prefix resolver, and then walk backwards in document order, searching   
    // for an xsl:param element or xsl:variable element that matches our   
    // qname.  If we reach the top level, use the StylesheetRoot's composed  
    // list of top level variables and parameters.  
    if (prefixResolver instanceof org.apache.xalan.templates.ElemTemplateElement) {
        org.apache.xalan.templates.ElemVariable vvar;
        org.apache.xalan.templates.ElemTemplateElement prev = (org.apache.xalan.templates.ElemTemplateElement) prefixResolver;
        if (!(prev instanceof org.apache.xalan.templates.Stylesheet)) {
            while (!(prev.getParentNode() instanceof org.apache.xalan.templates.Stylesheet)) {
                org.apache.xalan.templates.ElemTemplateElement savedprev = prev;
                while (null != (prev = prev.getPreviousSiblingElem())) {
                    if (prev instanceof org.apache.xalan.templates.ElemVariable) {
                        vvar = (org.apache.xalan.templates.ElemVariable) prev;
                        if (vvar.getName().equals(qname))
                            return getLocalVariable(xctxt, vvar.getIndex());
                    }
                }
                prev = savedprev.getParentElem();
            }
        }
        vvar = prev.getStylesheetRoot().getVariableOrParamComposed(qname);
        if (null != vvar)
            return getGlobalVariable(xctxt, vvar.getIndex());
    }
    throw new javax.xml.transform.TransformerException(XSLMessages.createXPATHMessage(XPATHErrorResources.ER_VAR_NOT_RESOLVABLE, new Object[] { qname.toString() }));
}
