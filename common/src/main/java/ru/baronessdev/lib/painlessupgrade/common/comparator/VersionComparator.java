package ru.baronessdev.lib.painlessupgrade.common.comparator;

public abstract class VersionComparator<C, O> {

    public abstract int getDiff(C currentVersion, O oldVersion);
}
