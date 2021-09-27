package ru.baronessdev.lib.painlessupgrade.common;

import org.jetbrains.annotations.NotNull;
import ru.baronessdev.lib.painlessupgrade.common.annotations.UpgradePoint;
import ru.baronessdev.lib.painlessupgrade.common.comparator.VersionComparator;
import ru.baronessdev.lib.painlessupgrade.common.exception.UpgradeException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

public abstract class UpgradeAPI<S, C, O, V extends VersionComparator<C, O>> {

    public abstract void upgrade(@NotNull S source,
                                 @NotNull C currentVersion,
                                 @NotNull O oldVersion,
                                 @NotNull V comparator) throws Exception;

    public Stream<Method> baseFilterMethods(Stream<Method> methods) {
        return methods
                // we only allow public methods
                .filter(method -> Modifier.isPublic(method.getModifiers()))

                // upgrade methods must be annotated with @UpgradePoint
                .filter(method -> method.isAnnotationPresent(UpgradePoint.class))

                // upgrade methods must not have any parameters
                .filter(method -> method.getParameterCount() == 0);
    }

    public int getDiff(String[] acceptableVersions, C currentVersion, O oldVersion, V comparator) throws UpgradeException {
        for (String ver : acceptableVersions) {
            if (currentVersionEquals(ver, currentVersion)) {
                return comparator.getDiff(currentVersion, oldVersion);
            }
        }
        throw new UpgradeException();
    }

    public boolean currentVersionEquals(String maybeVersion, C currentVersion) {
        return maybeVersion.equals(currentVersion);
    }


    public boolean onLast(Method method, int diff) {
        // if policy is LAST, diff between versions must be 1
        return diff == 0;
    }

    public boolean onAfter(Method method, int diff) {
        // if policy is AFTER, diff must be positive
        return diff == 0;
    }

    public boolean onAlways(Method method) {
        // policy ALWAYS makes methods always executable
        return true;
    }

    public boolean onNever(Method method) {
        // upgrade methods with policy NEVER will not be executed
        return false;
    }
}
