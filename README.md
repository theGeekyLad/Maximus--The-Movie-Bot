# Maximus
An interactive chatbot smart enough to answer almost anything above movies. It thereby changes the way one searches for movie related information.

## Introduction

### Scenario
The buzz word when it comes to movies is IMDBb which is where most of us are found, looking out for related information. There are many other websites as well as mobile apps where information regarding movies can be found or for the most part, a simple Google search can get you what you want.

### Future
With the advent of personal assistants such as the famous Google Assistant, Siri, Alexa and so on, chatbots with substantial cognitive abilities are taking over the way we *"search"*. It's simple. It's easier to ask a human rather than a web lookup followed by extensive filtering among the plethora of presented information simply because, a human's reply would be appealingly concise.

## Solution
Maximus is the chatbot from the future that changes how one looks up information about movies. The fact that it feels more like a human you're talking is what makes it worthwhile against a monotonous web search. Looking for the year of release? Or the cast? Or the major players - director, writer or producer? Maximus knows it all.

## Technologies

* **OMDb** <br> Yes, you read that right, not IMDb! OMDb provides for Maximus' pool of knowledge. Shout out to those guys for the amazing work as an open source database provider for movie-related information. Access to their service can be provided over HTTP requests which makes it cross-platform and makes way for easy implementation.
* **IBM Watson Assistant** <br> Being the go-to firm when it comes to chatbots today, IBM provides all that's required for Maximus to function which essentially means, all of Maximus' cognitive skills are a direct result of the machine learning on the IBM servers
* **Android** <br> Every chatbot requires an interface for the user to interact with. Presently, Maximus can be accessed through a dedicated Android app

## Setup

**Difficulty** - Mediocre *(you must know basic Android development)*

The services provided by IBM Watson in the free tier are metered while that of OMDb though free, are required for developer identity and hence have been removed from the source in this repository. In this section, you can find heads on how to setup your own,
* IBM Watson developer account, and
* OMDb account thereby procuring the required API key

Begin with downloading this repo.

### IBM Watson Assistant
* Head over to the [Bluemix Console](https://console.bluemix.net/catalog/services/watson-assistant-formerly-conversation) and sign in
* **Create** button, in order to create a new instance
* Once created, **Launch Tool**
* **Skills** -> **Create new** -> **Import skill** -> **Choose JSON file** -> Point to the `maximus-skill.json` from the downloaded repo
* **Deploy** button, at the left -> **Add to an assistant** -> **Create new** and fill up the fields -> **Create** button
* **View API details** hyperlink
* Note down **Assistant ID**, **Username**, **Password**
    * Open the `BotStuff.java` in your favorite editor
    * Assistant ID to be pasted in `WORKSPACE_ID` string field
    * Username to be pasted in `USERNAME` string field
    * Password to be pasted in `PASSWORD` string field

### OMDb
* Head over to the [OMDb official website](http://www.omdbapi.com/)
* **API Key** hyperlink at the top
* Account type as **FREE! (1,000 daily limit)** and plug in your email (that's where you'd get your API key)
* Follow the link enclosed in their mail to activate the API key
* Note down the API key enclosed in the same mail
    * Open the `BotStuff.java` in your favorite editor
    * Key to be pasted in `OMDB_API_KEY` string field

Phew, the setup is more or less done! Now all that's left is to build the Android app (preferably in [Android Studio](https://developer.android.com/studio/)). If you're new to compiling an Android app, you could skim through the [official guide](https://developer.android.com/studio/intro/).

## Conclusion

As a prototype that demonstrates the cognitive abilities of Maximus, the Android app presently just allows textual interaction. **Voice based interaction** would make it far more practical as chatbots after all, must have a human touch!