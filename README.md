# JavaFinder

<!--- Buttons start -->
<p align="center">
  <a href="https://repo.jeff-media.com/javadoc/public/com/jeff_media/javafinder/1.0-SNAPSHOT">
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
<a href="https://repo.jeff-media.com/#/public/com/jeff_media/javafinder">
  <img src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.jeff-media.com%2Fpublic%2Fcom%2Fjeff_media%2Fjavafinder%2Fmaven-metadata.xml" alt="Maven" /></a>
<img src="https://img.shields.io/github/last-commit/jeff-media-gbr/javafinder" />
</p>

Finds installed Java versions on the system by looking through common paths on Windows, Linux, and macOS. The returned List is sorted from newest to oldest Java version, with JDKs coming JREs of the same version.

![image](https://github.com/JEFF-Media-GbR/javafinder/assets/1122571/975eb622-f821-4225-946b-41e6e55338b1)


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
