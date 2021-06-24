## Travel Planner
This app allows users to view their favourite hotels, sights, activities and restaurants and book them in the app. Users can skim through tours, locations, POIs as well as search different countries and receive custom intinerary’s for their trip.

This application is powered by [Triposo.com](https://www.triposo.com) .

## Installation
1. Install Android Studio. Please refer to the [Documentation Guide](https://developer.android.com/studio/install)
2. Download code
3. Run the project


## Configuration
### Triposo API
In order to run this project, you need to get an account id and token from [Triposo.com](https://www.triposo.com)

You need to set your account id and token in the TriposoAPI.java file as follows:

```
String account = "your account id"

String token = "your token"
```

### Firebase
You also need create a Firebase Project and download google-services.json in order to use Firebase Realtime Database and Authentication. Please refer to [Firebase Console](https://console.firebase.google.com)


## Features
- Can search 100 countries
-Users can add a country to the favourite page.
- A page for each point of interest helps you out to find everything.
- Users can book an event via Musement.com by activating bookable option.
-Users can show any point of interest in the map.
-The app summarises content about a point of interest.
-Users can receive a custom itinerary through the planner page.
-Users can an itinerary to the my trips page
-Users can personalise their experience by adding their interests in the settings page.
