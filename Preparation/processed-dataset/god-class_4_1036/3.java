private static List getElements(Element element) {
    ArrayList list = new ArrayList();
    try {
        if (element.getName().equals("outline") || element.getName().equals("body")) // OPML 
        {
            List outlines = element.elements("outline");
            if (outlines.size() == 1) {
                element = (Element) outlines.get(0);
            }
            for (Iterator i = element.elementIterator("outline"); i.hasNext(); ) {
                Element outline = (Element) i.next();
                list.add(outline);
            }
        }
    } catch (Exception ex) {
        Tools.logException(Podcasting.class, ex, "Could not determine data");
    }
    if (list.size() > 0) {
        Element[] elementArray = (Element[]) list.toArray(new Element[0]);
        /*
			 * Arrays.sort(elementArray, new Comparator() { public int
			 * compare(Object o1, Object o2) { String element1 =
			 * Tools.getAttribute((Element) o1, "text"); String element2 =
			 * Tools.getAttribute((Element) o2, "text"); if (element1 != null &&
			 * element2 != null) return element1.compareTo(element2); else
			 * return 0; } });
			 */
        list.clear();
        for (int i = 0; i < elementArray.length; i++) list.add(elementArray[i]);
    }
    return list;
}
