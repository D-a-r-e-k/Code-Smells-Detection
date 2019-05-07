/**
     *  Checks out that the first paragraph is correctly installed.
     *
     *  @param rootElement
     */
private void paragraphify(Element rootElement) {
    // 
    //  Add the paragraph tag to the first paragraph 
    // 
    List kids = rootElement.getContent();
    if (rootElement.getChild("p") != null) {
        ArrayList<Content> ls = new ArrayList<Content>();
        int idxOfFirstContent = 0;
        int count = 0;
        for (Iterator i = kids.iterator(); i.hasNext(); count++) {
            Content c = (Content) i.next();
            if (c instanceof Element) {
                String name = ((Element) c).getName();
                if (isBlockLevel(name))
                    break;
            }
            if (!(c instanceof ProcessingInstruction)) {
                ls.add(c);
                if (idxOfFirstContent == 0)
                    idxOfFirstContent = count;
            }
        }
        // 
        //  If there were any elements, then add a new <p> (unless it would 
        //  be an empty one) 
        // 
        if (ls.size() > 0) {
            Element newel = new Element("p");
            for (Iterator i = ls.iterator(); i.hasNext(); ) {
                Content c = (Content) i.next();
                c.detach();
                newel.addContent(c);
            }
            // 
            // Make sure there are no empty <p/> tags added. 
            // 
            if (newel.getTextTrim().length() > 0 || !newel.getChildren().isEmpty())
                rootElement.addContent(idxOfFirstContent, newel);
        }
    }
}
