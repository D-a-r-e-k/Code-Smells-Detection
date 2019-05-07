// IAttributeVisitor:  
public Object visit(final CodeAttribute_info attribute, final Object ctx) {
    final boolean trace2 = m_log.atTRACE2();
    final boolean trace3 = m_log.atTRACE3();
    final byte[] code = attribute.getCode();
    final int codeSize = attribute.getCodeSize();
    if (trace2)
        m_log.trace2("visit", "code attribute for method #" + m_methodID + ": size = " + codeSize);
    final IntSet leaders = new IntSet();
    // instructionMap.get(ip) is the number of instructions in code[0-ip)  
    // [this map will include a mapping for code length as well]  
    final IntIntMap /* int(ip)->instr count */
    instructionMap = new IntIntMap();
    // add first instruction and all exc handler start pcs:  
    leaders.add(0);
    final IExceptionHandlerTable exceptions = attribute.getExceptionTable();
    final int exceptionCount = exceptions.size();
    for (int e = 0; e < exceptionCount; ++e) {
        final Exception_info exception = exceptions.get(e);
        leaders.add(exception.m_handler_pc);
    }
    final IntObjectMap branches = new IntObjectMap();
    // determine block leaders [an O(code length) loop]:  
    boolean branch = false;
    boolean wide = false;
    int instructionCount = 0;
    instructionMap.put(0, 0);
    for (int ip = 0; ip < codeSize; ) {
        final int opcode = 0xFF & code[ip];
        int size = 0;
        // will be set to -<real size> for special cases in the switch below   
        //if (trace3) m_log.trace3 ("parse", MNEMONICS [opcode]);  
        // "visitor.visit (opcode, wide, ip, null)":  
        {
            // "opcode visit" logic:  
            int iv, ov;
            if (branch) {
                // previous instruction was a branch: this one is a leader  
                leaders.add(ip);
                branch = false;
            }
            switch(opcode) {
                case _ifeq:
                case _iflt:
                case _ifle:
                case _ifne:
                case _ifgt:
                case _ifge:
                case _ifnull:
                case _ifnonnull:
                case _if_icmpeq:
                case _if_icmpne:
                case _if_icmplt:
                case _if_icmpgt:
                case _if_icmple:
                case _if_icmpge:
                case _if_acmpeq:
                case _if_acmpne:
                    {
                        //ov = getI2 (code, ip + 1);  
                        int scan = ip + 1;
                        ov = (code[scan] << 8) | (0xFF & code[++scan]);
                        final int target = ip + ov;
                        leaders.add(target);
                        branches.put(ip, new IFJUMP2(opcode, target));
                        branch = true;
                    }
                    break;
                case _goto:
                case _jsr:
                    {
                        //ov = getI2 (code, ip + 1);  
                        int scan = ip + 1;
                        ov = (code[scan] << 8) | (0xFF & code[++scan]);
                        final int target = ip + ov;
                        leaders.add(target);
                        branches.put(ip, new JUMP2(opcode, target));
                        branch = true;
                    }
                    break;
                case _lookupswitch:
                    {
                        int scan = ip + 4 - (ip & 3);
                        // eat padding  
                        ov = (code[scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                        leaders.add(ip + ov);
                        //final int npairs = getU4 (code, scan);  
                        //scan += 4;  
                        final int npairs = ((0xFF & code[++scan]) << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                        final int[] keys = new int[npairs];
                        final int[] targets = new int[npairs + 1];
                        targets[0] = ip + ov;
                        for (int p = 0; p < npairs; ++p) {
                            //iv = getI4 (code, scan);  
                            //scan += 4;  
                            iv = (code[++scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                            keys[p] = iv;
                            //ov = getI4 (code, scan);  
                            //scan += 4;  
                            ov = (code[++scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                            targets[p + 1] = ip + ov;
                            leaders.add(ip + ov);
                        }
                        branches.put(ip, new LOOKUPSWITCH(keys, targets));
                        branch = true;
                        size = ip - scan - 1;
                    }
                    break;
                case _tableswitch:
                    {
                        int scan = ip + 4 - (ip & 3);
                        // eat padding  
                        ov = (code[scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                        leaders.add(ip + ov);
                        //final int low = getI4 (code, scan + 4);  
                        final int low = (code[++scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                        //final int high = getI4 (code, scan + 8);  
                        //scan += 12;  
                        final int high = (code[++scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                        final int[] targets = new int[high - low + 2];
                        targets[0] = ip + ov;
                        for (int index = low; index <= high; ++index) {
                            //ov = getI4 (code, scan);  
                            ov = (code[++scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                            targets[index - low + 1] = ip + ov;
                            leaders.add(ip + ov);
                        }
                        branches.put(ip, new TABLESWITCH(low, high, targets));
                        branch = true;
                        size = ip - scan - 1;
                    }
                    break;
                case _goto_w:
                case _jsr_w:
                    {
                        int scan = ip + 1;
                        //ov = getI4 (code, ip + 1);  
                        ov = (code[scan] << 24) | ((0xFF & code[++scan]) << 16) | ((0xFF & code[++scan]) << 8) | (0xFF & code[++scan]);
                        final int target = ip + ov;
                        leaders.add(target);
                        branches.put(ip, new JUMP4(opcode, target));
                        branch = true;
                    }
                    break;
                case _ret:
                    {
                        int scan = ip + 1;
                        iv = wide ? (((0xFF & code[scan]) << 8) | (0xFF & code[++scan])) : (0xFF & code[scan]);
                        branches.put(ip, new RET(opcode, iv));
                        branch = true;
                    }
                    break;
                case _athrow:
                case _ireturn:
                case _lreturn:
                case _freturn:
                case _dreturn:
                case _areturn:
                case _return:
                    {
                        branches.put(ip, new TERMINATE(opcode));
                        branch = true;
                    }
                    break;
            }
        }
        // end of processing the current opcode  
        // shift to the next instruction [this is the only block that adjusts 'ip']:  
        if (size == 0)
            size = (wide ? WIDE_SIZE : NARROW_SIZE)[opcode];
        else
            size = -size;
        ip += size;
        wide = (opcode == _wide);
        instructionMap.put(ip, ++instructionCount);
    }
    // end of for  
    // split 'code' into an ordered list of basic blocks [O(block count) loops]:  
    final int blockCount = leaders.size();
    if (trace2)
        m_log.trace2("visit", "method contains " + blockCount + " basic blocks");
    final BlockList blocks = new BlockList(blockCount);
    final int[] _leaders = new int[blockCount + 1];
    // room for end-of-code leader at the end   
    leaders.values(_leaders, 0);
    _leaders[blockCount] = codeSize;
    Arrays.sort(_leaders);
    final int[] _branch_locations = branches.keys();
    Arrays.sort(_branch_locations);
    final IntIntMap leaderToBlockID = new IntIntMap(_leaders.length);
    if (m_metadata) {
        // help construct a MethodDescriptor for the current method:  
        m_methodBlockSizes = new int[blockCount];
        m_methodBlockOffsets = _leaders;
    }
    // compute signature even if metadata is not needed (because the instrumented  
    // classdef uses it):  
    consumeSignatureData(m_methodID, _leaders);
    // pass 1:  
    final int[] intHolder = new int[1];
    int instr_count = 0, prev_instr_count;
    for (int bl = 0, br = 0; bl < blockCount; ++bl) {
        final Block block = new Block();
        blocks.m_blocks.add(block);
        final int leader = _leaders[bl];
        block.m_first = leader;
        // m_first set  
        leaderToBlockID.put(leader, bl);
        final int next_leader = _leaders[bl + 1];
        boolean branchDelimited = false;
        prev_instr_count = instr_count;
        if (_branch_locations.length > br) {
            final int next_branch_location = _branch_locations[br];
            if (next_branch_location < next_leader) {
                branchDelimited = true;
                block.m_length = next_branch_location - leader;
                // m_length set  
                if ($assert.ENABLED)
                    $assert.ASSERT(instructionMap.get(next_branch_location, intHolder), "no mapping for " + next_branch_location);
                else
                    instructionMap.get(next_branch_location, intHolder);
                instr_count = intHolder[0] + 1;
                // [+ 1 for the branch]  
                block.m_branch = (Branch) branches.get(next_branch_location);
                block.m_branch.m_parentBlockID = bl;
                // m_branch set  
                ++br;
            }
        }
        if (!branchDelimited) {
            block.m_length = next_leader - leader;
            // m_length set  
            if ($assert.ENABLED)
                $assert.ASSERT(instructionMap.get(next_leader, intHolder), "no mapping for " + next_leader);
            else
                instructionMap.get(next_leader, intHolder);
            instr_count = intHolder[0];
        }
        block.m_instrCount = instr_count - prev_instr_count;
        // m_instrCount set  
        if ($assert.ENABLED)
            $assert.ASSERT(block.m_length == 0 || block.m_instrCount > 0, "invalid instr count for block " + bl + ": " + block.m_instrCount);
        if (m_metadata)
            m_methodBlockSizes[bl] = block.m_instrCount;
    }
    // pass 2:  
    final Block[] _blocks = (Block[]) blocks.m_blocks.toArray(new Block[blockCount]);
    for (int l = 0; l < blockCount; ++l) {
        final Block block = _blocks[l];
        if (block.m_branch != null) {
            final int[] targets = block.m_branch.m_targets;
            if (targets != null) {
                for (int t = 0, targetCount = targets.length; t < targetCount; ++t) {
                    // TODO: HACK ! convert block absolute offsets to block IDs:  
                    if ($assert.ENABLED)
                        $assert.ASSERT(leaderToBlockID.get(targets[t], intHolder), "no mapping for " + targets[t]);
                    else
                        leaderToBlockID.get(targets[t], intHolder);
                    targets[t] = intHolder[0];
                }
            }
        }
    }
    // update block count map [used later by <clinit> visit]:  
    m_classBlockCounts[m_methodID] = blockCount;
    // actual basic block instrumentation:  
    {
        if (trace2)
            m_log.trace2("visit", "instrumenting... ");
        // determine the local var index for the var that will alias COVERAGE_FIELD:  
        final int localVarIndex = attribute.m_max_locals++;
        if (m_methodID == m_clinitID) // note: m_clinitID can be -1 if <clinit> has not been visited yet  
        {
            // add a long stamp constant after all the original methods have been visited:  
            m_stampIndex = m_cls.getConstants().add(new CONSTANT_Long_info(m_classSignature));
            blocks.m_header = new clinitHeader(this, localVarIndex);
        } else
            blocks.m_header = new methodHeader(this, localVarIndex);
        int headerMaxStack = blocks.m_header.maxstack();
        int methodMaxStack = 0;
        for (int l = 0; l < blockCount; ++l) {
            final Block block = _blocks[l];
            final CodeSegment insertion = new BlockSegment(this, localVarIndex, l);
            block.m_insertion = insertion;
            final int insertionMaxStack = insertion.maxstack();
            if (insertionMaxStack > methodMaxStack)
                methodMaxStack = insertionMaxStack;
        }
        // update maxstack as needed [it can only grow]:  
        {
            final int oldMaxStack = attribute.m_max_stack;
            attribute.m_max_stack += methodMaxStack;
            // this is not precise, but still need to add because the insertion may be happening at the old maxstack point  
            if (headerMaxStack > attribute.m_max_stack)
                attribute.m_max_stack = headerMaxStack;
            if (trace3)
                m_log.trace3("visit", "increasing maxstack by " + (attribute.m_max_stack - oldMaxStack));
        }
        if ($assert.ENABLED)
            $assert.ASSERT(blocks.m_header != null, "header not set");
    }
    // assemble all blocks into an instrumented code block:  
    if (trace2)
        m_log.trace2("visit", "assembling... ");
    int newcodeCapacity = codeSize << 1;
    if (newcodeCapacity < EMIT_CTX_MIN_INIT_CAPACITY)
        newcodeCapacity = EMIT_CTX_MIN_INIT_CAPACITY;
    final ByteArrayOStream newcode = new ByteArrayOStream(newcodeCapacity);
    // TODO: empirical capacity  
    final EmitCtx emitctx = new EmitCtx(blocks, newcode);
    // create a jump adjustment map:  
    final int[] jumpAdjOffsets = new int[blockCount];
    // room for initial 0  + (blockCount - 1)  
    final int[] jumpAdjMap = new int[jumpAdjOffsets.length];
    // room for initial 0  + (blockCount - 1)  
    if ($assert.ENABLED)
        $assert.ASSERT(jumpAdjOffsets.length == jumpAdjMap.length, "jumpAdjOffsets and jumpAdjMap length mismatch");
    // header:  
    blocks.m_header.emit(emitctx);
    // jumpAdjOffsets [0] = 0: redundant  
    jumpAdjMap[0] = emitctx.m_out.size();
    // rest of blocks:  
    for (int l = 0; l < blockCount; ++l) {
        final Block block = _blocks[l];
        if (l + 1 < blockCount) {
            jumpAdjOffsets[l + 1] = _blocks[l].m_first + _blocks[l].m_length;
        }
        block.emit(emitctx, code);
        // TODO: this breaks if code can shrink:  
        if (l + 1 < blockCount) {
            jumpAdjMap[l + 1] = emitctx.m_out.size() - _blocks[l + 1].m_first;
        }
    }
    m_methodJumpAdjOffsets = jumpAdjOffsets;
    m_methodJumpAdjValues = jumpAdjMap;
    if (trace3) {
        final StringBuffer s = new StringBuffer("jump adjustment map:" + EOL);
        for (int a = 0; a < jumpAdjOffsets.length; ++a) {
            s.append("    " + jumpAdjOffsets[a] + ": +" + jumpAdjMap[a]);
            if (a < jumpAdjOffsets.length - 1)
                s.append(EOL);
        }
        m_log.trace3("visit", s.toString());
    }
    final byte[] _newcode = newcode.getByteArray();
    // note: not cloned   
    final int _newcodeSize = newcode.size();
    // [all blocks have had their m_first adjusted]  
    // backpatching pass:          
    if (trace3)
        m_log.trace3("visit", "backpatching " + emitctx.m_backpatchQueue.size() + " ip(s)");
    for (Iterator i = emitctx.m_backpatchQueue.iterator(); i.hasNext(); ) {
        final int[] patchData = (int[]) i.next();
        int ip = patchData[1];
        if ($assert.ENABLED)
            $assert.ASSERT(patchData != null, "null patch data for ip " + ip);
        final int jump = _blocks[patchData[3]].m_first - patchData[2];
        if ($assert.ENABLED)
            $assert.ASSERT(jump > 0, "negative backpatch jump offset " + jump + " for ip " + ip);
        switch(patchData[0]) {
            case 4:
                {
                    _newcode[ip++] = (byte) (jump >>> 24);
                    _newcode[ip++] = (byte) (jump >>> 16);
                }
            // *FALL THROUGH*  
            case 2:
                {
                    _newcode[ip++] = (byte) (jump >>> 8);
                    _newcode[ip] = (byte) jump;
                }
        }
    }
    attribute.setCode(_newcode, _newcodeSize);
    if (trace2)
        m_log.trace2("visit", "method assembled into " + _newcodeSize + " code bytes");
    // adjust bytecode offsets in the exception table:  
    final IExceptionHandlerTable exceptionTable = attribute.getExceptionTable();
    for (int e = 0; e < exceptionTable.size(); ++e) {
        final Exception_info exception = exceptionTable.get(e);
        int adjSegment = lowbound(jumpAdjOffsets, exception.m_start_pc);
        exception.m_start_pc += jumpAdjMap[adjSegment];
        adjSegment = lowbound(jumpAdjOffsets, exception.m_end_pc);
        exception.m_end_pc += jumpAdjMap[adjSegment];
        adjSegment = lowbound(jumpAdjOffsets, exception.m_handler_pc);
        exception.m_handler_pc += jumpAdjMap[adjSegment];
    }
    // visit other nested attributes [LineNumberAttribute, etc]:      
    final IAttributeCollection attributes = attribute.getAttributes();
    final int attributeCount = attributes.size();
    for (int a = 0; a < attributeCount; ++a) {
        final Attribute_info nested = attributes.get(a);
        nested.accept(this, ctx);
    }
    return ctx;
}
