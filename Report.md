# FLAPS Report

**First author**: Mohammad Al Shakoush (s4274865)

**Second author**: Dominic Therattil (s4228952)



## Introduction

We have been tasked with extending the Flight Logistics Aviation Planning Simulation (FLAPS) framework by adding an editor that allows users to edit the configuration of airplanes. In this report we will elaborate on our program design choices during the construction of FLAPS, namely the finer details in the decisions we made when implementing the Model View Controller (MVC) design pattern into our program. 

In addition, we will give an overview of our impression of the final program and reflect upon the changes we hoped to have made and challenges we faced in the creation of our program.



## Program design

We separated our program in 4 main componenets.
1. Controller
2. Model
3. Util
4. View

Below we will go through each one of these packages and explain the design and functionality of it.
### Controller

The package `controller` contains two packages:
</br></br>
1.  `Errors`: this package contains classes that can handle potential errors caused by the user such as exceeding a limit or choosing unvalid values.
    </br></br>
2.  `Listeners`: in this package we have all the listeners that listen to the user interaction. It can be either changing a value in a `JSpinner` or a `JSlider` or choosing an element from a cargo list. And finally pressing a button. Each action to each particular component in the GUI makes the program react in such a way that the user can smoothly configure the aircraft as wished. All the listeners listed in this package react with different models from the models package.




### Model

The model package contains classes that hold data needed to be viewed to the user. These models get their own data updated by the listeners. In there we have:

1.  Blueprint contains :
    </br></br>
    a.  `blueprintModel` which has fields that contain important information needed for the blueprint of the aircraft. For example : we have fields for the center of gravity,positions of cargo areas and fuel tanks in addition to methods that calculate these fields and map some on top of the blueprint shown in GUI.
    </br></br>
    b.  `BluePrintModelListener`: is an interface that is used by the panels over in the `view` package. With the help of this interface we update the data viewed from the `blueprintModel` into the `blueprintPanel`.
      
  

2.  `Config_models` contains models essentiel for the configuration part of the program. Such as:
    </br></br>
    a.  `InfoPanelModel` : from this model we call and make an instance of other models that update different things needed to be presented to the user after a change in the configuration with the help of its own interface `InfoPanelModelListener`. These informations are about :
    </br></br>
    b.  `Aircraft`: information about the aircraft are the weight, center and center of gravity. For those we have other models that calculate the weight and center of gravity and update them in the main model `InfoPanelModle`.
    </br></br>
    c.  `Passengers`: this model contains information about the passengers. But also about where the entrance of the aircraft is placed and the maximum number of passengers. With its own interface it updates the it’s own fields that are then used by other view components.
    </br></br>
    d.  `Trip`: information about the trip is stored in this package. Such as the range of the aircraft and the profit estimation of the trip. The appropriate calculations are made here and then stored to be used by other models and components.


### Util

Here we store the classes that have methods that are more general. But still help with the overall functionality of the program. Such as:

1.  `AddEdit`: the class is used to make un/redo functionality possible.
    </br></br>

2.  `isDestinationReachable`: this class calculates whether the aircraft selected is fit to reach the destination selected. By adding the fuel tanks capacities and checking whether the fuel at max capacity is enough. If this is not possible the user is presented with an option box stating that the aircraft will never reach the destination, however if the user wishes to proceed with configuring the aircraft, they can. Of course in that case the aircraft is not cleared out to take off the ground.


### View

In here we have all the components that are presented to the user. Such as:

1.  `Frame`: containing the main frame of the program and its edit menu for the un/redo. In addition to the fact that the functionality of the key combinations of CTRL+Z and CTRL+Y are also present in our program.
    </br></br>
2.  `Panels`: here we have all other panels that are made when the user wishes to configure the aircraft. We divide these panels into :
    </br> </br>
    a.  Blueprint panel: here we have the `blueprintPanel`. This panel draws the blueprint of the aircraft and the dedicated key to make sure the user understands what they are looking at. The key holds information about the indicators of the aircraft.Such as, cargo areas, fuel tanks , the center of gravity and the entrance of the aircraft. Pressing on any of these indicates triggers the interaction panel to view the appropriate configuration panel.
    </br></br>
    b.  `Aircraft info panels`: here we have the main `InfoPanel` which is the main panel that contains all other interaction panels. Alongside with the result panel which is the main panel holding the result of the configuration of the user. The interaction panels are panels that hold information alongside with components that are intractable. Such as button’s, sliders and spinners. These interaction panels are:
    </br></br>
    1.`CargoConfigPanel`: this panel takes care of making the configuration panel of the cargo areas and holds all components needed to the user to configure the cargo area. With a button to view the import demands of the destination airport.  
    </br></br>
    2.`FuelConfigPanel`: this panel takes care of making the fuel panel of the fuel tanks and holds all components needed to the user to configure the fuel tanks.
    </br></br>
    3.`PassengerConfigPanel`: this panel takes care of making the configuration panel of the passengers and holds all components needed to the user to configure the passengers . Always 2 crew are onboard. The pilot and the co-pilot. Then a random number of crew is generated for each type. However, the user has to worry only about the ‘normal’ passengers.


All panels listed above are part of the `InteractionPanel`. The interaction panel also holds the `confirmButton`. When pressing this button an instance of the program is saved for the undo, and the result panel is updated through methods that update the models then the panels. With the interaction components that show the current amount of either cargo, fuel or passengers. In addition to this the result panel has a departure button, where the aircraft is allowed to depart only when some constraints are met. In here we chose to make the user know what is wrong with the aircraft and why it cannot depart by putting the text with the reason on top of the depart button. This way the user never misses it and does not get constant pop ups when a constraint is not met.

When designing the program we made a lot of decisions regarding the design. One such is to break down the task as much as we can in classes. This way whenever something goes wrong we know exactly what and where it went wrong. Also grouping the classes together in packages gives a clear idea on what each class does. In addition to this we have also decided to use multiple split panes and scroll panes. This way we guarantee a good user experience, by making the interface extremely customizable to the users liking. Alongside with a nice looking map key we made in photoshop to make sure the user knows exactly what they are looking at.

## Evaluation of the program

We find our program to be stable and handle invalid user actions appropriately. We each separately tested the program and we did not find any bugs that would crash our program and the bugs that we did find were minor appearance inconsistencies which were patched appropriately.

One minor appearance issue that we were not able to patch is when you have selected a configuration icon and you perform an undo/redo that icon still remains large. However, this does not impact the functionality of the program in any way and we discovered it too late since it was such a minor detail we did not have time to patch an update for the deadline.

Potential areas for improvement would be one or two of our classes pushing the soft limit of 200 lines. However in most cases it is to make understanding the code easier for each other or due to lengthy processes of setting up the GridBagLayout.



## Process evaluation

The processes that led to the final code and report was challenging to say the least. 

Upon the release of the assignment we designated each other tasks and set a deadline to complete them, upon the completion of tasks we would regroup, revaluate and then set each other some more tasks until the project was complete. 

Along the way we would communicate regularly to get ideas for superior implementation choices, an early example would be using icons opposed to dots for the aircraft configuration.

Communicating during the debugging process via screen sharing and voice application was crucial to our success in the formation of this program. It really helped expedite some of the issues we were facing, since we could help each other pick out mistakes and quickly bounce ideas to solve bugs. The saying two heads are better than one, was definitely highlighted during these times.

We would say establishing the models was quite difficult, since understanding the overwhelming amount of information in the simulation package was very involved. Notably, the most difficult part was implementing the cargo methods for the aircraft, because it took us some time to understand the hashmaps and how it all works in relation to the CargoAreas and all the different CargoUnits.

However, once this phase was complete and we had set up a model it helped us create a solid foundation that we could build upon. This really helped us follow the correct MVC and Command pattern rules which in turn helped us easily add new features to the view and controller.

Due to both of our personalities' need for perfection in the aesthetic of the program we would say the View implementation was easiest yet the most time consuming and along with the View came the implementations of the Controller which was also pretty straightforward. We described the View in this manner, because the Swing libraries provide a wide assortment of components, with minute details that really enables you to set the look and feel of the program as you envision it. 

As a result we spent an unnecessary amount of time on perfecting the task requirements by;

- Using JScrollPanes and various LayoutMangers to display the aircraft information in a comprehensive and well arranged manner.
- Providing Informative popup messages. 
- Use of images where appropriate to make the program more engaging.

And also adding epic features like; 

- The ability to hover over points to get ToolTipText capacity briefing about the area your mouse is over.
- Informative data such as estimated flight time and better fuel consumption/range. 
- Functionality to add passengers to the plane, which overall makes the simulation a whole lot more realistic.
- Ctrl-Z & Ctrl-Y for undo/redo functionality.

Another minor hiccup was implementing undo/redo utility. This was due to sparsity in information for implementing this. Although, once we dove into the JavaDocs and watched some tutorials it all finally came together and this functionality really made our program complete.

Through progressing with this assignment we learned the importance of task management and communication, and how vital this is to fluid progression of the project and more importantly we learned a whole lot about Java, MVC and Command Patterns.

Finally, the construction of the report was quite straightforward however, for the future writing down notes on the progression of the project would make the process much easier.



## Conclusions

All in all this project and this course was our favourite from this year, the amount of cool skills we were able to learn in such a short time is amazing. We are extremely satisfied with our final program and what we were able to produce over the past couple weeks.
