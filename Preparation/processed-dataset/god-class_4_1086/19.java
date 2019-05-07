/**
     * @throws IOException
     */
@SuppressWarnings("unchecked")
private void readDecryptedDocObj() throws IOException {
    if (encrypted)
        return;
    PdfObject encDic = trailer.get(PdfName.ENCRYPT);
    if (encDic == null || encDic.toString().equals("null"))
        return;
    encryptionError = true;
    byte[] encryptionKey = null;
    encrypted = true;
    PdfDictionary enc = (PdfDictionary) getPdfObject(encDic);
    String s;
    PdfObject o;
    PdfArray documentIDs = trailer.getAsArray(PdfName.ID);
    byte documentID[] = null;
    if (documentIDs != null) {
        o = documentIDs.getPdfObject(0);
        strings.remove(o);
        s = o.toString();
        documentID = com.itextpdf.text.DocWriter.getISOBytes(s);
        if (documentIDs.size() > 1)
            strings.remove(documentIDs.getPdfObject(1));
    }
    // just in case we have a broken producer 
    if (documentID == null)
        documentID = new byte[0];
    byte uValue[] = null;
    byte oValue[] = null;
    int cryptoMode = PdfWriter.STANDARD_ENCRYPTION_40;
    int lengthValue = 0;
    PdfObject filter = getPdfObjectRelease(enc.get(PdfName.FILTER));
    if (filter.equals(PdfName.STANDARD)) {
        s = enc.get(PdfName.U).toString();
        strings.remove(enc.get(PdfName.U));
        uValue = com.itextpdf.text.DocWriter.getISOBytes(s);
        s = enc.get(PdfName.O).toString();
        strings.remove(enc.get(PdfName.O));
        oValue = com.itextpdf.text.DocWriter.getISOBytes(s);
        o = enc.get(PdfName.P);
        if (!o.isNumber())
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.p.value"));
        pValue = ((PdfNumber) o).intValue();
        o = enc.get(PdfName.R);
        if (!o.isNumber())
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.r.value"));
        rValue = ((PdfNumber) o).intValue();
        switch(rValue) {
            case 2:
                cryptoMode = PdfWriter.STANDARD_ENCRYPTION_40;
                break;
            case 3:
                o = enc.get(PdfName.LENGTH);
                if (!o.isNumber())
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value"));
                lengthValue = ((PdfNumber) o).intValue();
                if (lengthValue > 128 || lengthValue < 40 || lengthValue % 8 != 0)
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value"));
                cryptoMode = PdfWriter.STANDARD_ENCRYPTION_128;
                break;
            case 4:
                PdfDictionary dic = (PdfDictionary) enc.get(PdfName.CF);
                if (dic == null)
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("cf.not.found.encryption"));
                dic = (PdfDictionary) dic.get(PdfName.STDCF);
                if (dic == null)
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("stdcf.not.found.encryption"));
                if (PdfName.V2.equals(dic.get(PdfName.CFM)))
                    cryptoMode = PdfWriter.STANDARD_ENCRYPTION_128;
                else if (PdfName.AESV2.equals(dic.get(PdfName.CFM)))
                    cryptoMode = PdfWriter.ENCRYPTION_AES_128;
                else
                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("no.compatible.encryption.found"));
                PdfObject em = enc.get(PdfName.ENCRYPTMETADATA);
                if (em != null && em.toString().equals("false"))
                    cryptoMode |= PdfWriter.DO_NOT_ENCRYPT_METADATA;
                break;
            default:
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("unknown.encryption.type.r.eq.1", rValue));
        }
    } else if (filter.equals(PdfName.PUBSEC)) {
        boolean foundRecipient = false;
        byte[] envelopedData = null;
        PdfArray recipients = null;
        o = enc.get(PdfName.V);
        if (!o.isNumber())
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.v.value"));
        int vValue = ((PdfNumber) o).intValue();
        switch(vValue) {
            case 1:
                cryptoMode = PdfWriter.STANDARD_ENCRYPTION_40;
                lengthValue = 40;
                recipients = (PdfArray) enc.get(PdfName.RECIPIENTS);
                break;
            case 2:
                o = enc.get(PdfName.LENGTH);
                if (!o.isNumber())
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value"));
                lengthValue = ((PdfNumber) o).intValue();
                if (lengthValue > 128 || lengthValue < 40 || lengthValue % 8 != 0)
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value"));
                cryptoMode = PdfWriter.STANDARD_ENCRYPTION_128;
                recipients = (PdfArray) enc.get(PdfName.RECIPIENTS);
                break;
            case 4:
                PdfDictionary dic = (PdfDictionary) enc.get(PdfName.CF);
                if (dic == null)
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("cf.not.found.encryption"));
                dic = (PdfDictionary) dic.get(PdfName.DEFAULTCRYPTFILTER);
                if (dic == null)
                    throw new InvalidPdfException(MessageLocalization.getComposedMessage("defaultcryptfilter.not.found.encryption"));
                if (PdfName.V2.equals(dic.get(PdfName.CFM))) {
                    cryptoMode = PdfWriter.STANDARD_ENCRYPTION_128;
                    lengthValue = 128;
                } else if (PdfName.AESV2.equals(dic.get(PdfName.CFM))) {
                    cryptoMode = PdfWriter.ENCRYPTION_AES_128;
                    lengthValue = 128;
                } else
                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("no.compatible.encryption.found"));
                PdfObject em = dic.get(PdfName.ENCRYPTMETADATA);
                if (em != null && em.toString().equals("false"))
                    cryptoMode |= PdfWriter.DO_NOT_ENCRYPT_METADATA;
                recipients = (PdfArray) dic.get(PdfName.RECIPIENTS);
                break;
            default:
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("unknown.encryption.type.v.eq.1", rValue));
        }
        for (int i = 0; i < recipients.size(); i++) {
            PdfObject recipient = recipients.getPdfObject(i);
            strings.remove(recipient);
            CMSEnvelopedData data = null;
            try {
                data = new CMSEnvelopedData(recipient.getBytes());
                Iterator<RecipientInformation> recipientCertificatesIt = data.getRecipientInfos().getRecipients().iterator();
                while (recipientCertificatesIt.hasNext()) {
                    RecipientInformation recipientInfo = recipientCertificatesIt.next();
                    if (recipientInfo.getRID().match(certificate) && !foundRecipient) {
                        envelopedData = recipientInfo.getContent(certificateKey, certificateKeyProvider);
                        foundRecipient = true;
                    }
                }
            } catch (Exception f) {
                throw new ExceptionConverter(f);
            }
        }
        if (!foundRecipient || envelopedData == null) {
            throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("bad.certificate.and.key"));
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(envelopedData, 0, 20);
            for (int i = 0; i < recipients.size(); i++) {
                byte[] encodedRecipient = recipients.getPdfObject(i).getBytes();
                md.update(encodedRecipient);
            }
            if ((cryptoMode & PdfWriter.DO_NOT_ENCRYPT_METADATA) != 0)
                md.update(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 });
            encryptionKey = md.digest();
        } catch (Exception f) {
            throw new ExceptionConverter(f);
        }
    }
    decrypt = new PdfEncryption();
    decrypt.setCryptoMode(cryptoMode, lengthValue);
    if (filter.equals(PdfName.STANDARD)) {
        //check by owner password 
        decrypt.setupByOwnerPassword(documentID, password, uValue, oValue, pValue);
        if (!equalsArray(uValue, decrypt.userKey, rValue == 3 || rValue == 4 ? 16 : 32)) {
            //check by user password 
            decrypt.setupByUserPassword(documentID, password, oValue, pValue);
            if (!equalsArray(uValue, decrypt.userKey, rValue == 3 || rValue == 4 ? 16 : 32)) {
                throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password"));
            }
        } else
            ownerPasswordUsed = true;
    } else if (filter.equals(PdfName.PUBSEC)) {
        decrypt.setupByEncryptionKey(encryptionKey, lengthValue);
        ownerPasswordUsed = true;
    }
    for (int k = 0; k < strings.size(); ++k) {
        PdfString str = strings.get(k);
        str.decrypt(this);
    }
    if (encDic.isIndirect()) {
        cryptoRef = (PRIndirectReference) encDic;
        xrefObj.set(cryptoRef.getNumber(), null);
    }
    encryptionError = false;
}
