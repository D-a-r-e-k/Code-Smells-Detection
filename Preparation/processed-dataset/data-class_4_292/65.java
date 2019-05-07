void swapLeftAndRightNodes() {
    Expression temp = nodes[LEFT];
    nodes[LEFT] = nodes[RIGHT];
    nodes[RIGHT] = temp;
}
