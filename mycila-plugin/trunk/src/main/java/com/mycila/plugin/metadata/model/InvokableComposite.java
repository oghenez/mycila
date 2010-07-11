package com.mycila.plugin.metadata.model;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.metadata.InvokeException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InvokableComposite implements Invokable {

    private final List<Invokable> invokables = new LinkedList<Invokable>();

    public InvokableComposite(Invokable... invokables) {
        add(invokables);
    }

    public InvokableComposite(Iterable<Invokable> invokables) {
        add(invokables);
    }

    public void add(Invokable... invokables) {
        this.invokables.addAll(Arrays.asList(invokables));
    }

    public void add(Iterable<Invokable> invokables) {
        for (Invokable invokable : invokables)
            this.invokables.add(invokable);
    }

    @Override
    public Object invoke(Object... args) throws InvokeException {
        Object res = null;
        for (Invokable invokable : invokables)
            res = invokable.invoke(args);
        return res;
    }
}
