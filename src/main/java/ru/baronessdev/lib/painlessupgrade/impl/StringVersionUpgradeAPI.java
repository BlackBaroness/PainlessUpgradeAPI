package ru.baronessdev.lib.painlessupgrade.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.baronessdev.lib.painlessupgrade.PainlessUpgradeAPI;
import ru.baronessdev.lib.painlessupgrade.annotations.UpgradePoint;
import ru.baronessdev.lib.painlessupgrade.comparator.StringVersionComparator;
import ru.baronessdev.lib.painlessupgrade.comparator.VersionComparator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class StringVersionUpgradeAPI extends PainlessUpgradeAPI<Object, String, VersionComparator<String>> {

    @Override
    public boolean upgrade(@NotNull Object source,
                           @NotNull String oldVersion,
                           @NotNull VersionComparator<String> comparator,
                           @Nullable Object... methodParams) throws InvocationTargetException, IllegalAccessException {

        Set<Method> upgradePoints = filterMethods(Arrays.stream(source.getClass().getDeclaredMethods()), UpgradePoint.class, methodParams)
                .filter(method -> {
                    // old version must equal
                    return Arrays.stream(method.getAnnotation(UpgradePoint.class).value())
                            .anyMatch(applicableVersion -> comparator.equals(oldVersion, applicableVersion));
                }).collect(Collectors.toSet());

        for (Method method : upgradePoints) {
            method.invoke(source);
        }

        return !upgradePoints.isEmpty();
    }

    public boolean upgrade(@NotNull Object source,
                           @NotNull String oldVersion,
                           @Nullable Object... methodParams) throws InvocationTargetException, IllegalAccessException {
        return upgrade(source, oldVersion, new StringVersionComparator(), methodParams);
    }
}
