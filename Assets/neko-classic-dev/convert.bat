@REM Converts all aseprite files in the current directory to a spritesheet with json metadata
setlocal EnableDelayedExpansion
FOR %%f IN ("aseprite/*.aseprite") DO (
    aseprite -b  "aseprite/%%~nf.aseprite" --palette "palettes/classic.aseprite" --sheet "export/classic/%%~nf.png" --sheet-type packed --data "export/classic/%%~nf.json" --format json-array --filename-format {title}_{frame}
    aseprite -b  "aseprite/%%~nf.aseprite" --palette "palettes/strawberry.aseprite" --sheet "export/strawberry/%%~nf.png" --sheet-type packed --data "export/strawberry/%%~nf.json" --format json-array --filename-format {title}_{frame}
    aseprite -b  "aseprite/%%~nf.aseprite" --palette "palettes/negative.aseprite" --sheet "export/negative/%%~nf.png" --sheet-type packed --data "export/negative/%%~nf.json" --format json-array --filename-format {title}_{frame}
)