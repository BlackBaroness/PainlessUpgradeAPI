package ru.baronessdev.lib.painlessupgrade.common.annotations;

import org.jetbrains.annotations.NotNull;
import ru.baronessdev.lib.painlessupgrade.common.Policy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpgradePolicy {
    @NotNull Policy value();
}
