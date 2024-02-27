---
hide:
  - navigation
  - toc
---

# StarThread class
Note: I may move this to StarLib in the future, I will keep it here for a few versions after that though.

This class is just a wrapper for the `BukkitRunnable` that adds performance runtime metrics. You also supply the `ThreadOptions` and then call `start` to start the thread. 