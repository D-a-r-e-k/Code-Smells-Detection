public static void main(String[] args) {
    try {
        MemoryComparator set = new MemoryComparator(new TroveSetCreator(), new JavasoftSetCreator(), "Compare size of Set implementation: 1,000 Integer objects measured in bytes");
        set.compare();
        set = null;
        MemoryComparator list = new MemoryComparator(new TroveListCreator(), new JavasoftListCreator(), "Compare size of LinkedList implementation: 1,000 TLinkableAdaptor objects measured in bytes");
        list.compare();
        list = null;
        MemoryComparator list2 = new MemoryComparator(new TroveIntArrayListCreator(), new JavasoftIntegerArrayListCreator(), "Compare size of int/IntegerArrayList implementation: 1,000 ints measured in bytes");
        list2.compare();
        list2 = null;
        MemoryComparator map = new MemoryComparator(new TroveMapCreator(), new JavasoftMapCreator(), "Compare size of Map implementation: 1,000 Integer->Integer mappings measured in bytes");
        map.compare();
    } catch (Exception e) {
        e.printStackTrace();
    }
    System.exit(0);
}
