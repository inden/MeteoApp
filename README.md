# (Demo) NewSunshine Android #
a 1 and 7 days weather forecast Android App.
The weather data is provided by the Open API http://api.openweathermap.org.

I'have developed an app with similar fonctionalities but with more elegant architecture. Find it here :
https://github.com/inden/OpenWeatherMap


## How to use ##

## Today's forecast per city ##
Weather forecast of your favorite cities.

<img src="https://cloud.githubusercontent.com/assets/21304543/20180235/f66c5e78-a759-11e6-8567-301d8885273a.png" width="400"/>


## 7 days' forecast of a selected city  ##
Click a city. The list is expanded and display 7 days's forecast.

<img src="https://cloud.githubusercontent.com/assets/21304543/20180236/f675c6f2-a759-11e6-97fe-fddea8034847.png" width="400"/>


## Weather forecast with the detail ##
Click a day and it displays the detail of the selected day.
Click *MAP* to see the city on a map. An implicit intent calls a Map app on device.

<img src="https://cloud.githubusercontent.com/assets/21304543/20180237/f677279a-a759-11e6-93dd-dd9aa68eff03.png" width="400"/>


## Setting ##
City setting to manage your favorite cities.


<img src="https://cloud.githubusercontent.com/assets/21304543/20180241/f68cfc32-a759-11e6-813b-612b49a0ce67.png" width="400"/>


##  Language Setting ##
You can make a choice of a different language from the device's setting. T
The default value is the device's one.


<img src="https://cloud.githubusercontent.com/assets/21304543/20180239/f6812ee8-a759-11e6-8abe-13d20313ca3f.png" width="400"/>


## Synchronization ##
The app try to synchronize the data with the OpenweatherMap API
- when it is launched
- when a new city is added by city setting 
- every 3 hours with synchAdapter (you can change the interval)



## SDK API 19 or later required ##

## Technology ##
- Activity / Fragments
- Service
- ExpandableList
- CursorAdapter
- CursorLoader
- ContentProvider
- SQLite
- SynchAdapter
- Intent (Implicit and Explicit)
- JSon (Gson)
- Async task
- URIConnection


