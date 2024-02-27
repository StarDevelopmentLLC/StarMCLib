---
hide:
  - navigation
  - toc
---

# ObjectTester class
This is a developer only utility to allow seeing and changing values within an object.  
It uses some of the same parsing concepts as StarData, but this is on a much smaller scale than that library.

To get started, you must have a command registered in your plugin.yml of the same name as the class that you wish to model.  
Then you instantiate the command with the following
```java
ObjectCommand<TestObject> testObjectCmd = new ObjectCommand<>(this, TestObject.class, "test.test");
```
You pass in the plugin instance, the class of the type and a permission to limit access to it.  
Then you call the `register` method to register the command set things up.
```java
testObjectCmd.register();
``` 
You can find the object I am using [here](https://github.com/StarDevelopmentLLC/StarMCLib/blob/main/src/main/java/com/stardevllc/starmclib/objecttester/test/TestObject.java).  
I was debating removing it, but it is useful for testing this and for demonstration purposes.

Then it is all based on the commands.
```text
/<cmd> list <fields|methods>
/<cmd> list values [variableName] - If no variableName is provided, values for static fields will be listed. If one is provided, both static and instance values will be listed
/<cmd> get <fieldName> [variableName] - If no variableName is provided, you can only modify static fields
/<cmd> select <new|reset|selectorName> <variableName> [constructorParameters]
/<cmd> set <variableName> <fieldName> <value>
/<cmd> set <fieldName> <value>
/<cmd> call <variableName> <methodName> [methodParameters]
/<cmd> call <methodName> [methodParameters]
```
It will make it's best effort to find a method/constructor with the given parameters (if provided). All access modifiers are allowed so it does not matter if it is public. This will also recursively look at the class heirarchy for the provided class to find other fields and methods as well.  
You can modify `final` fields.  
You cannot modify constants (`static` AND `final` fields)

A `Selector` is a Functional Interface to allow selecting an object. Selectors get the CommandSender and the args after the context specific ones. These do start from index 0.  
A `TypeCodex` is a way to extend supported fields and parameters. This is a class that you can extend and then add using the `addCodec` method. There are default codecs if you want to take a look at them. 
