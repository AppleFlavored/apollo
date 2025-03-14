# Apollo for Minestom
This is an implementation of Lunar Client's [Apollo API](https://github.com/LunarClient/Apollo) for Minestom.

## Usage
Add the following repositories to your `build.gradle.kts` file:
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.lunarclient.dev")
}
```

Add the following dependency to your `build.gradle.kts` file:
```kotlin
dependencies {
    implementation("dev.flavored:apollo:0.1.0")
}
```

Finally, initialize Apollo:
```java
ApolloMinestomPlatform.init();
```

## Outdated Version?
This library is not uploaded regularly. Please [create an issue](https://github.com/AppleFlavored/apollo/issues)
if you wish for me to update this library to the latest version.

## License
The source code in this repository is licensed under the MIT license. See [LICENSE](LICENSE) for more details.
