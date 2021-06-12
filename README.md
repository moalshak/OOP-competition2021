
[flaps]: images/readme/FLAPS.png
[blueprint]: images/readme/blueprint_coordinates.png


![FLAPS Banner][flaps]

You have been tasked with extending the Flight Logistics Aviation Planning Simulation (F.L.A.P.S. or FLAPS) framework by adding an editor that allows users to edit the configuration
of airplanes. For this, Team Operations Worldwide Elevation Routes (T.O.W.E.R.) has provided you with a bunch of code. Before you get scared by the amount of code:
don't. The main application can be found in the `simulation` package. You will be working in the `aircraft_editor`
package. The only class from the `Simulation` package that you have to edit is the `Aircraft` class.

Below, T.O.W.E.R. has listed their requirements. They mostly care about what the editor can do. How the different
functionalities are presented to the user is not very important to them, so it is up to you to design a UI.
They have added some suggestions about the UI, but it is not mandatory to follow this exactly. The only concrete
requirements here is that the GUI should use Swing and follow the MVC pattern. For this assignment, you no longer have
to implement Listeners yourself (but you are still free to do so).

This assignment is quite complex, so be sure to start early. You won't be able to finish if you start 24 hours before
the deadline! To help you a bit, there is an optional demo on the 1st of June. If you create a pull request before the
28th of May at 23:59, your TA will put you in a schedule and you can get some feedback on your progress so far.

Before you start implementing things, please read through the assignment thoroughly.

## General Overview of the FLAPS Framework

FLAPS is a framework that can be used to send aircraft between different airports. The world that FLAPS models consists
of multiple airports. Each airport has a number of aircraft on the ground. Due to the different locations of the airports,
each airport has different demands for import goods. As such, the price per kg of certain trade goods can differ
between airports. Similarly, the fuel prices also fluctuate between airports.

T.O.W.E.R. has a high demand for customizability. Correspondingly, the entire application is initialized from a fully customizable
yaml configuration. The yaml files can be used as quick guidelines to see what kind of properties certain classes have.
The yaml files in the `data` directory define the "rules" of the simulation. These are specifications that define which
airports, aircraft, cargo types and fuel types exist. These specifications are modelled in the application as (immutable)
types (e.g. `AircraftType`, `CargoType`, `FuelType`). The exception to this is `Airport`, since there can only ever exist
one airport of any given type (we cannot have two Heathrow Airports in real life).

Throughout this project, T.O.W.E.R. used [Lombok](https://projectlombok.org/). Lombok makes use of annotations to reduce
the amount boilerplate code a developer needs to write. For example, whenever you see a field with a `@Getter` annotation,
then this field has a getter method (without us having to write it manually). Similarly, if a class has the `@Getter`
annotation, all fields in the class have getters. We do not require you to use this (or know how it works exactly), but
it is important to know what this does when reading through the code.

While you only have to work with the `Aircraft` class, you should have a look at the methods that the following classes
expose, since you will have to use some of them.

- `AircraftType`
- `CargoArea`
- `CargoType`
- `CargoUnit`
- `FuelTank`
- `FuelType`
- `airport`

Whenever you intend to use a method from one of these classes (or any class for that matter), be sure to read the javadoc
first so that you know what it does exactly. For the `AircraftType` class it can be useful to read the `type_format.yaml`
to get an idea of the units of the different fields.

---

## 1 Basic View

An aircraft consists of two important customizable components: fuel areas and cargo areas. Information on the location
of these areas can be found in the `type` field of the aircraft. The components have an `x` and a `y` coordinate.
The coordinate system starts at the top center, so `x = 0` indicates the middle of the plane, while `y = 0` indicates the
top of the plane (see the image below). To make your life a bit easier, all blueprint ratio have a 1:1 aspect ratio.
You are free to draw them at a fixed size.

![FLAPS Banner][blueprint]

**Requirements**

- Start by displaying the blueprint image of the aircraft on the screen.

- Draw on top of the blueprint image an indicator (e.g. a red circle) for each fuel area. The coordinates of the fuel
  areas are proportional to the aircraft length. This means that a `y` coordinate of `50` and an `x` coordinate of `0` 
  on an aircraft with length `100` means that the point should be drawn in the middle of the image.
  
  > Hint: it might be useful to create methods that map from the aircraft coordinate space to the image coordinate space
  > (and back)

- Do the same for the cargo areas. Make sure this indicator is different from the one that is used for the fuel areas.
  That way the user can easily distinguish between cargo areas and fuel areas.

## 2 Aircraft Fuel

Each aircraft has one or more different fuel areas. Each of these areas has a certain capacity.
It goes without saying that an aircraft should be loaded with some fuel before it can take of.

**Requirements**

- Allow the user to select a fuel area.

> You could implement this by adding a panel that updates with the information/functionality of whatever is currently
> selected.

- When a fuel area is selected, the following information should be displayed:

  - Name of the fuel area
  - Capacity of the fuel area
  - Current amount of fuel in the fuel area

- Show to the user the current amount of fuel in the fuel tank.

- Allow the user to load fuel into the selected fuel area via the UI.

> This could be implemented by allowing the user to select a fuel area (e.g. by clicking on the indicator or selecting
> from  a list) and showing a slider (JSlider) to select the amount of fuel that should be loaded. If you decide to use
> a slider, make sure to add a `confirm` button that actually puts the fuel into the aircraft (instead of changing the
> fuel whenever the slider is changing values). This way, it is easy to > undo/redo the action once you implement
> undo/redo functionality.

- The user should not be able to add more fuel than whatever is the capacity of the tank.

- Make sure the information (such as the current amount of fuel) is updated when the user adds/removes fuel.

## 3 Cargo

The user should also be able to load/unload cargo to/from the aircraft.

**Requirements**

- Allow the user to select a cargo area.

- When a cargo area is selected, the following information should be displayed:

  - Name of the cargo area
  - Weight capacity of the fuel area
  - Current weight of the cargo area

- The user should be able to select a cargo type and the number of kilograms that should be loaded into the cargo area.

  > You can get a list of all available cargo types using the `world` field of `Aircraft`.

- Allow the user to add this selected cargo type and the corresponding amount to the selected cargo area.

  > You could add a popup that allows the user to select a cargo type and amount of cargo in kilograms. When the user
  > presses the `Confirm` button, the cargo is added to the cargo area.
   
- Allow the user to remove cargo from the cargo area.

  > This could, for example, be done via a popup that allows the user to select a cargo type. When the user presses the
  > `Delete` button, the cargo type is removed from the cargo area. You could also choose to allow the user to remove a 
  > specific amount of cargo.

- Each cargo area has a weight capacity. Make sure that the user cannot violate this weight constraint.

## 4 Information

Now that the user is able to fully configure an aircraft, it is time to show some additional information to the user.
There are three important things that should be added here. These are discussed next. Make sure that this information
is updated accordingly whenever there is a change to the aircraft that would affect the values. Where you show this 
information to the user is up to you.

### 4.1 Range

Depending on the configuration, the range of the aircraft changes. The range for an aircraft is defined as the following:

```
range = (total fuel / fuel consumption) * cruise speed
```

> In the real world this calculation is much more complex, but this is not the real world :)

**Requirements**

- Show the range of the aircraft to the user.

### 4.2 Center of Gravity

Similar to the range, depending on the contents of the fuel tanks and cargo areas, the center of gravity of the aircraft 
can change. The `x` coordinate of this center of gravity can be calculated as follows (it's equivalent for `y`):

The following Java-ish pseudocode indicates how you would compute a weighted sum of multiple components (where a component 
is anything that has weight and a position, such as cargo, fuel, or the aircraft itself).

```java
var x = 0.0;
var totalWeight = 0.0;
for (var c : components) {
	x += c.getWeight() * c.getXCoordinate();
	totalWeight += c.getWeight();
}
return x / totalWeight;
```

For the aircraft, you will have to use the coordinates of the center of gravity. This is accessible for an `aircraft` via
`aircraft.getType().getEmptyCgX()` (and similarly for the `y` coordinate).

Here a component can be a fuel tank, a cargo area or the aircraft itself. Doing this for both `x` and `y` results in the 
center of gravity of the entire aircraft. The aircraft itself also has a center of gravity. 
For the aircraft itself, 


**Requirements**

- Show the center of gravity of the empty aircraft.
- Show the center of gravity of the aircraft with its current fuel and cargo loads accounted for.

> Instead of printing the x and y coordinates of the center of gravity, you could also draw a unique indicator 
> (e.g. a square) on the blueprint image instead.

### 4.3 Profit Estimation

Since the aircraft are transporting cargo, the user should have some information about the estimated revenue, cost and
profit of this journey.

- The estimated cost is based on the fuel prices of the departure airport and the amount of fuel the journey will take:

  ```
  cost = fuel price origin airport * fuel loaded
  ```

- The estimated revenue is based on the amount of cargo and the corresponding prices in the destination airport:

  ```
  cargo revenue = cargo price destination airport * amount of cargo in kg
  ```

  The total revenue is the sum of this calculation for all different cargo types.
  
- The estimated profit is the difference between the revenue and the cost:

  ```
  profit = revenue - cost
  ```

**Requirements**

- The user should be able to see the estimated revenue, cost and profit.


### 4.4 Weight

The total weight of the aircraft is relevant for its departure, so this is useful to show to the user.

**Requirements**

- The user should be able to see the total weight of the aircraft (in kg).

## 5 Departures

By now, the user has enough information to know whether an aircraft can depart to its selected destination.

**Requirements**

- The user should be able to click a button that makes the aircraft depart to its destination.

  > The action of this button is already available with some basic functionality in the class `DepartAction`.

- The button should only be enabled when the following two conditions are met:

  - The destination airport has enough capacity to accept an incoming aircraft

  - The range is larger than the distance between the departure airport and destination airport.

  > The distance between two airports `a` and `b` can be retrieved as follows:
  `a.getGeographicCoordinates().distanceTo(b.getGeographicCoordinates())`.

  - The center of gravity of the load of the aircraft is within the tolerance of the aircraft.

  > Both the aircraft and its contents have a center of gravity. The difference between these as a percentage of the
  length of the aircraft cannot surpass the threshold as defined in `aircraft.getType().getCgTolerance()`

  - The total weight of the aircraft (including fuel and cargo) does not surpass the `maxTakeoffWeight`.

- Once the `Depart` button has been pressed, the aircraft will move to the new airport and an amount of fuel is removed.

  - The amount of fuel to be removed is calculated as follows: `( distance / cruise speed ) * fuel consumption`.
  
- All cargo should be removed once the aircraft arrives at its destination.
  
- A popup should appear that shows an informative message to the user that the aircraft departed.

- Ideally, the frame should close once the aircraft has departed. Leave this for the end though (it is not extremely
  important).


## 6 Undo and Redo

The user can sometimes make mistakes and manually fixing this can cost quite some time. To improve the user experience,
there is a final - very important - requirement, the user should be able to undo and redo actions.

To do this, follow these steps:

- For every operation that should support undo/redo, create a class that extends `AbstractUndoableEdit`.

- Add an `UndoManager` as a field to your model.

- Whenever the user clicks on a button, the corresponding `AbstractAction` should not perform the action itself,
  but rather create a new instance of the corresponding `UndoableEdit`. Make sure the constructor of the `UndoableEdit`
  executes the operation (or calls a function that does).

- Implement the methods `undo()` and `redo()` that undo/redo the action. Make sure to call `super.undo()` and
  `super.redo()` in these methods first. Otherwise, the `canRedo()` method from the `UndoManager` won't work.

- Add the edit to the `UndoManager` using the `addEdit()` method.

- Allow the user to use Undo and Redo from the menu (or via buttons). Selecting these options should call the
  `undo()`/`redo()` from the `UndoManager`. This will make sure that the appropriate action is being executed, and you
  do not have to keep track of all these operations yourself.

- Before undoing/redoing, be sure to check whether it is actually possible to undo/redo using the `canUndo()`/`canRedo()` 
  methods.

- Make sure all the information that is displayed to the user such as range, estimated profit and the availability of
  the `Depart` button are still being updated appropriately when undoing/redoing.

**Requirements**

The following actions should support undo/redo:

- Adding/removing fuel.

- Adding/removing specific cargo types.

---

## Requirements Summary

Below is a list of all the things your program should be able to do. Note that you should still pay attention to the
steps described above! This only serves as a very compressed summary.

Your program should be able to:

- Display the blueprint image of the aircraft

- Display (different) indicators for the cargo areas and fuel tanks

- Add/remove fuel to/from fuel tanks

- Add/remove cargo to/from cargo areas

- Show the following information:

  - Range

  - Empty aircraft center of gravity

  - Loaded aircraft center of gravity

  - Revenue of the journey

  - Cost of the journey

  - Profit of the journey

- Depart planes
- Support undo/redo functionality

Additionally:

- Your program should follow the MVC structure.
- Pay attention to the design of your program.
- All exceptions should be handled appropriately, and an informative message should be shown to the user if something
  goes wrong. As such, be sure to thoroughly test your program. You are not expected to fix issues in the code that has
  been provided to you.

---

## Report

For this assignment you will have to write a short report in which you will describe your program, and the corresponding
design decisions. This report should be written in markdown in the `report.md` file. You don't need actually need to
know what markdown is, just fill it in as a normal text file. If you want to make it look a bit nicer, you can look
[here](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet).

We do not expect a 100000 word, full-on architecture document; just be sure to discuss everything mentioned
in the template, which will probably result in 1000-2000 words.

---

## Extra

Once your basic program is 100% done (and perfected), you can move on to add some extras! These are
not mandatory, but can give you some bonus points. Examples could be:

- Keyboard shortcuts such as `Ctrl + z`, `Ctrl + y` etc.
- Make the interface as nice as possible. Add icons to buttons, add extra menus, etc.
- Add more information to the view, such as estimated flight time.
- Add the capacity of each component to the corresponding indicator on the blueprint image.
- Add more realistic calculations for fuel consumption/range.

Some more ambitious extras:

- Extend the program to support passengers as well as cargo.
- Use a third-party API to fetch real-world aircraft data.

If you decide to add extra actions, do not forget to add undo/redo for those to. Any action for which it makes sense to
undo/redo should have it.

---

## Handing in

Create a pull request from `flaps` into `main`. Make sure the code compiles and runs without any errors.
Do not forget the report!

