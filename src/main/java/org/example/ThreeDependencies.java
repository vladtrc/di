package org.example;

public class ThreeDependencies {
    NoDependency noDependency;
    OneDependency oneDependency;

    public ThreeDependencies(NoDependency noDependency, OneDependency oneDependency) {
        this.noDependency = noDependency;
        this.oneDependency = oneDependency;
    }
}
