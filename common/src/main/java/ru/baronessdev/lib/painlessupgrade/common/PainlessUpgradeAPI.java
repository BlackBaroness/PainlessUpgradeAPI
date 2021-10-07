package ru.baronessdev.lib.painlessupgrade.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.baronessdev.lib.painlessupgrade.common.annotations.IntUpgradePoint;
import ru.baronessdev.lib.painlessupgrade.common.annotations.UpgradePoint;
import ru.baronessdev.lib.painlessupgrade.common.comparator.VersionComparator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

public abstract class PainlessUpgradeAPI<S, O, V extends VersionComparator<O>> {

    @IntUpgradePoint({123, 123})
    public abstract boolean upgrade(@NotNull S source,
                                    @NotNull O oldVersion,
                                    @NotNull V comparator,
                                    @Nullable Object... methodParams
    ) throws Exception;

    public @NotNull Stream<Method> filterMethods(@NotNull Stream<Method> methods,
                                        @Nullable Class<? extends Annotation> upgradeAnnotation,
                                        @Nullable Object... methodParams
    ) {
        if (upgradeAnnotation == null) {
            // setting default annotation
            upgradeAnnotation = UpgradePoint.class;
        }

        Class<? extends Annotation> finalUpgradeAnnotation = upgradeAnnotation;

        return methods
                // we only allow public methods
                .filter(method -> Modifier.isPublic(method.getModifiers()))

                // upgrade methods must be annotated with upgrade annotation
                .filter(method -> method.isAnnotationPresent(finalUpgradeAnnotation))

                // upgrade methods must not have any parameters
                .filter(method -> method.getParameterCount() == methodParams.length);
    }
}
