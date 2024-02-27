---
hide:
  - navigation
  - toc
---

# Position class
This class is just a simplified version of the `Location` class as it does not have the World reference.  
There are methods to convert from and to Locations though.
Lets create one like that
```java
Position position = Position.fromLocation(player.getLocation());
```
And we can convert it back as well.
```java
Location location = position.toLocation(player.getWorld());
```
These aren't good use cases for this though and is just showing you the parameters.  
You can also use the constructors, there are two, one that takes in 3 doubles as x, y, and z and the other that takes in 3 doubles and 2 floats: x, y, z, yaw, pitch.