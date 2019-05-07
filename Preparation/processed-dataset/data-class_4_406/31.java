/**
	 * Add a hyperlink to this presentation
	 *
	 * @return 0-based index of the hyperlink
	 */
public int addHyperlink(Hyperlink link) {
    ExObjList lst = (ExObjList) _documentRecord.findFirstOfType(RecordTypes.ExObjList.typeID);
    if (lst == null) {
        lst = new ExObjList();
        _documentRecord.addChildAfter(lst, _documentRecord.getDocumentAtom());
    }
    ExObjListAtom objAtom = lst.getExObjListAtom();
    // increment the object ID seed 
    int objectId = (int) objAtom.getObjectIDSeed() + 1;
    objAtom.setObjectIDSeed(objectId);
    ExHyperlink ctrl = new ExHyperlink();
    ExHyperlinkAtom obj = ctrl.getExHyperlinkAtom();
    obj.setNumber(objectId);
    ctrl.setLinkURL(link.getAddress());
    ctrl.setLinkTitle(link.getTitle());
    lst.addChildAfter(ctrl, objAtom);
    link.setId(objectId);
    return objectId;
}
