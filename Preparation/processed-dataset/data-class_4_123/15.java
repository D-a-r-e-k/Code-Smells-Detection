private void consumeSignatureData(final int methodID, final int[] basicBlockOffsets) {
    // note: by itself, this is not a very good checksum for a class def;  
    // however, it is fast to compute and since it will be used along with  
    // a class name it should be good at detecting structural changes that  
    // matter to us (method and basic block ordering/sizes)   
    final int temp1 = basicBlockOffsets.length;
    long temp2 = NBEAST * m_classSignature + (methodID + 1) * temp1;
    for (int i = 1; i < temp1; ++i) // skip the initial 0 offset  
    {
        temp2 = NBEAST * temp2 + basicBlockOffsets[i];
    }
    m_classSignature = temp2;
}
