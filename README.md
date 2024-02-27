# StarMCLib
This is the Minecraft based library that has utilities and things specific to Minecraft but doesn't need a plugin to run.  
[![](https://jitpack.io/v/StarDevelopmentLLC/StarMCLib.svg)](https://jitpack.io/#StarDevelopmentLLC/StarMCLib)
## To use this Library
You must add JitPack as a repo, below is for Gradle  
```groovy
repositories {
    maven {
        url = 'https://www.jitpack.io'
    }
}
```  
Then to use this library as a dependency  
```goovy
dependencies {
    implementation 'com.github.StarDevelopmentLLC:StarMCLib:1.0.0-alpha.9'
}
```  
Please note that this library requires StarLib and XSeries.  
You must shade this library in order to properly use it, or have it already on the class-path. Gradle has the Shadow Plugin you can use for this task.  
If you are using Maven, the Maven-Shade plugin works just fine.
