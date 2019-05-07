protected void solveEdgeCrosses(JGraph jgraph, List levels) {
    movements = new ArrayList(100);
    movementsCurrentLoop = -1;
    movementsMax = Integer.MIN_VALUE;
    iteration = 0;
    while (movementsCurrentLoop != 0) {
        // reset the movements per loop count 
        movementsCurrentLoop = 0;
        // top down 
        for (int i = 0; i < levels.size() - 1; i++) {
            movementsCurrentLoop += solveEdgeCrosses(jgraph, true, levels, i);
        }
        // bottom up 
        for (int i = levels.size() - 1; i >= 1; i--) {
            movementsCurrentLoop += solveEdgeCrosses(jgraph, false, levels, i);
        }
        updateProgress4Movements();
    }
}
