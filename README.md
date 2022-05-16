# AndroidForCats

The app is aiming to provide an interesting platform for people looking to breed their cats, similar to the famous dating app. 
In this way, people can find a quick match for their pets without having to go through a difficult process and see the way the pet looks from the first moment.

The user should be able to sign up their pet and log in the profile. - Done

- users can sign up and be authenticated through Firebase

The user should be able to create a profile of the pet write a description and add photos. - Done

- users can upload a photo directly from the device and write a description and save it. Hobbies was not implemented and acts a sa placeholder.

The user should be able to view other listed pets in the area and approve or discard other potential matches. - Done

- users can see all users in the Firebase database and their location, regardless of how close or far away they are (no location services have been implemented)
- users can like or dislike another user


The user should be able to view the profiles that hit the like button back. - Done, but with a swipe instead of a button

- if both users have liked each other, they will get a toast notificaiton "Match" and will see the other user in the chat fragment

The user should be able to chat with the other user that liked back their pet. - Done

The user should be able to use the camera in the app to take a picture and send it to the chat. - Not done

#FireBase

FireBase Authentication FirebaseStorage were implemented successfully.

#PlaceHolderView

To create the tinder-like card layot, a customer library was implemented - https://github.com/janishar/PlaceHolderView
The library requred some classes and layots to retain the same names as the ones in Janishar's blog in order for the view to function properly.

LINK TO DEMO VIDEO - https://www.youtube.com/watch?v=O72NsAVk5T8&feature=youtu.be
