# OOP Design Document

![UML.PNG
](https://github.com/cs2113-f21/project-2-cole-knie/blob/main/UML.PNG)

## Overview

[provide a general overview of your OOP design. Once you do, remove this text!]


[For each of the classes in your OOP design, including ones provided
for you, offer a brief description of their functionality and how they
interact. Be sure to highlight good OOP like encapsulation,
inheritance, and polymorphism. Once you do, remove this text! Also be
sure the headers below match the class names, and not Class 1 and
Class 2 and etc.]

## City

City manages the simulation. When simulate is called, it activates a single round of the simulation. It includes private data structures that keep record of all of the creatures. In addition to the already provided methods, I added a public method  to update indivudal creature's location in the hashmap. It takes no parameters except for the object. It's public to ensure that it can be called from any creature, but it doesn't take any other parameters beside the creature it is updating to avoid misuse. I also added functionality so that City could interact with creatures to provide a list of all of the creatures as well as a list of creatures at a specified point without having to worry about misuese. I accomplished this by designing Creature to have a deep copy method that every child class must implement. DeepCopyMap adds functionality that allows creature realizations to add/remove other creatures without having to worry about misuse by using clones.  

## Creature

In Creature, I provided additional functionality, mostly in getDeepCopy, and in setDir, findClosestPath(Creature c). This allows child classes to create deep copies of themselves as  well as allowing subclasses to set the direction (which enables chasing in the case of cats, and running away from cats in the case of birds). It is also worth noting that I have provided additional constructors which will be useful for deep copying and creating creatures at specific row-column locations. Additionally, I created findClosestPath to set the direction to the closest path of a creature that is passed as a parameter. The field copyOf allows copies to keep a record of their parent, but keep it private so nothing outside of the creature class can access it. Rmself removes a creature from the city (and therefore the simulation). If the creature is a copy, rmself removes the creature it is a copy of

## Mouse

I have constructed Mouse to act very simply. It implements the abstract methods specified in Creature and has an additional method called maybeBaby that allows it to decide whether or not to give birth to another mouse. It inherits from Creature

## Cat

I designed Cat with several features including jumpdistance, chasedistance. I also wrote several methods like maybekill and maybechase that allow Cat functionality. This method inherits from Creature and implements the neccesary abstract methods defined in Creature and provided in the assignment. Cat is dependent on ZombieCat (to turn into when it dies) as well as Mouse (to chase). It also depends on Bird (for Chasing birds in  level 4).

## ZombieCat

I kept the structure of ZombieCat relatively the same, but I modified the chasing distance as well as the lifespan. It also plans on overriding maybeKill to decide to kill non-Zombie Cats. It inherits from Cat and depends on Cat (to chase Cats) and Mouse to chase Mice. It also depends on Bird for the same reasons above.

## Egg


I created  an Egg class, that when dying creates Birds. It inherits from Creature and depends on Bird. 

## Bird

I created a Bird class that randomly lays eggs. Birds are green and intelligently run away from Cats and ZombieCats who chase them.

