# Petbooking Android

### Project Structure Folder
The application is organized by feature in following package structure inside com.beautydate package:

+ **_API_**:  Contains all classes and interfaces that is use to communicate with rest service.
+ **_Components_**: Contains all app interface components used in many places.
+ **_Interfaces_**: Contains all interfaces used in the app as Callbacks and UI Responses.
+ **_Constants_**: Constants used in many places of app grouped by SharedPreferences, Analytics and General constants.
+ **_Manager_**: Classes with a strict defined responsability like obtain User Location, control pending reviews, link device to receive push notification, control authentication process.
+ **_Models_**: Contains all models that represents the rest response.
+ **_UI_**: Group all classes that control app interfaces presented to user.
+ **_Utils_** - Utilities classes used in many places.
 
> Each UI package has Android UI component like Activity/Fragment. Adapters, Holder or another file are inside when exists.

### Patterns
+ **_API_** layer is done by:
  - Volley as *Rest Service* manager.
  - GSON as *Json Parser*.
+ **_Variables_** names follow this pattern: `m` + `Elem Abreviation` + `Variable Name`;
  - For example, a variable for show an user email (TextView) has the following name: `mTvEmail`.
+ **_Strings_** files are into languague folder: values-br, values-en. 
+ **_XML_** files are:
  - Activity follow activity_name.xml
  - Fragment follow fragment_name.xml
  - Dialog follow dialog_name.xml
  - Views for list follow item_list_name.xml
  - Views for include follow include_name.xml
  - Views for widget follow widget_name.xml
 
### Libraries
The application uses the libraries below:
+ [Retrofit](http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
+ [Glide](https://github.com/bumptech/glide) - Glide is a fast and efficient open source media management and image loading framework for Android.
+ [GSON](https://github.com/google/gson) - A Java serialization/deserialization library that can convert Java Objects into JSON and back.
+ [Facebook](https://developers.facebook.com/docs/android/) - SDK to Use Facebook API.
+ [Eventbus](http://greenrobot.org/eventbus/) - EventBus is an open-source library for Android using the publisher/subscriber pattern for loose coupling.
+ [FabricIO](https://get.fabric.io/) - Fabric is a platform that helps your mobile team build better apps, understand your users, and grow your business.

