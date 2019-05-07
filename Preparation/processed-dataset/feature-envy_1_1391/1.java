public CsWord(int action, int direction) {
    super("CsWord_" + "_" + ACTIONS[action] + "_" + DIRECTIONS[((direction > 0) ? 1 : 0)]);
    this.action = action;
    this.direction = direction;
}
