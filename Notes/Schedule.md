# Schedule
```mermaid
gantt
    title Ikitomo
    dateFormat YYYY-MM-DD
    axisFormat %d.%m.%Y
    section Preparation
        Class Diagram               : cd, 2022-04-22, 1d
        Schedule                    : sd, 2022-04-23, 1d
    section Foundation
        [04] Common Package         : 04, 2022-04-24, 1d
        [01] Public API             : 01, 2022-04-24, 1d
        [11] Dummy Sprites          : 11, 2022-04-24, 1d
        [07] Tomodachi GUI          : 07, after 04, 7d
        [09] Tray Icon              : 09, after 06, 1d
    section Features
        [05] Settings Serialization : 05, after 04, 7d
        [08] Behaviors              : 08, after 07, 7d
        [10] Final Sprites          : 10, 2022-04-30, 7d
    section Interaction
        [06] Settings GUI           : 06, after 05, 3d
    section Development Environment
        [02] Linting Integration    : 02, 2022-04-24, 1d
        [03] Automated Testing      : 03, 2022-04-24, 1d
    section Buffer
        Buffer                      : buffer, after 01 02 03 04 05 06 07 08 09 10 11, 2022-05-13
```

## Responsibilities
  - Class Diagram: @zumbrseb
  - Schedule: @costajon, @thalmma5, @zieglmic, @zumbrseb
  - Common Package: @zumbrseb
  - Public API: @zumbrseb
  - Settings Serialization: @thalmma5
