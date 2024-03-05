package org.example;

import java.util.function.Supplier;

public class OneDependencySupplier implements Supplier<OneDependency> {
    NoDependency noDependency;

    public OneDependencySupplier(NoDependency noDependency) {
        this.noDependency = noDependency;
    }

    @Override
    public OneDependency get() {
        return new OneDependency(new NoDependency());
    }
}
