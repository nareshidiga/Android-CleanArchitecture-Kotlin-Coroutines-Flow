## Android Clean Architecture - Kotlin
This project is based on Clean Architecture by Robert Cecil Martin aka Uncle Bob.   
It uses android architecture components (Live data, View model and Room) with Kotlin Coroutines, Flows, 
Dagger2 and Retrofit2.
       
       
## Key benefits
1. Create extensible and reusable modules to use across multiple apps.
2. A system that is testable, independent of frameworks, database and UI
3. When some frameworks or libraries become obsolete easily replace them with new ones.


## Using this template    
  There are three main packages.  
  
  **base** : all your base classes go in this package    
  **features** : all your features related code go in this package    
  **app** : all your high level classes that combine multiple features go in here.   
  
  **base** and **feature** packages are divided into four standard packages (data, presentation, usecases and entities).
  
As your code grows, you can move **base package** to a separate library module and
each **feature** to a separate feature module. And you can reuse the features across your multiple android apps.
      
     
     
## Understanding Clean Architecture
**Key principle:** Nothing in an inner circle can know anything at all about something in an outer circle

  ![alt text](https://8thlight.com/blog/assets/posts/2012-08-13-the-clean-architecture/CleanArchitecture.jpg)


To understand and adapt clean architecture on android apps, take a look at the following great articles:

Original article by Uncle Bob:    
  https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html


A three part series blog on Android Architecture from FIVE:  
  http://five.agency/android-architecture-part-1-every-new-beginning-is-hard/  
  http://five.agency/android-architecture-part-2-clean-architecture/  
  http://five.agency/android-architecture-part-3-applying-clean-architecture-android/
