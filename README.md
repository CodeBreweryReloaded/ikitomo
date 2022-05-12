# Iikitomo

## Architecture Documentation (0.5 - 1 A4 Page)

**TODO: Insert class diagram**

One of our key design decisions was to have a two staged model. Classes, like `Settings` or `TomodachiDefinition` are very close to the JSON structure in the file and are used to load and save JSON data from and to the disk. Those classes don't load any additional resources, like animations or icons. For this we implemented a second stage model, `TomdachiModel`. A `TomodachiDefinition` object gets transformed into a `TomodachiModel`. In this transformation the animations are actually loaded. There are also some additional properties needed by the `OverlayController` which actually uses the `TomodachiModel`.  We've chosen this design to avoid loading all animations at the start of the application, as this could potently take a long time and use an increased amount of memory.

An other decision with the model is a consequence of how we wanted the settings pane to work. When the user changes a value, they should instantly see the result. Because of this information need to flow from the `SettingsController` to the `OverlayController`. This was done using properties and bindings. When a setting is adjusted in the `SettingsController` the `OverlayController` is notified via the binding or property. 

The behavior of the Tomodachis is implemented in a separate class `TomodachiBehavior`. It controls the animation and position of the currently displayed Tomodachi and gets recreated every time the user changes the selected Tomodachi. The `TomodachiBehavior` class uses its own model `BehaviorModel`. It allows the behavior class to modify the animation and position of the Tomodachi. It communicates with the other classes via properties, bindings and some listener/callbacks. We created the model in the first place, instead of using the `TomodachiModel`, to reduce the interface between the `TomodachiBehavior` and the `OverlayController`. This makes it a lot easier to mock test the behavior as there is less API surface to mock.

Every controller implements an interface named `Killable` which has one method `CompletableFuture<Void> kill()`. This is used to close the application and disposing all resources of every controller. Because of this, we can dispose the tray icon and swing frames quit elegantly.

## Notable Pull-Requests

The following pull requests represent our typical PRs:

1. Add Classes for Serializing Settings [#18](https://github.zhaw.ch/PM2-IT21bWIN-ruiz-mach-krea/team03-codebrewery-projekt2-ikitomo/pull/18)
2. Add log configuration [#21](https://github.zhaw.ch/PM2-IT21bWIN-ruiz-mach-krea/team03-codebrewery-projekt2-ikitomo/pull/21)

## Branching Model

Our default branch is `main`. All changes have to be made via a pull request after the initial commits.
For every task there is an issue. If somebody starts working on an issue, they're assigned 
to the issue and will create a branch for all changes. The branch name has to be a short description of the 
issue that it fixes. On how to write commit message see clean code rules. An pull request has to be linked to an
issue. This can either be done through mentioning the issue number in the commit message or by setting it manually
in the web GUI. A branch may only be merged into `main` if a pull request exists and has been approved by at least one other person.
