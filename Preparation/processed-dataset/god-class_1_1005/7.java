@SuppressWarnings("unchecked")
public List list() {
    ArrayList list = new ArrayList();
    list.add(new Person("Lars"));
    list.add(new Person(null));
    return list;
}
