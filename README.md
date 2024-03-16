[![](https://jitpack.io/v/Matin2/ExtraCommands.svg)](https://jitpack.io/#Matin2/ExtraCommands)

## API Usage Explained
Include the API using Gradle(Groovy):
```groovy
repositories {
	maven { url "https://jitpack.io" }
}
dependencies {
    compileOnly "com.github.Matin2:ExtraCommands:version"
}
```
Include the API using Gradle(Kotlin):
```kotlin
repositories {
	maven("https://jitpack.io")
}
dependencies {
    compileOnly("com.github.Matin2:ExtraCommands:version")
}
```

Include the API using Maven:
```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Matin2</groupId>
        <artifactId>ExtraCommands</artifactId>
        <version>version</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
