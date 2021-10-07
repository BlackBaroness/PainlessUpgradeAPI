package ru.baronessdev.lib.painlessupgrade.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.baronessdev.lib.painlessupgrade.PainlessUpgradeAPI;
import ru.baronessdev.lib.painlessupgrade.annotations.IntUpgradePoint;
import ru.baronessdev.lib.painlessupgrade.comparator.IntVersionComparator;
import ru.baronessdev.lib.painlessupgrade.comparator.VersionComparator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IntVersionUpgradeAPI extends PainlessUpgradeAPI<Object, Integer, VersionComparator<Integer>> {

    @Override
    public boolean upgrade(@NotNull Object source,
                           @NotNull Integer oldVersion,
                           @NotNull VersionComparator<Integer> comparator,
                           @Nullable Object... methodParams) throws InvocationTargetException, IllegalAccessException {

        Set<Method> upgradePoints = filterMethods(Arrays.stream(source.getClass().getDeclaredMethods()), IntUpgradePoint.class, methodParams)
                .filter(method -> {
                    // old version must equal
                    return Arrays.stream(method.getAnnotation(IntUpgradePoint.class).value())
                            .anyMatch(applicableVersion -> comparator.equals(oldVersion, applicableVersion));
                }).collect(Collectors.toSet());

        for (Method method : upgradePoints) {
            method.invoke(source);
        }

        return !upgradePoints.isEmpty();
    }

    public boolean upgrade(@NotNull Object source,
                           @NotNull Integer oldVersion,
                           @Nullable Object... methodParams) throws InvocationTargetException, IllegalAccessException {
        return upgrade(source, oldVersion, new IntVersionComparator(), methodParams);
    }
}
