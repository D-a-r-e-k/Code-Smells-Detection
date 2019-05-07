/**
	 * Adds a picture to this presentation and returns the associated index.
	 *
	 * @param data
	 *            picture data
	 * @param format
	 *            the format of the picture. One of constans defined in the
	 *            <code>Picture</code> class.
	 * @return the index to this picture (1 based).
	 */
public int addPicture(byte[] data, int format) throws IOException {
    byte[] uid = PictureData.getChecksum(data);
    EscherContainerRecord bstore;
    EscherContainerRecord dggContainer = _documentRecord.getPPDrawingGroup().getDggContainer();
    bstore = (EscherContainerRecord) Shape.getEscherChild(dggContainer, EscherContainerRecord.BSTORE_CONTAINER);
    if (bstore == null) {
        bstore = new EscherContainerRecord();
        bstore.setRecordId(EscherContainerRecord.BSTORE_CONTAINER);
        dggContainer.addChildBefore(bstore, EscherOptRecord.RECORD_ID);
    } else {
        Iterator<EscherRecord> iter = bstore.getChildIterator();
        for (int i = 0; iter.hasNext(); i++) {
            EscherBSERecord bse = (EscherBSERecord) iter.next();
            if (Arrays.equals(bse.getUid(), uid)) {
                return i + 1;
            }
        }
    }
    PictureData pict = PictureData.create(format);
    pict.setData(data);
    int offset = _hslfSlideShow.addPicture(pict);
    EscherBSERecord bse = new EscherBSERecord();
    bse.setRecordId(EscherBSERecord.RECORD_ID);
    bse.setOptions((short) (0x0002 | (format << 4)));
    bse.setSize(pict.getRawData().length + 8);
    bse.setUid(uid);
    bse.setBlipTypeMacOS((byte) format);
    bse.setBlipTypeWin32((byte) format);
    if (format == Picture.EMF)
        bse.setBlipTypeMacOS((byte) Picture.PICT);
    else if (format == Picture.WMF)
        bse.setBlipTypeMacOS((byte) Picture.PICT);
    else if (format == Picture.PICT)
        bse.setBlipTypeWin32((byte) Picture.WMF);
    bse.setRef(0);
    bse.setOffset(offset);
    bse.setRemainingData(new byte[0]);
    bstore.addChildRecord(bse);
    int count = bstore.getChildRecords().size();
    bstore.setOptions((short) ((count << 4) | 0xF));
    return count;
}
