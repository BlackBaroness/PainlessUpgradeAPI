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

    public void upgrade(@NotNull Object source,
                        @NotNull String currentVersion,
                        @NotNull String oldVersion,
                        @NotNull VersionComparator<String, String> comparator) throws Exception {
        Set<Method> upgradePoints = filterMethods(Arrays.stream(source.getClass().getDeclaredMethods()))
                .filter(method -> {
                    Policy policy = Policy.LAST;

                    UpgradePolicy annotationPolicy = method.getAnnotation(UpgradePolicy.class);
                    if (annotationPolicy != null) {
                        policy = annotationPolicy.value();
                    }

                    // upgrade methods with policy NEVER will not be executed
                    if (policy == Policy.NEVER) return false;

                    // policy ALWAYS makes methods always executable
                    if (policy == Policy.ALWAYS) return true;

                    // calculation of diff between versions
                    String[] notedVersions = method.getAnnotation(UpgradePoint.class).value();
                    int diff;
                    try {
                        diff = getDiff(notedVersions, currentVersion, oldVersion, comparator);
                    } catch (UpgradeException e) {
                        // version is not acceptable
                        return false;
                    }

                    // if policy is LAST, diff between versions must be 1
                    if (policy == Policy.LAST) return diff == 1;

                    // if policy is AFTER, diff must be positive
                    if (policy == Policy.AFTER) return diff > 0;

                    return false;
                })

                .collect(Collectors.toSet());

        for (Method method : upgradePoints) {
            method.invoke(source);
        }
    }
}