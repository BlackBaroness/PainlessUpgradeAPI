package ru.baronessdev.lib.painlessupgrade.comparator;

public abstract class VersionComparator<V> {

    public abstract boolean equals(V oldVersion, V annotatedVersion);
}
