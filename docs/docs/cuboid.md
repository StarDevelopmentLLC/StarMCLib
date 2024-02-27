---
hide:
  - navigation
  - toc
---

# Cuboid Class
This class is intended to be used with small areas and is not optimised nor intended to be used with large areas. For that, please use a plugin like WorldEdit/FastAsyncWorldEdit. These have a pretty good API and I have used it in the past. This is just to prevent having to depend directly on those for small use cases.

A cuboid is just that, a cuboid. You provide either two locations in the same world or x, y, z for the min and max. The class will determine the minimums and maximums, so you can mostly be lazy about which order you put them in.  
This class does operate on doubles and integers for a lot of the math based functions, so it is pretty fast when it comes to that, however it is slow when trying to get things as Bukkit Locations. In small cases, this is negligible, but when it comes to large areas, then it can really eat up resources. For that, there are better plugins to handle that.
```java
Cuboid cuboid = new Cuboid(location1, location2);
```