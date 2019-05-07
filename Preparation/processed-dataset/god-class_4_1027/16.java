public Player getPlayer() {
    if (mPlayer == null)
        mPlayer = new Player(this);
    return mPlayer;
}
