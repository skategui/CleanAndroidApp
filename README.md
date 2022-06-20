# MVI + Compose + Coroutine Flow 2022

Sample app using the latest android tools. 

### Architecture, language and tools used to make this app 💪

- 🔨Kotlin : Language of dev
- ♻️ MVI (Model View Intent) : It's an unidirectional flow a reducer to create the new State. [source](https://github.com/skategui/CleanAndroidApp/blob/main/app/src/main/java/agis/guillaume/cleancode/base/BaseViewModel.kt) 
- 🧹 Clean architecture : To well structured the app. [source](https://github.com/skategui/CleanAndroidApp/tree/main/app/src/main/java/agis/guillaume/cleancode/usecases)
- 💉 Koin : to inject deps. [soure](https://github.com/skategui/CleanAndroidApp/tree/main/app/src/main/java/agis/guillaume/cleancode/di)
- 📈 Coroutine (with Flow) : to make the app fully reactive (only the Edition summer 2022, newest version of the app). [source](https://github.com/skategui/CleanAndroidApp/blob/main/app/src/main/java/agis/guillaume/cleancode/ui/article/ArticlesListViewModel.kt)
- 🛰️ Retrofit : To make HTTP requests. [source](https://github.com/skategui/CleanAndroidApp/blob/main/app/src/main/java/agis/guillaume/cleancode/api/services/ArticlesService.kt)
- 🏠 Room : Local DB. [source](https://github.com/skategui/CleanAndroidApp/tree/main/app/src/main/java/agis/guillaume/cleancode/datastore/article)
- 👀 JUnit with Turbine and Mockk : Added 35+ unit tests to cover the code, [source](https://github.com/skategui/CleanAndroidApp/tree/main/app/src/test/java/agis/guillaume/cleancode/ui/article)
- 🖼️ Compose : Used in the summer edition 2022 [source](https://github.com/skategui/CleanAndroidApp/tree/main/app/src/main/java/agis/guillaume/cleancode/ui/compose)
- 🖼️ View binding : Only used in the old Edition 2019, to show the other way of interacting with the views. [source](https://github.com/skategui/CleanAndroidApp/blob/main/app/src/main/java/agis/guillaume/cleancode/ui/post/PostListActivity.kt)
- 🔒 Proguard : to obfuscate and minify the code. [source](https://github.com/skategui/CleanAndroidApp/blob/main/app/proguard-rules.pro)

### How to run the app

You should get yourself an API key from News API (https://newsapi.org/) and add it inside `variables.properties`.
Otherwise without this, you will get an error message.

### Usage of Flow 🤟

Inside the ArticleDatastore, ArticleDao and ArticleUseCase, as soon as the local DB is updated, then a flow will emit the latest version of the article
to the viewmodel , and then render this new list of articles on the view. It's fully **reactive**.

PostActivity is an old app I 've made in 2019 and just make it "up to date". Better to watch the code about ArticleActivity as it contains Compose, Coroutine Flow, MVI, etc...


### Improvements that could be made in the future 💡

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
- App : Create a light version / less greedy of the app for country that does not have high quality android devices (such as in Africa and Asia)
- CI : Add a CI to build + run the unit tests after each commit / add a nightly build that will run all the tests (junit + espresso, as it's longer to run).
- ....


By Guillaume Agis - 2022

With love and passion <3

