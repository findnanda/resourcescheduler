# resourcescheduler
Import the project in eclipse and convert it to maven nature.
Run the Scheduler.java which has a main method and it send multiple messages and set few groups for cancellation. Its a very basic scheduler developed to test the MessageReceiever and various functionalties like the Cancellation and Sorting Strategies.
Also there are unit test cases added for the important classess like the MessageReceiver class, Cancellar and Sorting classes.
The various sorting techiniques that the receiver could handler and provide you ways to dyanmically change the sorting strategies using the interface ISortingRules. It also provides support to mix and match develop your own sort strategy rule based on the available SortingStrategy classes.
The resourcePool can be configured by config.properties which is loaded by app-ctx.xml at startup.
Also you could force switch off the PrioritisedGroupSortingStrategy in the receiver which is also loaded by app-ctx.xml at startup. The default value is set to false.
