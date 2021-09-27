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

After releasing v199, I want to clear the database.

```java
@UpgradePoint("198")
public void upgradeFrom198() {
    database.clear();
}
```

Or... I want to clear database after **versions before 159** and **versions after 176**.

```java
@UpgradePoint("159")
@UpgradePolicy(Policy.BEFORE)
public void upgradeBefore159() {
    database.clear();
}

@UpgradePoint("176")
@UpgradePolicy(Policy.AFTER)
public void upgradeAfter176() {
    database.clear();
}
```

Looks simple, yea? How about multiply versions?

```java
@UpgradePoint("190", "197", "198")
public void upgradeFromSomeBadVersions() {
    database.clear();
}
```

And we have some additional policy to various solutions:
```java
@UpgradePoint({})
@UpgradePolicy(Policy.ALWAYS)
public void alwaysUpgrade() {
    System.out.println("You will always see me")
}

@UpgradePoint({})
@UpgradePolicy(Policy.NEVER)
public void neverUpgrade() {
    System.out.println("We'll never meet")
}
```
