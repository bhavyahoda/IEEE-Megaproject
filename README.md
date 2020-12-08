# Sentry: 

1. Right now as a team of three we are committing sub parts of our project, which will be later on integrated with the final layout in the last week.

2. The API key has not been provided by us, if you want to use the project then clone it and then generate your own api key and use it.


This Project aims at providing users 

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
 
 2) **Register**: This activity takes the name age email password and phone number from the user uses email and password for authentication using FirebaseAuth 
 
 3) **Login**: In this activity the user can login using the email and password with which the user had registered. Authentication done with FirebaseAuth class.

 4) **HeatMap**: This show a map with the graphical representation of data values represented by colour showing the different coordinated where crime took place in New York using the crime data set from data.cityofnewyork.us The user can also enter his source and destination and will be provided with all the different routes he/she can take. If the user is a resident of the ney york city then he/she can decide which is the safest route to take. Routes shown using polylines and google maps direction api. Heatmap using the heatmap utility from the android utility library.
 
 5) **SOS**: In this activity we allow the user to add five contacts to which the user can send his location later on in any emergency situation these contacts are stored in cloud firestore which will always be overwritten if the user uses contact adder activity again. When the send button is clicked
