# Fixing JavaFX Module Issue in IntelliJ

If you encounter a problem where IntelliJ doesn't detect JavaFX Modules, you can follow these steps to resolve it:

## Steps:

1. **Download JavaFX Module**:
    - Visit [GluonHQ](https://gluonhq.com/products/javafx/) and download the required JavaFX module.

2. **Move the Downloaded File**:
    - After downloading, move the file to `C:\Program Files\Java`. If the directory doesn't exist, create a new one called `Java`.

3. **Extract Files**:
    - Extract the files from the downloaded `.zip` file into the `Java` folder.

4. **Add Library in IntelliJ**:
    - Open IntelliJ IDEA and navigate to `File > Project Structure > Libraries`.
    - Click the `+` button, then the `Java` button.
    - Locate the `javafx-sdk` folder where you extracted the files, open the `lib` folder, and click `OK`.

5. **Add JAR Files**:
    - Below "Classes," remove any existing files.
    - Click the `+` button, navigate back to the `lib` folder, and add all `.jar` files.

6. **Update Modules**:
    - Go to `File > Project Structure > Modules`.
    - If the `lib` module is added, click the checkbox on the right side and hit the `Apply` button.

7. **Rebuild Project**:
    - Finally, click on `Build > Rebuild Project`.

### Need Help?
If these steps didn't work, feel free to ping me on the group chat @Chriscent Pingol.
