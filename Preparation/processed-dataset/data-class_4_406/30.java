/**
	 * Add a control in this presentation
	 *
	 * @param name
	 *            name of the control, e.g. "Shockwave Flash Object"
	 * @param progId
	 *            OLE Programmatic Identifier, e.g.
	 *            "ShockwaveFlash.ShockwaveFlash.9"
	 * @return 0-based index of the control
	 */
public int addControl(String name, String progId) {
    ExObjList lst = (ExObjList) _documentRecord.findFirstOfType(RecordTypes.ExObjList.typeID);
    if (lst == null) {
        lst = new ExObjList();
        _documentRecord.addChildAfter(lst, _documentRecord.getDocumentAtom());
    }
    ExObjListAtom objAtom = lst.getExObjListAtom();
    // increment the object ID seed 
    int objectId = (int) objAtom.getObjectIDSeed() + 1;
    objAtom.setObjectIDSeed(objectId);
    ExControl ctrl = new ExControl();
    ExOleObjAtom oleObj = ctrl.getExOleObjAtom();
    oleObj.setObjID(objectId);
    oleObj.setDrawAspect(ExOleObjAtom.DRAW_ASPECT_VISIBLE);
    oleObj.setType(ExOleObjAtom.TYPE_CONTROL);
    oleObj.setSubType(ExOleObjAtom.SUBTYPE_DEFAULT);
    ctrl.setProgId(progId);
    ctrl.setMenuName(name);
    ctrl.setClipboardName(name);
    lst.addChildAfter(ctrl, objAtom);
    return objectId;
}
