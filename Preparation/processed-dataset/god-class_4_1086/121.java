PdfIndirectReference getCryptoRef() {
    if (cryptoRef == null)
        return null;
    return new PdfIndirectReference(0, cryptoRef.getNumber(), cryptoRef.getGeneration());
}
