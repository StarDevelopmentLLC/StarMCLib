---
hide:
  - navigation
  - toc
---

# Home
Welcome to the StarMCLib wiki! This wiki contains information that you need to know how to use this library.
Each page is dedicated to a class or a system, please take a look at the page(s) that are most relevent to what you what to know. There is very little cross-dependecies within this library.
This library does depend on StarLib. Please make sure that also add that as the dependency.

## Choosing a Build Tool
The two primary build tools for Java are Maven and Gradle. Pick one and stick with it.

## JitPack Repository
Star Development LLC uses JitPack as the repository for development. You must have this repository in whichever build tool you wish to use.
### Maven
```xml 
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
### Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

## Installation
The first thing you need to determine is the version of StarMCLib that you are using.  
Go to the [releases](https://github.com/StarDevelopmentLLC/StarMCLib/releases/) page on the GitHub and look for the `latest`.  
The release description will have which version(s) of StarLib work with StarMCLib. If no version of StarLib is listed, the latest version of StarLib is assumed.  

## StarLib
StarLib is an essential library used within StarClock, however, it does not package it by default, this is to allow compatibility with other projects as to not have too many duplicate dependencies.  
Please ensure you are installing the correct version of StarLib for the version of StarClock that you are using, see above for how to find this information.  
**Replace `{VERSION}` with the version of StarLib you are using!**
### Maven
```xml
<dependency>
    <groupId>com.github.StarDevelopmentLLC</groupId>
    <artifactId>StarLib</artifactId>
    <version>{VERSION}</version>
</dependency>
```
### Gradle
```groovy
dependencies {
    implementation 'com.github.StarDevelopmentLLC:StarLib:{VERSION}'
}
```
## StarMCLib
### Maven
```xml
<dependency>
    <groupId>com.github.StarDevelopmentLLC</groupId>
    <artifactId>StarMCLib</artifactId>
    <version>{VERSION}</version>
</dependency>
```
### Gradle
```groovy
dependencies {
    implementation 'com.github.StarDevelopmentLLC:StarMCLib:{VERSION}'
}
```