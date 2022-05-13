@REM Converts all aseprite files in the current directory to a spritesheet with json metadata
setlocal EnableDelayedExpansion
FOR %%f IN ("aseprite/*.aseprite") DO (
    echo aseprite -b  "aseprite/%%~nf.aseprite" --sheet "tubby/%%~nf.png" --sheet-type packed --data "tubby/%%~nf.json" --format json-array --filename-format {title}_{frame}
)