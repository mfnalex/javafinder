# JavaFinder

<!--- Buttons start -->
<p align="center">
  <a href="https://hub.jeff-media.com/javadocs/com/jeff-media/javafinder/1.4">
    <img src="https://static.jeff-media.com/img/button_javadocs.png?3" alt="Javadocs">
  </a>
  <a href="https://discord.jeff-media.com/">
    <img src="https://static.jeff-media.com/img/button_discord.png?3" alt="Discord">
  </a>
  <a href="https://paypal.me/mfnalex">
    <img src="https://static.jeff-media.com/img/button_donate.png?3" alt="Donate">
  </a>
</p>
<!--- Buttons end -->

<p align="center">
<a href="https://central.sonatype.com/artifact/com.jeff-media/javafinder/"><img src="https://maven-badges.herokuapp.com/maven-central/com.jeff-media/javafinder/badge.svg" /></a>
<img src="https://img.shields.io/github/last-commit/jeff-media-gbr/javafinder" />
</p>

Finds installed Java versions on the system by looking through common paths on Windows, Linux, and macOS.

The returned List is sorted from newest to oldest Java version, with JDKs listed before JREs of the same version. When ran directly with java -jar, it prints the list to STDOUT, marking the currently running java version with an asterisk (*):

![image](https://github.com/JEFF-Media-GbR/javafinder/assets/1122571/688efa74-8e68-4819-83d4-9d5cb7ed3e5a)

## Maven
The dependency is available on Maven Central:
```xml
<dependencies>
    <dependency>
        <groupId>com.jeff-media</groupId>
        <artifactId>javafinder</artifactId>
        <version>1.4.4</version>
    </dependency>
</dependencies>
```

## Usage
Print to stdout from command line:
```sh
java -jar javafinder-1.4.4.jar
```

Or through Java code:
```java
Collection<JavaInstallation> installations = JavaFinder.builder().build().findInstallations()
```

## List of checked locations

#### Universal
- Parent folder of current $JAVA_HOME (unless that directory is the same as $HOME)
- $HOME/.sdkman/candidates/java

#### Windows
- Every folder in $ProgramFiles or $ProgramFiles(x86) containing "Java", "JDK", or "JRE" (case-insensitive)
- $ProgramFiles/Eclipse Foundation
- $ProgramFiles/Eclipse Adoptium

#### Linux
- /usr/lib/jvm

#### macOS
- /Library/Java/JavaVirtualMachines
- /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
- $HOME/Library/Java/JavaVirtualMachines
