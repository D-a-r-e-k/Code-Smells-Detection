/**
	 * @see TargetManager#getAbstractTargetDTOs(String, int)
	 */
public Pagination getSubGroupParentDTOs(String name, int pageNumber, int pageSize) {
    List<String> types = new ArrayList<String>();
    Iterator<String> it = subGroupParentTypesList.iterator();
    while (it.hasNext()) {
        types.add(it.next());
    }
    return targetDao.getSubGroupParentDTOs(name, types, pageNumber, pageSize);
}
