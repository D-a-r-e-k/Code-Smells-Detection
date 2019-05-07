public boolean handleAction(BView view, Object action) {
    if (action.equals("pop")) {
        pop();
        remove();
        return true;
    }
    return super.handleAction(view, action);
}
