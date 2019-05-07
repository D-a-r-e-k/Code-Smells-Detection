// }}}  
//{{{ addNotify() method  
@Override
public void addNotify() {
    super.addNotify();
    EditBus.addToBus(this);
}
