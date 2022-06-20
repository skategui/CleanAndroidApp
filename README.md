# MVI + Compose + Coroutine Flow 2022


### Architecture, language and tools used to make this app

- MVI (Model View Intent)
- Clean architecture
- Koin
- Coroutine (with Flow)
- Retrofit
- JUnit
- Kotlin
- LifeCycle
- Compose (Used in the latest edition 2022)
- View binding (only used in the old Edition 2019, to show the other way of interacting with the views)
- Proguard (to obfuscate the code)


### What it is about ? 

The app should have two screens, as follows:

First screen will display the edition of news of 2022 as a list.
Clicking on the article will redirect on the main website, inside Chrome.

Second screen is based on the old app available in that repo where I re-used the same logic, and just update
the code,libs, and way of interacting with the view.

Both activities work (+/-) the same way : 

They display a message "In progress" with a lottie animation while fetching the list of posts from the server and if we do receive a list of posts, the list is displayed.
For each row, the user avatar is loaded with the post's title and the post's body. 
In the case there is a network issue, an error message with a lottie animation is displayed with a button to have the possibility to refresh the view.
in the case the list of posts is empty, a message is displayed to the user informing him that list is empty.


### Usage of Flow

Inside the ArticleDatastore, ArticleDao and ArticleUseCase , as soon as the local DB is updated, then a flow will emit the latest version of the article
to the viewmodel , and then render this new list of articles on the view. It's fully **reactive**.


### Improvements that could be made in the future

- Architecture : usage of multi module in a mono repo : to split up the team work and make the build faster (as it will only recompile the module modified and not the whole project), where each module could be a big feature.
  Also, each module will be able to use the config, then use the same version of each dependencies. 
  For this usecase we could have a module for the design component for instance.
- Security : Store key inside a secured shared preference. Keys can also be split and shared
- Security : setup SSL pinning and certificate transparency to avoid any man in the middle
- Tracking : Add a tool to track user interactions in the app (segment / mixpanel + datadog) to understand how the users use the app and verify if the usage is normal
- Network : Cache the requests **smartly** with a different expire time given the resource to access.
- Network : Add some paging on the resources that intent to return a big list of data. To avoid the user to wait too long and **use less memory/less battery**.
- Network : **Take in consideration the connexion speed** to load a different amount of data per request, and make sure the user does not wait too long to get the response of the request.
- Network :  use https://developers.google.com/protocol-buffers/ or https://google.github.io/flatbuffers/ to **transit smaller on-the-wire packet size, then make the requests faster**.
  Benchmark of the performance : https://codeburst.io/json-vs-protocol-buffers-vs-flatbuffers-a4247f8bda6f
- UI: Move the composable UI inside a Constraintlayout to improve performance.
- UI: Setup dark mode

- Requests content : Populate object in the JSON instead of providing only the object's id and make another request to get the full object.
  For instance, in the Post object we could have received the user object instead of only the Id ,or only **the fields needed** to have smaller response).
  [GraphQL](https://graphql.org/) proposes this feature, and then help reduce response size, then speed up the response time.

- App : Create a light version / less greedy of the app for country that does not have high quality android devices (such as in Africa and Asia)
- CI : Add a CI to build + run the unit tests after each commit / add a nightly build that will run all the tests (junit + espresso, as it's longer to run).


By Guillaume Agis - 2022

With love and passion <3

