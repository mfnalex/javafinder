# JavaFinder
Finds installed Java versions on the system by looking through common paths on Windows, Linux, and macOS. The returned List is sorted from oldest to newest, and JREs coming before JDKs.

![image](https://github.com/JEFF-Media-GbR/javafinder/assets/1122571/7c97679c-a011-479b-8a6e-cc37f9e2997f)

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

## List of checked locations

#### Universal
- Parent folder of current $JAVA_HOME
- $HOME/.sdkman/candidates/java

#### Windows
- $ProgramFiles/Java
- $ProgramFiles(x86)/Java

#### Linux
- /usr/lib/jvm

#### macOS
- /Library/Java/JavaVirtualMachines
- /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
