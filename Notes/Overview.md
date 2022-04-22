# Overview
- An offline version of Ikitomo for Windows, Linux and macOS
- At least two different Tomodachis
    - The Tomodachi will from time to time follow the mause
    - Click the Tomodachi to feed it4
    - The Tomodachi will randomly take a nap
- A configuration format for the settings
- A configuration format for the tomodachis 
- A GUI for adjusting settings of the Tomodachi
- Sleep zone (when dragged there, ikitiomo will take a nap. Sleep zone can be configured)

## Einstellungen
- choose Tomodachi 
- Speed
- Activity Chances
  - sleep chance
  - wake up chance
- (select sleep zone by spawning a borderless window and see where the user draws a rectangle)

## Technisches
- State machine which controls animation and behavior
- Tomodachi Configuration
  - State machine
    - specific States (like eat, sleep) and their parameters (like the animation or chance)

## Ikitomo Configuration File
The settings from the settings ui are saved in a properties file.
The following snippet it an example for such a file.
```properties
```

## Tomodachi XML Format
The following XML is an example for a configuration of a Tomodachi
```xml
<?xml version="1.0" ?>
<tomodachi version="0.1">
    <config>
        <id>ch.codebrewery.name</id>
        <name>Name</name>
    </config>
    <states>
        <eat animation="src" chance="0.4"/>
        <sleep animation="src" chance="0.4"/>
    </states>
</tomodachi>
```
