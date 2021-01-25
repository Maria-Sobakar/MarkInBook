# MarkInBook

MarkInBook - is a mobile school diary.
This project was creating in cooperation with my teacher which made a remote server and site. This app can be used without conection to server , but it will have less
capabilities in this case. During creation I used REST API to connect to remote server.

Remote server:
https://github.com/diagorus/markinbook-backend/tree/master

Site: https://github.com/diagorus/markinbook-teacher


## Capabilities
 
 ### With conection to remote server
 - SignIn/SignUp/continue without registration and use app without conection to remote server ( with option to SignUp later)
 - see your profile (if internet conection is available)
 - add a profile photo from any other app
 - recieve a notification about adding new homework/new lesson mark/new homework mark
 - add a local lesson ( available for user only)
 - all your scheduled lessons (local and whichadded by your teacher) in one list with sorting by day and time
 - see schedule for current, next and previous week
 - all lessons (local added by your teacher in one schedule
 - see a homework
 
 ### Without conection to remote server

 - add a local lesson ( available for user only)
 - all your scheduled lessons in one list with sorting by day and time
 - see schedule for current, next and previous week
 - see a homework
 
## Technologies

 - Single activity
 - View pager 2
 - Retrofit
 - Room
 - Kotlin coroutines
 - Firebase Cloud Massaging
 - LiveData
 - Navigation Component
 - Material date/time pickers
 
 ## Architecture
 
 - MVVM
