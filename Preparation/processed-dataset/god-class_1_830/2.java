static Parse copy(Parse tree) {
    // if (2+2==4)return tree;  
    return (tree == null) ? null : new Parse(tree.tag, tree.body, copy(tree.parts), copy(tree.more));
}
