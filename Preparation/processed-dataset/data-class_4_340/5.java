/** Call the update method to propagate down the tree, parsing input values into their config options. */
public boolean apply() {
    // returns false if the update did not succeed 
    return _rootNode.update();
}
