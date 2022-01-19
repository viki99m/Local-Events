# Local-Events

In the following I describe the functionality of the induvidial activities
### Main Activity
- save the current User
- initialize the Storage for Users 
- is link between Data and Presantation

### Login Activity 
 - Creates new Accounts or Login an existing User
 - Checks if a User already sign in with Google
 - Create a new User based on a Google Account
 
### EventList Activity
three differnt functions:
- Home screen (event_overview): list all activities of all users (no add symbol in the Menu, no editing of Events, only events that starts in future)
- MyEvents screen (my_events): list all activities of the user, which is signed in (add symbol in the Menu, allows editing of Events )
- Old Events(old_events): same like Home but list all events from the past
Also you can open here the Navigation Drawer Menu 

### Event Activity
- create a user, update a user and delete a user
- NumberPicker for costs, DatePicker for the date, Timepicker for the time (after chosen a time you see it on a text view)
- Button for select a image  (after chosen a image, you see it on an image view) therefore is the file ImagePicker
- Button for select a location (MapActivity starts, if you go back to Event Activity it will save the current location of the Marker)

### EventMaps Activity
- show a Map with all event locations 
- on clicking on one you see the information below
Also you can open here the Navigation Drawer Menu 

### Profile Activity
- show the current user 
- change current user data with the Button (same Button for save the Data)
- delete user 
- set DarkMode (is saved in the User object(so if you login again it will change))
Also you can open here the Navigation Drawer Menu 

### Maps Activity
- only for choose and show location 
- The Map Key for Google Maps is saved in the local.properties. It is not saved in GitHub

## Store
### User
differnt implementations (current implementation on the app is the Firebase(UserFirestore))
### Event 
differnt implementations (current implementation on the app is the EventJSONStore) therefore is the FileHelpers

## Layout
every Activity has a layout file, additionally: 
- card Event is for the EventListactivity 
- header is for the Navigation drawer
## Menu
- the default menu is the menu_main
- main_nav is for the Drawer Navigation 
- two different Menus for the Buttons in the Menu at My Events screen (EventListActivity) and for the EventActivity
