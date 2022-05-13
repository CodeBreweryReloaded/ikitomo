@REM Converts all aseprite files in the current directory to a spritesheet with json metadata
setlocal EnableDelayedExpansion
FOR %%f IN ("aseprite/*.aseprite") DO (
    aseprite -b  "aseprite/%%~nf.aseprite" --palette "palettes/tubby.aseprite" --sheet "export/tubby/%%~nf.png" --sheet-type packed --data "export/tubby/%%~nf.json" --format json-array --filename-format {title}_{frame}
    aseprite -b  "aseprite/%%~nf.aseprite" --palette "palettes/garfield.aseprite" --sheet "export/garfella/%%~nf.png" --sheet-type packed --data "export/garfella/%%~nf.json" --format json-array --filename-format {title}_{frame}
)