**PainlessUpgradeAPI** is universal library that helps support upgrading from old versions without headache.

It based on simple annotation system and can be changed using provided abstractions.

## Why I need that?

Imagine situation: you have some program with version v198. One day you're releasing v199, and it breaks something on user-side, 
e.g. some configuration section was renamed, or you made something with upgrading data to new style.

In this case, write a lot of legacy-support code is awful idea. You usually don't want to think about old versions and support code for migrate.

Using **PainlessUpgradeAPI** you can clearly and simple organize all your legacy-support code without checking old version with your hands.
Just leave this boring job to library by adding some simple annotations.

## What I need to work?

You need to store last version, you can do it as you want.

Example: 
1. Call upgrading tasks, using last version from file.
2. Write current version to file.

This is pretty simple, I'm sure you can handle it.

## Oh... How does it look?

Very simple example:

```java
@UpgradePoint("198-BETA")
public void upgradeFrom198() {
    database.clear();
}

public void launch() {
    new StringVersionUpgradeAPI().upgrade(
        this,
        "213-SNAPSHOT" // last enabled version
    );
}
```

Advanced example:
```java
@IntUpgradePoint({54, 55})
public void repairBrokenConfiguration(YamlConfiguration configuration) {
    configuration.set("broken-block", null);
    configuration.set("new-block", "some setting");
}

@IntUpgradePoint(96)
public void createNewSections(YamlConfiguration configuration) {
    configuration.setDefaults(getDefaults());
}

public void launch(YamlConfiguration configuration) {
    boolean configChanged = new IntVersionUpgradeAPI().upgrade(
        this,
        getLastVersion(),
        configuration
    );
    
    if (configChanged) {
        configuration.save();
    }
}
```