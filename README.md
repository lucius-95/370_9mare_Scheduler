# CMPT370-9MARE

Software Engineering Project for CMPT370 Winter 2022
Designed by Dillon, Lucius, Nam, Kyle, and Abdul

## Installation Instructions
1. Download the latest version of AndroidStudio (installation steps for IDE can be found at https://developer.android.com/studio/install)
2. Within the installer, on the "Choose Components" section ensure that under the "Select components to install" selection box has the component "Android Virtual Device" checked.
3. Unless explicitly required by the user, all other options should remain as the recommended parameters. (File location, shortcuts, etc.)
4. Upon completion of installation and the first run of the IDE, a Setup Wizard should appear for IDE configurations.
5. Follow all recommended steps of the Setup Wizard in installing and configuring the necessary components. Read and accept the terms and conditions.
6. Click on Finish and wait for all SDK components to install.
7. Upon completion, download or clone the app into a folder, preferably AndroidStudios preset directory "/StudioProjects".
8. Open the app within AndroidStudio using the Open/Open Project and navigating to the directory where the app resides.
9. Navigate and click on "Tools > Device Manager". A tool window should appear to the right side of the IDE.
10. Find and click "Create Device" a pop-up should appear listing several android devices. Any android phone may be used for running and testing the app, however, most testing was done using "Pixel 3 XL". Upon selecting a device click "Next".
11. A selection of system images will appear, navigate to the "Q" system image and click on the "Download" link to the right of its name to download the system image.
12. Upon completion of installation of the system image select "Q" as the system image and click "Next" followed by "Finish".
13. With the app, IDE, and virtual device installed everything can be ran from within the IDE using the virtual device.

# Running the app
14. In order to run the app the shortcut Shift+F10 can be used, or by navigating to "Run > Run 'app'".

# Testing the app
15. In order to run the instrumental tests, ensure that the data on the virtual device is wiped and that animations are disabled.
16. To wipe the data, first the device must be shutdown. This can be done by opening the "Emulator" window (right side navigation bar of the IDE) and shutting down the device by navigating to the top of the emulator window in which to the right of the "Emulators:" an "X" can be clicked beside the names of the currently running virtual devices.
17. After waiting a couple of seconds the "Device Manager" window can be opened which displays all installed emulators. Under the Actions column select the downwards pointing arrow and click on "Wipe Data". Confirm that you want to wipe all data.
18. Upon wiping the data run the emulator again by clicking the run button also under the Actions column.
19. Navigate to the "Emulator" window and wait for the virtual device to run.
20. Within the virtual device, holding left-click at the top of the screen, swipe down to show a list of notifications and settings.
21. Within the first settings window, holding left-click at the bottom of the window, swipe down to show more settings.
22. Look for a cogwheel in the bottom right of the window and click on it to show the virtual devices System Settings.
23. In the search bar at the top search for "Animations", click on the first result and switch on the "Remove animations".
24. Upon wiping the data and turning of animations, you can navigate to and click on the "Project" window (left side navigation bar of the IDE).
25. Navigation in to the "/java" directory and right click the directory "/ca.nomosnow.cmpt370_9mare (androidTest)" selecting "Run 'Tests in ...'".
26. NOTE: If encountering issues in either running or testing the app, consult the Demo Video for further explanation.
27. ADDITION: All instrumental tests are performance related. In some cases where the app is overloaded with information or lag is induced by the user through background processes then errors may occur and tests may fail. Please restart and re-wipe data and re-attempt tests if encountering this issue.
28. ADVISE: When performing the test suites, it is recommended to execute only one test suite at a time to reduce the risk of failure due to performance related issues.

## Gradle Commands (For running the app from a terminal window)

## How to build the app:
./gradlew clean build

## How to Test:
# Unit Test
./gradlew app:test

# Debug test
./gradlew -Pci --console=plain :app:testDebug

# Lint Debug
./gradlew -Pci --console=plain :app:lintDebug -PbuildDir=lint

# Instrumental test
./gradlew connectedAndroidTest


