// 2 * 256 
void GenerateNonAsciiMoves(java.io.PrintWriter ostr) {
    int i = 0, j = 0;
    char hiByte;
    int cnt = 0;
    long[][] loBytes = new long[256][4];
    if ((charMoves == null || charMoves[0] == 0) && (rangeMoves == null || rangeMoves[0] == 0))
        return;
    if (charMoves != null) {
        for (i = 0; i < charMoves.length; i++) {
            if (charMoves[i] == 0)
                break;
            hiByte = (char) (charMoves[i] >> 8);
            loBytes[hiByte][(charMoves[i] & 0xff) / 64] |= (1L << ((charMoves[i] & 0xff) % 64));
        }
    }
    if (rangeMoves != null) {
        for (i = 0; i < rangeMoves.length; i += 2) {
            if (rangeMoves[i] == 0)
                break;
            char c, r;
            r = (char) (rangeMoves[i + 1] & 0xff);
            hiByte = (char) (rangeMoves[i] >> 8);
            if (hiByte == (char) (rangeMoves[i + 1] >> 8)) {
                for (c = (char) (rangeMoves[i] & 0xff); c <= r; c++) loBytes[hiByte][c / 64] |= (1L << (c % 64));
                continue;
            }
            for (c = (char) (rangeMoves[i] & 0xff); c <= 0xff; c++) loBytes[hiByte][c / 64] |= (1L << (c % 64));
            while (++hiByte < (char) (rangeMoves[i + 1] >> 8)) {
                loBytes[hiByte][0] |= 0xffffffffffffffffL;
                loBytes[hiByte][1] |= 0xffffffffffffffffL;
                loBytes[hiByte][2] |= 0xffffffffffffffffL;
                loBytes[hiByte][3] |= 0xffffffffffffffffL;
            }
            for (c = 0; c <= r; c++) loBytes[hiByte][c / 64] |= (1L << (c % 64));
        }
    }
    long[] common = null;
    boolean[] done = new boolean[256];
    for (i = 0; i <= 255; i++) {
        if (done[i] || (done[i] = loBytes[i][0] == 0 && loBytes[i][1] == 0 && loBytes[i][2] == 0 && loBytes[i][3] == 0))
            continue;
        for (j = i + 1; j < 256; j++) {
            if (done[j])
                continue;
            if (loBytes[i][0] == loBytes[j][0] && loBytes[i][1] == loBytes[j][1] && loBytes[i][2] == loBytes[j][2] && loBytes[i][3] == loBytes[j][3]) {
                done[j] = true;
                if (common == null) {
                    done[i] = true;
                    common = new long[4];
                    common[i / 64] |= (1L << (i % 64));
                }
                common[j / 64] |= (1L << (j % 64));
            }
        }
        if (common != null) {
            Integer ind;
            String tmp;
            tmp = "{\n   0x" + Long.toHexString(common[0]) + "L, " + "0x" + Long.toHexString(common[1]) + "L, " + "0x" + Long.toHexString(common[2]) + "L, " + "0x" + Long.toHexString(common[3]) + "L\n};";
            if ((ind = (Integer) lohiByteTab.get(tmp)) == null) {
                allBitVectors.add(tmp);
                if (!AllBitsSet(tmp))
                    ostr.println("static final long[] jjbitVec" + lohiByteCnt + " = " + tmp);
                lohiByteTab.put(tmp, ind = new Integer(lohiByteCnt++));
            }
            tmpIndices[cnt++] = ind.intValue();
            tmp = "{\n   0x" + Long.toHexString(loBytes[i][0]) + "L, " + "0x" + Long.toHexString(loBytes[i][1]) + "L, " + "0x" + Long.toHexString(loBytes[i][2]) + "L, " + "0x" + Long.toHexString(loBytes[i][3]) + "L\n};";
            if ((ind = (Integer) lohiByteTab.get(tmp)) == null) {
                allBitVectors.add(tmp);
                if (!AllBitsSet(tmp))
                    ostr.println("static final long[] jjbitVec" + lohiByteCnt + " = " + tmp);
                lohiByteTab.put(tmp, ind = new Integer(lohiByteCnt++));
            }
            tmpIndices[cnt++] = ind.intValue();
            common = null;
        }
    }
    nonAsciiMoveIndices = new int[cnt];
    System.arraycopy(tmpIndices, 0, nonAsciiMoveIndices, 0, cnt);
    /*
      System.out.println("state : " + stateName + " cnt : " + cnt);
      while (cnt > 0)
      {
         System.out.print(nonAsciiMoveIndices[cnt - 1] + ", " + nonAsciiMoveIndices[cnt - 2] + ", ");
         cnt -= 2;
      }
      System.out.println("");
*/
    for (i = 0; i < 256; i++) {
        if (done[i])
            loBytes[i] = null;
        else {
            //System.out.print(i + ", "); 
            String tmp;
            Integer ind;
            tmp = "{\n   0x" + Long.toHexString(loBytes[i][0]) + "L, " + "0x" + Long.toHexString(loBytes[i][1]) + "L, " + "0x" + Long.toHexString(loBytes[i][2]) + "L, " + "0x" + Long.toHexString(loBytes[i][3]) + "L\n};";
            if ((ind = (Integer) lohiByteTab.get(tmp)) == null) {
                allBitVectors.add(tmp);
                if (!AllBitsSet(tmp))
                    ostr.println("static final long[] jjbitVec" + lohiByteCnt + " = " + tmp);
                lohiByteTab.put(tmp, ind = new Integer(lohiByteCnt++));
            }
            if (loByteVec == null)
                loByteVec = new Vector();
            loByteVec.add(new Integer(i));
            loByteVec.add(ind);
        }
    }
    //System.out.println(""); 
    UpdateDuplicateNonAsciiMoves();
}
