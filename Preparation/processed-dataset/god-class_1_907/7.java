//}}} 
//{{{ refreshContents() 
public void refreshContents() {
    Log.flushStream();
    contentsJList.setModel(getContents());
}
