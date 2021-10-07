package ru.baronessdev.lib.painlessupgrade.common.comparator;

public class StringVersionComparator extends VersionComparator<String>{

    @Override
    public boolean equals(String oldVersion, String annotatedVersion) {
        return oldVersion.equals(annotatedVersion);
    }
}
