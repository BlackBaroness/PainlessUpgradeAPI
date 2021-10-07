package ru.baronessdev.lib.painlessupgrade.comparator;

import java.util.Objects;

public class IntVersionComparator extends VersionComparator<Integer> {

    @Override
    public boolean equals(Integer oldVersion, Integer annotatedVersion) {
        return Objects.equals(oldVersion, annotatedVersion);
    }
}
