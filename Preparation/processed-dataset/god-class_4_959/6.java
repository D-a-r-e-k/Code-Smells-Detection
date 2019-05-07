public Object visit(final LineNumberTableAttribute_info attribute, final Object ctx) {
    final boolean trace2 = m_log.atTRACE2();
    final boolean trace3 = m_log.atTRACE3();
    if (trace2)
        m_log.trace2("visit", "attribute: [" + attribute.getName(m_cls) + "]");
    final int lineCount = attribute.size();
    if (m_metadata) {
        if (trace2)
            m_log.trace2("visit", "processing line number table for metadata...");
        final int blockCount = m_classBlockCounts[m_methodID];
        if ($assert.ENABLED)
            $assert.ASSERT(blockCount > 0, "invalid method block count for method " + m_methodID);
        final int[][] blockLineMap = new int[blockCount][];
        if ($assert.ENABLED)
            $assert.ASSERT(blockCount + 1 == m_methodBlockOffsets.length, "invalid m_methodBlockOffsets");
        if (lineCount == 0) {
            for (int bl = 0; bl < blockCount; ++bl) blockLineMap[bl] = EMPTY_INT_ARRAY;
        } else {
            // TODO: this code does not work if there are multiple LineNumberTableAttribute attributes for the method  
            final LineNumber_info[] sortedLines = new LineNumber_info[attribute.size()];
            for (int l = 0; l < lineCount; ++l) {
                final LineNumber_info line = attribute.get(l);
                sortedLines[l] = line;
            }
            Arrays.sort(sortedLines, LINE_NUMBER_COMPARATOR);
            // construct block->line mapping: TODO: is the loop below the fastest it can be done?  
            final int[] methodBlockOffsets = m_methodBlockOffsets;
            LineNumber_info line = sortedLines[0];
            // never null  
            LineNumber_info prev_line = null;
            // remember the first line:  
            m_methodFirstLine = line.m_line_number;
            for (int bl = 0, l = 0; bl < blockCount; ++bl) {
                final IntSet blockLines = new IntSet();
                if ((prev_line != null) && (line.m_start_pc > methodBlockOffsets[bl])) {
                    blockLines.add(prev_line.m_line_number);
                }
                while (line.m_start_pc < methodBlockOffsets[bl + 1]) {
                    blockLines.add(line.m_line_number);
                    if (l == lineCount - 1)
                        break;
                    else {
                        prev_line = line;
                        line = sortedLines[++l];
                    }
                }
                blockLineMap[bl] = blockLines.values();
            }
        }
        m_classBlockMetadata[m_methodID] = blockLineMap;
        if (trace3) {
            StringBuffer s = new StringBuffer("block-line map for method #" + m_methodID + ":");
            for (int bl = 0; bl < blockCount; ++bl) {
                s.append(EOL);
                s.append("    block " + bl + ": ");
                final int[] lines = blockLineMap[bl];
                for (int l = 0; l < lines.length; ++l) {
                    if (l != 0)
                        s.append(", ");
                    s.append(lines[l]);
                }
            }
            m_log.trace3("visit", s.toString());
        }
    }
    for (int l = 0; l < lineCount; ++l) {
        final LineNumber_info line = attribute.get(l);
        // TODO: make this faster using either table assist or the sorted array in 'sortedLines'  
        // adjust bytecode offset for line number mapping:  
        int adjSegment = lowbound(m_methodJumpAdjOffsets, line.m_start_pc);
        line.m_start_pc += m_methodJumpAdjValues[adjSegment];
    }
    return ctx;
}
