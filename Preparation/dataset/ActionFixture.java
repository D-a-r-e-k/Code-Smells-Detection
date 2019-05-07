package fit;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import java.lang.reflect.Method;

public class ActionFixture extends Fixture {
    protected Parse cells;
    public static Fixture actor;
    protected static Class empty[] = {};

    // Traversal ////////////////////////////////

    public void doCells(Parse cells) {
        this.cells = cells;
        try {
            Method action = getClass().getMethod(cells.text(), empty);
            action.invoke(this, empty);
        } catch (Exception e) {
            exception(cells, e);
        }
    }

    // Actions //////////////////////////////////

    public void start() throws Exception {
        actor = (Fixture)(Class.forName(cells.more.text()).newInstance());
    }

    public void enter() throws Exception {
        Method method = method(1);
        Class type = method.getParameterTypes()[0];
        String text = cells.more.more.text();
        Object args[] = {TypeAdapter.on(actor, type).parse(text)};
        method.invoke(actor, args);
    }

    public void press() throws Exception {
        method(0).invoke(actor, empty);
    }

    public void check() throws Exception {
        TypeAdapter adapter = TypeAdapter.on(actor, method(0));
        check (cells.more.more, adapter);
    }

    // Utility //////////////////////////////////

    protected Method method(int args) throws NoSuchMethodException {
        return method(camel(cells.more.text()), args);
    }

    protected Method method(String test, int args) throws NoSuchMethodException {
        Method methods[] = actor.getClass().getMethods();
        Method result = null;
        for (int i=0; i<methods.length; i++) {
            Method m = methods[i];
            if (m.getName().equals(test) && m.getParameterTypes().length == args) {
                if (result==null) {
                    result = m;
                } else {
                    throw new NoSuchMethodException("too many implementations");
                }
            }
        }
        if (result==null) {
            throw new NoSuchMethodException();
        }
        return result;
    }
}