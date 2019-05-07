package net.suberic.pooka.gui;

/**
 * This is just a wrapper class for handling responses from other
 * threads.  We're doing it like this because of the old final problems
 * in going between multiple threads.
 */
class ResponseWrapper {

  // the return values;
  int mInt = -1;
  boolean mBoolean = false;
  String mString = "";

  public void setInt(int pInt) {
    mInt = pInt;
  }

  public int getInt() {
    return mInt;
  }

  public void setBoolean(boolean pBoolean) {
    mBoolean = pBoolean;
  }

  public boolean getBoolean() {
    return mBoolean;
  }

  public void setString(String pString) {
    mString = pString;
  }

  public String getString() {
    return mString;
  }
}
