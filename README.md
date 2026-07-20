# Kelp

A framework for building Paper plugins with structured commands, DI, and lifecycle management.

> **Requires Paper 26.2** Compatibility with other versions is not guaranteed yet.
> **Kotlin only for now.** Java support is not yet available.

## Getting Started

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.juliohaga</groupId>
    <artifactId>Kelp</artifactId>
    <version>{version}</version>
</dependency>
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.juliohaga:Kelp:{version}")
}
```

Replace `{version}` with a tag from the [Releases page](https://github.com/juliohaga/Kelp/releases).

### Example

```kotlin
@Component
@Command("hello", "kelp.hello")
class HelloCommand : KelpCommand() {

    @SubCommand("", "kelp.world.create")
    fun hello() {
        player.sendMessage("Hello!")
    }

    @SubCommand("world", "kelp.hello.world")
    @PlayerOnly
    fun helloworld() {
        player.sendMessage("Hello, World!")
    }
}
```
