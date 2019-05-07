/**
     * Attaches transformers to the class loader; classes extending
     * this class should call in the constructor after setting up the
     * lists.
     */
public final void setTransformers() {
    if (cxf != null) {
        for (int i = 0; i < cxf.length; i++) {
            qcl_.addClassXformer(cxf[i]);
        }
    }
    if (mxf != null) {
        for (int i = 0; i < mxf.length; i++) {
            qcl_.addMethodXformer(mxf[i]);
        }
    }
    if (gxf != null) {
        for (int i = 0; i < gxf.length; i++) {
            qcl_.addGraphXformer(gxf[i]);
        }
    }
}
