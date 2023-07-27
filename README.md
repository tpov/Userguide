# Userguide
UserGuide Library
The UserGuide library is designed to simplify the use of applications for users. Its functionality is based on drawing a point on an element and displaying a dialog box that slides out from below.

The library provides functions to easily add guides to view elements, display dialogs, and show information about the guides that the user has seen.


## Integration

- To integrate the UserGuide library into your project, add the following dependency to your project's build.gradle file:

```implementation 'com.github.tpov:Userguide:1.4.2'```

## Usage

- Create an instance of the UserGuide class, providing the application context and an optional theme for the dialog windows:

```
val userGuide = UserGuide(context, theme)
```
- Add guides to view elements using the addGuide method. This method allows you to specify the view, text, title, icon, video, callback, and other options for the guide:
```
userGuide.addGuide(view, text, titleText, icon, video, callback, *generalViews, options)
```
- Display only a dot without showing the dialog window using the addOnlyDot method:
```
userGuide.addOnlyDot(view)
```
- Add a guide with a new version using the addGuideNewVersion method. This method shows the dialog immediately and is called only once by default:
```
userGuide.addGuideNewVersion(text, titleText, icon, video, options)
```
- Show a notification dialog with options using the addNotification method:
```
userGuide.addNotification(id, text, options, titleText, icon, video)
```
- Show a fragment with information about all the guides the user has seen using the showInfoFragment method. Provide the fragment manager to display the fragment:
```
userGuide.showInfoFragment(text, fragmentManager)
```
### Manage the counter: 

You can set the counter value using the setCounterValue method. The counter is used to display guides with countKey greater than or equal to the counter value:
```
userGuide.setCounterValue()
```
You can also set a specific value for the counter

![image_2023-07-10_13-08-08](https://github.com/tpov/Userguide/assets/33009369/e59080dd-68ad-452e-9848-ef7557e8d002)
![image_2023-07-10_13-36-00](https://github.com/tpov/Userguide/assets/33009369/1dfd723d-53e3-4760-814b-a2c8423148b5)
