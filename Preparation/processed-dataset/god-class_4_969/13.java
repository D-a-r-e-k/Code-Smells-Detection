/******************************************************************************/
/**
 * Performs one round, so thats the main part of the Algorithm.
 * Different to the original Implementation of the Algorithm, this Algorithm
 * doesn't work with aproximativ 30 random Placements per Cell to find the best
 * Position. This Algorithm works with a user defined number of segments. The
 * Circle, the Cells will be placed on, is calculated like the original
 * Implementation tells. But it is splited into a user defined number of
 * segments. Then per cell a random offset is calculated and starting from
 * that offset every segment is checked out, whether there is a better position
 * for the cell. This can be done in a random order of the cells or always in
 * the same order. Temperature is decreased after all cells are checked out
 * for a new position, like in the original. While the original Implementation
 * allows always uphill moves, this Algorithm allows the user to decide to work
 * with or without them.
 */
private void performRound() {
    Point2D.Double[] config = getConfig();
    double startEnergy = getGlobalCosts(lambdaList);
    double globalEnergy = startEnergy;
    double newGlobalEnergy = globalEnergy * 1.1;
    //somewhat higher than globalEnergy 
    //sequencial order cells are computed (every round the same order) 
    int[] sequence = new int[applyCellList.size()];
    if (!computePermutation)
        for (int i = 0; i < applyCellList.size(); i++) sequence[i] = i;
    for (int i = 0; i < applyCellList.size(); i++) {
        if (computePermutation)
            //random order 
            sequence = createPermutation(applyCellList.size());
        //random offset 
        double offset = Math.random() * 2.0 * Math.PI;
        for (int j = 0; j < triesPerCell; j++) {
            double angle = j * ((2.0 * Math.PI) / triesPerCell);
            angle += offset;
            Point2D.Double move = null;
            //calculating new move  
            if (isCluster((CellView) applyCellList.get(i))) {
                move = new Point2D.Double(clusterMoveScaleFactor * temperature * Math.cos(angle), clusterMoveScaleFactor * temperature * Math.sin(angle));
            } else {
                move = new Point2D.Double(temperature * Math.cos(angle), temperature * Math.sin(angle));
            }
            //                Point2D.Double randomMove = getRandomVector(temperature); 
            //applying new move 
            setPosition(sequence[i], config[sequence[i]].x + move.x, config[sequence[i]].y + move.y);
            //calculating the costs for the actual layout 
            newGlobalEnergy = getGlobalCosts(lambdaList);
            //taking move if costs < previos cost or uphill move possible 
            if (newGlobalEnergy < globalEnergy || (getBolzmanBreak(globalEnergy, newGlobalEnergy) && uphillMovesAllowed)) {
                //                    if( isDebugging ) 
                //                        System.out.println("taking new energy : "+globalEnergy+" -> "+newGlobalEnergy+" <<<<<<<<<<<<<<<<<<<<<<<<<"); 
                globalEnergy = newGlobalEnergy;
                config[sequence[i]] = new Point2D.Double(config[sequence[i]].x + move.x, config[sequence[i]].y + move.y);
                //                    if( isDebugging )          
                //                        showApplyCellList(); 
                break;
            } else {
                //                    if( isDebugging ) 
                //                        System.out.println("energy = "+globalEnergy+"   new Global Energy = "+newGlobalEnergy+"   temperature = "+temperature); 
                setPosition(sequence[i], config[sequence[i]].x, config[sequence[i]].y);
            }
            setProgress((int) (((double) ((round * applyCellList.size() * triesPerCell) + (i * triesPerCell) + j) / (double) (maxRounds * applyCellList.size() * triesPerCell)) * (100.0)));
            if (!isAllowedToRun())
                break;
        }
        //if this rounds runs very good and energy is 5% of starting value 
        //then break this round and start next round 
        if (globalEnergy == startEnergy * 0.05)
            break;
        if (!isAllowedToRun())
            break;
    }
    //temperature will be decreased 
    temperature *= tempScaleFactor;
    round++;
}
