@REM Converts all aseprite files in the current directory to a spritesheet with json metadata
setlocal EnableDelayedExpansion
FOR %%f IN ("*.aseprite") DO (
    aseprite -b  "%%~nf.aseprite" --sheet "../sprites/%%~nf.png" --data "../sprites/%%~nf.json"
)