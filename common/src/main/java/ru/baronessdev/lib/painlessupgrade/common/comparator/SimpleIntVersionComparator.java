package ru.baronessdev.lib.painlessupgrade.common.comparator;

public class SimpleIntVersionComparator extends VersionComparator<String, String> {

    @Override
    public int getDiff(String currentVersion, String oldVersion) {
        try {
            return Integer.parseInt(currentVersion) - Integer.parseInt(oldVersion);
        } catch (Exception ignored) {
        }

        return 0;
    }
}
