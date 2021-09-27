package ru.baronessdev.lib.painlessupgrade.common;

import org.jetbrains.annotations.NotNull;
import ru.baronessdev.lib.painlessupgrade.common.annotations.UpgradePoint;
import ru.baronessdev.lib.painlessupgrade.common.annotations.UpgradePolicy;
import ru.baronessdev.lib.painlessupgrade.common.comparator.VersionComparator;
import ru.baronessdev.lib.painlessupgrade.common.exception.UpgradeException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PainlessUpgradeAPI extends UpgradeAPI<Object, String, String, VersionComparator<String, String>> {

    @UpgradePoint({})
    @UpgradePolicy(Policy.BEFORE)
    public void upgradeFrom198() {

    }

    public void upgrade(@NotNull Object source,
                        @NotNull String currentVersion,
                        @NotNull String oldVersion,
                        @NotNull VersionComparator<String, String> comparator) throws Exception {
        Set<Method> upgradePoints = baseFilterMethods(Arrays.stream(source.getClass().getDeclaredMethods()))
                .filter(method -> {
                    Policy policy = Policy.LAST;
                    UpgradePolicy annotationPolicy = method.getAnnotation(UpgradePolicy.class);
                    if (annotationPolicy != null) {
                        policy = annotationPolicy.value();
                    }

                    // calculation of diff between versions
                    String[] notedVersions = method.getAnnotation(UpgradePoint.class).value();
                    int diff;
                    try {
                        diff = getDiff(notedVersions, currentVersion, oldVersion, comparator);
                    } catch (UpgradeException e) {
                        // version is not acceptable
                        return false;
                    }

                    switch (policy) {
                        case LAST:
                            return onLast(method, diff);
                        case AFTER:
                            return onAfter(method, diff);
                        case BEFORE:
                            return onBefore(method, diff);
                        case ALWAYS:
                            return onAlways(method);
                        case NEVER:
                            return onNever(method);
                    }

                    return false;
                })

                .collect(Collectors.toSet());

        for (Method method : upgradePoints) {
            method.invoke(source);
        }
    }
}