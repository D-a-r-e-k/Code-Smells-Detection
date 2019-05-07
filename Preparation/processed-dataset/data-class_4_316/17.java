private String _getXmlVersion(Node node) {
    Document doc = (node.getNodeType() == Node.DOCUMENT_NODE) ? (Document) node : node.getOwnerDocument();
    if (doc != null && DocumentMethods.fgDocumentMethodsAvailable) {
        try {
            return (String) DocumentMethods.fgDocumentGetXmlVersionMethod.invoke(doc, (Object[]) null);
        } catch (VirtualMachineError vme) {
            throw vme;
        } catch (ThreadDeath td) {
            throw td;
        } catch (Throwable t) {
        }
    }
    return null;
}
