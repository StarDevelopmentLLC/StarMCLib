---
hide:
  - navigation
  - toc
---

# ServerProperties class
This class allows you to read the values found in the `server.properties` file. It does not allow you to write to this file though as that can be dangerous and you still must restart the server for changes to take effect anyways. I did at one point have it being able to write to it, but I ran into some issues and problems, so I removed that functionality when I imported it to this project.

I have found it very useful when trying to get the main world name as you can never be sure about the return of the other ways to get the main world. 