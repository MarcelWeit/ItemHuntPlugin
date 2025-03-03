### Your feedback is welcome! This is an early development version and I plan to update it if there is enough interest.

### Did you enjoy the plugin? Buy me a coffee on ko-fi to support further development!

Disclaimer: You might want to allow flight on your server in the server.properties before using the upflight item.

## Features
- Up to 9 teams with any number of players
- Teams can have one member (1v1v1 is possible)
- Collect a random item out of 1000 different items
- End items and hard to get items are removed
- Team Backpack

## Configurable
- Timer
- Amount of special items to skip the current target item
- Yes / No Elytra
- Yes / No Hunger
- Amount of updraft items to jump into the air
- Backpack size
- Recommended: 2 Hours, 5 Skip Items, With Elytra and No Hunger, 5 Updraft Items, Backpacksize 27 for one player 54 for 2+

## Setup
Each player joins a Team by using the red wool in their hotbar or entering the /teams command.

/Image

An operator on the server enters the /startchallenge command.

/Image

## Parameters:
- Time in seconds
- Amount of skip items
- With Elytra as Start Item (Yes / No)
- With Hunger (No= No Hunger, Yes = With Hunger)
- Amount of updraft items
- Backpack size (0 <= n*9 <= 54)
- You can choose to play without updraft items, skip items or backpack if you set the amounts to 0.

The challenge begins and each team must collect a random item. If one of the team members collects the item, the whole team must collect a new item and they will receive a point.

/Image

The tabbed menu shows who is on which team and what items they need to collect.

/Image

A link is provided in chat to the Minecraft wiki for more information on the item.
A passenger above the player's head also shows the current item to be collected [In Development might bug].

The skipitem can be used at any time to skip the current objective for the team. Using the skipitem is still worth one point.

KeepInventory is enabled.

Each team has its own backpack where they can store items.

/Image

If you set the amount of updraft items to greater than zero each team will get an additional updraft item after obtaining the target item.

When the timer has run out, the server operator can use the results command to display the results. The command must be run for each place, starting with the last place. An inventory will be displayed showing when the item was collected and by whom. Players can then click on a link in chat to reopen the inventory and use buttons to switch pages.

/Image

## Commands for Server Operator
- /skipitem <targetplayer> - Can be used by admin if the target item is too difficult to obtain
- /resetchallenge - Resets the whole challenge
- /results - Shows the result for the last place team after the timer has run out. You can use the command again to show the next place.
- /timer <resume | pause>

The Plugin is inspired by the item force battles of BastiGHG.
