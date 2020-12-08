# Sentry: 

1. Right now as a team of three we are committing sub parts of our project, which will be later on integrated with the final layout in the last week.

2. The API key has not been provided by us, if you want to use the project then clone it and then generate your own api key and use it.


  This app aims towards providing users help maintaing their **safety and security**. It tries to deal with the issue of unsafe travelling by providing routes so that the user can choose the safest one, deal with any emergency situation a user might have by sending their location information to added contacts, not knowing about the nearest emergency locations like police stations, hospitals and fire stations. It also provides the users with information about different IPC sections of the constitution because sometimes many people do not even know that they were a victim of some crime. With the information provided by the app atleast people will be aware to avoid any such situation or take action against it. Also provides information about self defence classes, articles for counselling, videos for counselling, hotel information. Thus, making people more aware of the safety issues and also information to take action against it.

## Technologies Used:

<ol>
  <li>A Java/Kotlin based Android App as the frontend</li>
  <li>Google Firebase Cloud Firestore</li>
  <li>Google Maps SDK for Android</li>
  <li>Google Maps Directions API</li>
  <li>Google Maps Places API</li>
</ol>

## The App:

## Different Activities in the app:

 1) **Main**: This is the first intent of the app which provides the user with two buttons login and register.
 
 2) **Register**: This activity takes the name, age, email, password and phone number from the user and uses email and password for authentication using FirebaseAuth. 
 
 3) **Login**: In this activity the user can login using the email and password with which the user had registered. Authentication done with FirebaseAuth class.

 4) **HeatMap**: This show a map with the graphical visualisation of data values represented by colour showing the different coordinates where crime took place in New York using the crime data set from <a href="https://data.cityofnewyork.us"><b><ins>data.cityofnewyork.us</ins></b></a>The user can also enter the source and destination and will be provided with all the different routes he/she can take. If the user is a resident of the New York City then he/she can decide which is the safest route to take. Routes shown using polylines and google maps direction API. A swapper button has also been provided with the help of which the user can add or remove the heatmap as per his/her wish. The user can also search for the origin and destination which provides autocomplete feature using google maps places API. 
 
 5) **SOS**: In this activity we allow the user to add five contacts to which the user can send his location later on in any emergency situation these contacts are stored in cloud firestore. When the send button is clicked the app shows the user his current location on a map and sends a call for help to contacts which the user had added with the users current latitude and longitude. The message was sent by using the SmsManager class.
 
 6) **Nearby Places**: When this activity is activated then it shows the user his/her current location with a marker. Now, the user can drag the marker to any other location and it will display a route from the user's current location with the help of a polyline and also the duration of travel. The app adds markers to location having hospitals, police stations, fire stations in 5 kilometer radius of your current location as per the user's choice. When the user clicks any of the marker then he/ she is also provided the information about the particular place and its rating in a card view.
 
7) **Information Section**: This section provides information for the users so that either they can be safe or take action if a crime has been committed.
          <br/>**i)** The law info activity consists of various IPC sections focussed on crime and criminal activity so that the users are well informed.
          <br/>**ii)** The hotel info activity consists of various hotels and the ratings of each hotel. Anyone can even register their own hotel.
          <br/>**iii)** A self defence section which provides with information, links and locations of different self defence classes online and offline. Any user can even register his own class.
          <br/>**iv)** The section for counselling awareness provides with information about classes for counselling and also uploaded articles by different users or register for their own classes for counselling.
          <br/>**v)** The Anonymous tips section is a place for users to report any crime which they might have witnessed anonymously.

