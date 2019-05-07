private void initStorageContext(IXMLElement root) {
    identifiedElements = new HashMap<String, IXMLElement>();
    identifyElements(root);
    elementObjects = new HashMap<IXMLElement, Object>();
    viewportStack = new Stack<Viewport>();
    viewportStack.push(new Viewport());
    styleManager = new StyleManager();
}
