@echo off

cls

cd %~dp0

echo.
echo. ## Cleaning up dist folder...
rmdir /s /q "dist"

echo.
echo. ## Copying resources...
xcopy "lib" "dist/lib" /s /e /i

echo.
echo. ## Packing...
"C:/Program Files (x86)/Launch4j/Launch4jc.exe" "launch4j.xml"

echo.
echo. ## Zipping...
"C:/Program Files/7-Zip/7z" a -tzip "dist/carnecarne.zip" "./dist/*"