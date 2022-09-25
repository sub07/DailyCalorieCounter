call ./gradlew clean
call ./gradlew assembleRelease
call zipalign -v -p 4 .\app\build\outputs\apk\release\app-release-unsigned.apk .\app\build\outputs\apk\release\my-app-unsigned-aligned.apk
call apksigner sign --ks C:\Users\pardo\Documents\MEGA\keystores\keystore.jks --out .\app\build\outputs\apk\release\app-release.apk .\app\build\outputs\apk\release\my-app-unsigned-aligned.apk
call scp .\app\build\outputs\apk\release\app-release.apk mpardo@mpardo.dev:~/download/calorie.apk