GUI BACKUP - 2026-11-23
=======================

This folder contains a snapshot of the Flux ClickGUI code before the
modern/minimal GUI pass. If anything looks wrong after the changes, copy these
files back over their matching paths in the project root.

Included:
  src/main/java/com/yourname/utilmod/ui/*          (all GUI screens/helpers)
  src/main/java/com/yourname/utilmod/module/impl/GuiThemeModule.java
  src/main/java/com/yourname/utilmod/module/Category.java
  src/main/java/com/yourname/utilmod/util/ConfigStore.java
  src/main/resources/assets/flux/font/*            (fonts + TTF tiers)
  src/main/resources/utilmod.mixins.json

Restore example (PowerShell, from the project root):

  Copy-Item -Path "gui-backup-2026-11-23\src\main" -Destination "." -Recurse -Force

Then rebuild:

  .\gradlew build --offline --console=plain

Note: this is a plain file backup, not a git commit. The project is not a git
repository.
