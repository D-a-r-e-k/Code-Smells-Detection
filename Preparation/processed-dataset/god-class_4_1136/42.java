// init()  
/** ensure element stack capacity */
private void ensureStackCapacity(int newElementDepth) {
    if (newElementDepth == fElementQNamePartsStack.length) {
        QName[] newStackOfQueue = new QName[newElementDepth * 2];
        System.arraycopy(this.fElementQNamePartsStack, 0, newStackOfQueue, 0, newElementDepth);
        fElementQNamePartsStack = newStackOfQueue;
        QName qname = fElementQNamePartsStack[newElementDepth];
        if (qname == null) {
            for (int i = newElementDepth; i < fElementQNamePartsStack.length; i++) {
                fElementQNamePartsStack[i] = new QName();
            }
        }
        int[] newStack = new int[newElementDepth * 2];
        System.arraycopy(fElementIndexStack, 0, newStack, 0, newElementDepth);
        fElementIndexStack = newStack;
        newStack = new int[newElementDepth * 2];
        System.arraycopy(fContentSpecTypeStack, 0, newStack, 0, newElementDepth);
        fContentSpecTypeStack = newStack;
    }
}
