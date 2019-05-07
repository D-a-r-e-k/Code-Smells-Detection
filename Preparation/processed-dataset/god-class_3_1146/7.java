// endContentModel(int[]):  boolean  
// Killed off whatCanGoHere; we may need it for DOM canInsert(...) etc.,  
// but we can put it back later.  
//  
// Private methods  
//  
/**
     * Builds the internal DFA transition table from the given syntax tree.
     *
     * @param syntaxTree The syntax tree.
     *
     * @exception RuntimeException Thrown if DFA cannot be built.
     */
private void buildDFA(CMNode syntaxTree) {
    //  
    //  The first step we need to take is to rewrite the content model  
    //  using our CMNode objects, and in the process get rid of any  
    //  repetition short cuts, converting them into '*' style repetitions  
    //  or getting rid of repetitions altogether.  
    //  
    //  The conversions done are:  
    //  
    //  x+ -> (x|x*)  
    //  x? -> (x|epsilon)  
    //  
    //  This is a relatively complex scenario. What is happening is that  
    //  we create a top level binary node of which the special EOC value  
    //  is set as the right side node. The the left side is set to the  
    //  rewritten syntax tree. The source is the original content model  
    //  info from the decl pool. The rewrite is done by buildSyntaxTree()  
    //  which recurses the decl pool's content of the element and builds  
    //  a new tree in the process.  
    //  
    //  Note that, during this operation, we set each non-epsilon leaf  
    //  node's DFA state position and count the number of such leafs, which  
    //  is left in the fLeafCount member.  
    //  
    //  The nodeTmp object is passed in just as a temp node to use during  
    //  the recursion. Otherwise, we'd have to create a new node on every  
    //  level of recursion, which would be piggy in Java (as is everything  
    //  for that matter.)  
    //  
    /* MODIFIED (Jan, 2001)
         *
         * Use following rules.
         *   nullable(x+) := nullable(x), first(x+) := first(x),  last(x+) := last(x)
         *   nullable(x?) := true, first(x?) := first(x),  last(x?) := last(x)
         *
         * The same computation of follow as x* is applied to x+
         *
         * The modification drastically reduces computation time of
         * "(a, (b, a+, (c, (b, a+)+, a+, (d,  (c, (b, a+)+, a+)+, (b, a+)+, a+)+)+)+)+"
         */
    //  
    //  And handle specially the EOC node, which also must be numbered  
    //  and counted as a non-epsilon leaf node. It could not be handled  
    //  in the above tree build because it was created before all that  
    //  started. We save the EOC position since its used during the DFA  
    //  building loop.  
    //  
    int EOCPos = fLeafCount;
    XSCMLeaf nodeEOC = new XSCMLeaf(XSParticleDecl.PARTICLE_ELEMENT, null, -1, fLeafCount++);
    fHeadNode = new XSCMBinOp(XSModelGroupImpl.MODELGROUP_SEQUENCE, syntaxTree, nodeEOC);
    //  
    //  Ok, so now we have to iterate the new tree and do a little more  
    //  work now that we know the leaf count. One thing we need to do is  
    //  to calculate the first and last position sets of each node. This  
    //  is cached away in each of the nodes.  
    //  
    //  Along the way we also set the leaf count in each node as the  
    //  maximum state count. They must know this in order to create their  
    //  first/last pos sets.  
    //  
    //  We also need to build an array of references to the non-epsilon  
    //  leaf nodes. Since we iterate it in the same way as before, this  
    //  will put them in the array according to their position values.  
    //  
    fLeafList = new XSCMLeaf[fLeafCount];
    fLeafListType = new int[fLeafCount];
    postTreeBuildInit(fHeadNode);
    //  
    //  And, moving onward... We now need to build the follow position  
    //  sets for all the nodes. So we allocate an array of state sets,  
    //  one for each leaf node (i.e. each DFA position.)  
    //  
    fFollowList = new CMStateSet[fLeafCount];
    for (int index = 0; index < fLeafCount; index++) fFollowList[index] = new CMStateSet(fLeafCount);
    calcFollowList(fHeadNode);
    //  
    //  And finally the big push... Now we build the DFA using all the  
    //  states and the tree we've built up. First we set up the various  
    //  data structures we are going to use while we do this.  
    //  
    //  First of all we need an array of unique element names in our  
    //  content model. For each transition table entry, we need a set of  
    //  contiguous indices to represent the transitions for a particular  
    //  input element. So we need to a zero based range of indexes that  
    //  map to element types. This element map provides that mapping.  
    //  
    fElemMap = new Object[fLeafCount];
    fElemMapType = new int[fLeafCount];
    fElemMapId = new int[fLeafCount];
    fElemMapSize = 0;
    Occurence[] elemOccurenceMap = null;
    for (int outIndex = 0; outIndex < fLeafCount; outIndex++) {
        // optimization from Henry Zongaro:  
        //fElemMap[outIndex] = new Object ();  
        fElemMap[outIndex] = null;
        int inIndex = 0;
        final int id = fLeafList[outIndex].getParticleId();
        for (; inIndex < fElemMapSize; inIndex++) {
            if (id == fElemMapId[inIndex])
                break;
        }
        // If it was not in the list, then add it, if not the EOC node  
        if (inIndex == fElemMapSize) {
            XSCMLeaf leaf = fLeafList[outIndex];
            fElemMap[fElemMapSize] = leaf.getLeaf();
            if (leaf instanceof XSCMRepeatingLeaf) {
                if (elemOccurenceMap == null) {
                    elemOccurenceMap = new Occurence[fLeafCount];
                }
                elemOccurenceMap[fElemMapSize] = new Occurence((XSCMRepeatingLeaf) leaf, fElemMapSize);
            }
            fElemMapType[fElemMapSize] = fLeafListType[outIndex];
            fElemMapId[fElemMapSize] = id;
            fElemMapSize++;
        }
    }
    // the last entry in the element map must be the EOC element.  
    // remove it from the map.  
    if (DEBUG) {
        if (fElemMapId[fElemMapSize - 1] != -1)
            System.err.println("interal error in DFA: last element is not EOC.");
    }
    fElemMapSize--;
    /***
         * Optimization(Jan, 2001); We sort fLeafList according to
         * elemIndex which is *uniquely* associated to each leaf.
         * We are *assuming* that each element appears in at least one leaf.
         **/
    int[] fLeafSorter = new int[fLeafCount + fElemMapSize];
    int fSortCount = 0;
    for (int elemIndex = 0; elemIndex < fElemMapSize; elemIndex++) {
        final int id = fElemMapId[elemIndex];
        for (int leafIndex = 0; leafIndex < fLeafCount; leafIndex++) {
            if (id == fLeafList[leafIndex].getParticleId())
                fLeafSorter[fSortCount++] = leafIndex;
        }
        fLeafSorter[fSortCount++] = -1;
    }
    /* Optimization(Jan, 2001) */
    //  
    //  Next lets create some arrays, some that hold transient  
    //  information during the DFA build and some that are permament.  
    //  These are kind of sticky since we cannot know how big they will  
    //  get, but we don't want to use any Java collections because of  
    //  performance.  
    //  
    //  Basically they will probably be about fLeafCount*2 on average,  
    //  but can be as large as 2^(fLeafCount*2), worst case. So we start  
    //  with fLeafCount*4 as a middle ground. This will be very unlikely  
    //  to ever have to expand, though it if does, the overhead will be  
    //  somewhat ugly.  
    //  
    int curArraySize = fLeafCount * 4;
    CMStateSet[] statesToDo = new CMStateSet[curArraySize];
    fFinalStateFlags = new boolean[curArraySize];
    fTransTable = new int[curArraySize][];
    //  
    //  Ok we start with the initial set as the first pos set of the  
    //  head node (which is the seq node that holds the content model  
    //  and the EOC node.)  
    //  
    CMStateSet setT = fHeadNode.firstPos();
    //  
    //  Init our two state flags. Basically the unmarked state counter  
    //  is always chasing the current state counter. When it catches up,  
    //  that means we made a pass through that did not add any new states  
    //  to the lists, at which time we are done. We could have used a  
    //  expanding array of flags which we used to mark off states as we  
    //  complete them, but this is easier though less readable maybe.  
    //  
    int unmarkedState = 0;
    int curState = 0;
    //  
    //  Init the first transition table entry, and put the initial state  
    //  into the states to do list, then bump the current state.  
    //  
    fTransTable[curState] = makeDefStateList();
    statesToDo[curState] = setT;
    curState++;
    /* Optimization(Jan, 2001); This is faster for
         * a large content model such as, "(t001+|t002+|.... |t500+)".
         */
    HashMap stateTable = new HashMap();
    /* Optimization(Jan, 2001) */
    //  
    //  Ok, almost done with the algorithm... We now enter the  
    //  loop where we go until the states done counter catches up with  
    //  the states to do counter.  
    //  
    while (unmarkedState < curState) {
        //  
        //  Get the first unmarked state out of the list of states to do.  
        //  And get the associated transition table entry.  
        //  
        setT = statesToDo[unmarkedState];
        int[] transEntry = fTransTable[unmarkedState];
        // Mark this one final if it contains the EOC state  
        fFinalStateFlags[unmarkedState] = setT.getBit(EOCPos);
        // Bump up the unmarked state count, marking this state done  
        unmarkedState++;
        // Loop through each possible input symbol in the element map  
        CMStateSet newSet = null;
        /* Optimization(Jan, 2001) */
        int sorterIndex = 0;
        /* Optimization(Jan, 2001) */
        for (int elemIndex = 0; elemIndex < fElemMapSize; elemIndex++) {
            //  
            //  Build up a set of states which is the union of all of  
            //  the follow sets of DFA positions that are in the current  
            //  state. If we gave away the new set last time through then  
            //  create a new one. Otherwise, zero out the existing one.  
            //  
            if (newSet == null)
                newSet = new CMStateSet(fLeafCount);
            else
                newSet.zeroBits();
            /* Optimization(Jan, 2001) */
            int leafIndex = fLeafSorter[sorterIndex++];
            while (leafIndex != -1) {
                // If this leaf index (DFA position) is in the current set...  
                if (setT.getBit(leafIndex)) {
                    //  
                    //  If this leaf is the current input symbol, then we  
                    //  want to add its follow list to the set of states to  
                    //  transition to from the current state.  
                    //  
                    newSet.union(fFollowList[leafIndex]);
                }
                leafIndex = fLeafSorter[sorterIndex++];
            }
            /* Optimization(Jan, 2001) */
            //  
            //  If this new set is not empty, then see if its in the list  
            //  of states to do. If not, then add it.  
            //  
            if (!newSet.isEmpty()) {
                //  
                //  Search the 'states to do' list to see if this new  
                //  state set is already in there.  
                //  
                /* Optimization(Jan, 2001) */
                Integer stateObj = (Integer) stateTable.get(newSet);
                int stateIndex = (stateObj == null ? curState : stateObj.intValue());
                /* Optimization(Jan, 2001) */
                // If we did not find it, then add it  
                if (stateIndex == curState) {
                    //  
                    //  Put this new state into the states to do and init  
                    //  a new entry at the same index in the transition  
                    //  table.  
                    //  
                    statesToDo[curState] = newSet;
                    fTransTable[curState] = makeDefStateList();
                    /* Optimization(Jan, 2001) */
                    stateTable.put(newSet, new Integer(curState));
                    /* Optimization(Jan, 2001) */
                    // We now have a new state to do so bump the count  
                    curState++;
                    //  
                    //  Null out the new set to indicate we adopted it.  
                    //  This will cause the creation of a new set on the  
                    //  next time around the loop.  
                    //  
                    newSet = null;
                }
                //  
                //  Now set this state in the transition table's entry  
                //  for this element (using its index), with the DFA  
                //  state we will move to from the current state when we  
                //  see this input element.  
                //  
                transEntry[elemIndex] = stateIndex;
                // Expand the arrays if we're full  
                if (curState == curArraySize) {
                    //  
                    //  Yikes, we overflowed the initial array size, so  
                    //  we've got to expand all of these arrays. So adjust  
                    //  up the size by 50% and allocate new arrays.  
                    //  
                    final int newSize = (int) (curArraySize * 1.5);
                    CMStateSet[] newToDo = new CMStateSet[newSize];
                    boolean[] newFinalFlags = new boolean[newSize];
                    int[][] newTransTable = new int[newSize][];
                    // Copy over all of the existing content  
                    System.arraycopy(statesToDo, 0, newToDo, 0, curArraySize);
                    System.arraycopy(fFinalStateFlags, 0, newFinalFlags, 0, curArraySize);
                    System.arraycopy(fTransTable, 0, newTransTable, 0, curArraySize);
                    // Store the new array size  
                    curArraySize = newSize;
                    statesToDo = newToDo;
                    fFinalStateFlags = newFinalFlags;
                    fTransTable = newTransTable;
                }
            }
        }
    }
    //  
    // Fill in the occurence information for each looping state   
    // if we're using counters.  
    //  
    if (elemOccurenceMap != null) {
        fCountingStates = new Occurence[curState];
        for (int i = 0; i < curState; ++i) {
            int[] transitions = fTransTable[i];
            for (int j = 0; j < transitions.length; ++j) {
                if (i == transitions[j]) {
                    fCountingStates[i] = elemOccurenceMap[j];
                    break;
                }
            }
        }
    }
    //  
    //  And now we can say bye bye to the temp representation since we've  
    //  built the DFA.  
    //  
    if (DEBUG_VALIDATE_CONTENT)
        dumpTree(fHeadNode, 0);
    fHeadNode = null;
    fLeafList = null;
    fFollowList = null;
    fLeafListType = null;
    fElemMapId = null;
}
