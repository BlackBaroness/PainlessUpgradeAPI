package ru.baronessdev.lib.painlessupgrade.comparator;

public class StringVersionComparator extends VersionComparator<String>{

    @Override
    public boolean equals(String oldVersion, String annotatedVersion) {
        return oldVersion.equals(annotatedVersion);
    }
}
