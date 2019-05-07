/**
	 * Add a movie in this presentation
	 *
	 * @param path
	 *            the path or url to the movie
	 * @return 0-based index of the movie
	 */
public int addMovie(String path, int type) {
    ExObjList lst = (ExObjList) _documentRecord.findFirstOfType(RecordTypes.ExObjList.typeID);
    if (lst == null) {
        lst = new ExObjList();
        _documentRecord.addChildAfter(lst, _documentRecord.getDocumentAtom());
    }
    ExObjListAtom objAtom = lst.getExObjListAtom();
    // increment the object ID seed 
    int objectId = (int) objAtom.getObjectIDSeed() + 1;
    objAtom.setObjectIDSeed(objectId);
    ExMCIMovie mci;
    switch(type) {
        case MovieShape.MOVIE_MPEG:
            mci = new ExMCIMovie();
            break;
        case MovieShape.MOVIE_AVI:
            mci = new ExAviMovie();
            break;
        default:
            throw new IllegalArgumentException("Unsupported Movie: " + type);
    }
    lst.appendChildRecord(mci);
    ExVideoContainer exVideo = mci.getExVideo();
    exVideo.getExMediaAtom().setObjectId(objectId);
    exVideo.getExMediaAtom().setMask(0xE80000);
    exVideo.getPathAtom().setText(path);
    return objectId;
}
