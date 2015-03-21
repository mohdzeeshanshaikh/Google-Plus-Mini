# GooglePlusMini
A minimalistic version of the Google+ android application.

##Author Details:
Mohd Zeeshan Shaikh <mohdzeeshan.shaikh@sjsu.edu>

##How to build this app
Download the apk (this is a signed apk) : GooglePlusMini.apk
In terminal, simple run: adb install path-to-apk (if installing on plugged-in device) 
or run adb -s emulator-5554 path-to-apk (if using avd)

##Introduction:
The Current google+ app offers a variety of features. However, if you simply only want to see all your friends from different circles in a simple list view, this app provide that simplicity and save time when you searching for people in your circles. It allows users to choose their accounts and view all the circles they have as well as the people in each of the circles.

##Userguide:
This version of GooglePlusMini is implemented based on Google+ Domains API. It uses oAuth 2.0 for authentication. Here only domains email id’s will be accepted.User of this app should install the signed apk file on the device. After installation, user should select domain email id to login. User will be redirected to a tabbed activity, one tab with the basic profile information screen and and the other with the list of your circles. By selecting the people from the circles, you will be able to view your friend's profile and have also the ability to email the selected person. A detailed flow of these steps are below:

##Functions included (and can be tested):
1. *SDK:* UI of the app renders properly on kitkat4.4 (API Level: 19) and tested the same with by installing the signed apk.
2. *Integration of G+ API:* Authentication against G+ API is doing using oAuth 2.0 and also integrated the domain API to fetch profile,circle, friends information.

##Basic screens:
1. *Login View:* This screen is rendered with g+ SignInButton. If the user has multiple accounts, an account chooser will be popped.
2. *User Profile View:* This screen shows the basic information of user, profile picture, name, aboutme, occupation and organization. The second tab, which upon selection takes the user to list of circles.
3. *Circle View:* A list of circles with circle name will be displayed. Upon selection of any of the circles, a list of people in that list is displayed along with picture and name of the person. 
4. *Friend Profile View:* Person’s profile which is been selected in the previous screen will be displayed. It is similar to user profile view, but with an Email Icon at the bottom. On click of the email icon, it shows various apps already installed on the device from which an email can be sent. 

###Navigation: 
A simple ancestral navigation is implemented which from friend profile view takes us back to list of friends. And with the layout implementation, In order to navigate back to profile screen we just need to swipe right to left.

_Apart from these, I've implemented a simple and attractive UI with center layouts and used custom launch icon._
