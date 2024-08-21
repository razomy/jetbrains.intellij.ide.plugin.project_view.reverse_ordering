# Split Reverse Order

Plugin for JetBrains IDE

### It splits all filenames by `_`, `-`, `.`, [space] and (?=[A-Z]), then orders them in the opposite way.

## Use Cases

Python file naming with nesting [child_parent]:

```shell
string.py
bound_string.py
get_xy_bound_string.py
get_xyz_bound_string.py
```

Or in C#:

```shell
Cn.cs
MultiplayerDeviceCn.cs
UserMultiplayerDeviceCn.cs
UserCn.cs
AchievementUserCn.cs
AchievementsUserCn.cs
ChapterUserCn.cs
ChaptersUserCn.cs
FinishUserCn.cs
InfinityUserCn.cs
```

Angular project, to view elements in groups:

```shell
config.ts
config.service.ts
timeline.service.ts
table-timeline.service.ts
config.model.ts
timeline.model.ts
table-timeline.model.ts
config.pipe.ts
timeline.pipe.ts
table-timeline.pipe.ts
```

Ordered elements:

```shell
documents-1.md
specification-2.md
development-3.md
products-4.md
```

Or:

```shell
summer-12-01-2023.png
dog-18-12-2023.png
landing-17-12-2023.png
cat-14-02-2024.png
```

### How to Use It

1. Build
2. Install the plugin
3. In Project, select `ReverseOrderProject`
4. In Sort, select "Select by Type"

### Feel free to extend and update.

### References

Discussion: [https://youtrack.jetbrains.com/issue/IJPL-160446](https://youtrack.jetbrains.com/issue/IJPL-160446)

Github: [https://github.com/razomy/jetbrains.intellij.ide.plugin.project_view.reverse_ordering](https://github.com/razomy/jetbrains.intellij.ide.plugin.project_view.reverse_ordering)

Plugin: [https://plugins.jetbrains.com/plugin/25161-project-view-reverse-ordering](https://plugins.jetbrains.com/plugin/25161-project-view-reverse-ordering)
