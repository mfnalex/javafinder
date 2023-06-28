## Maven
```xml

<repositories>
    <repository>
        <id>jeff-media-public</id>
        <name>JEFF Media GbR Repository</name>
        <url>https://repo.jeff-media.com/public</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.jeff_media</groupId>
        <artifactId>javafinder</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Usage
```java
Collection<JavaInstallation> installations = JavaFinder.findJavaInstallations();
```