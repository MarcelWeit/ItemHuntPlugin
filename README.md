# Item Hunt Plugin

### Your feedback is welcome! This is an early development version and I plan to update it if there is enough interest.

### Did you like the plugin? Buy me a coffee on ko-fi to support further development!

**Disclaimer**: You might want to allow flight on your server in the server.properties before using the upflight item.

## Features
- Up to 9 teams with any number of players
- Teams can have one member (1v1v1 is possible)
- Collect a random item out of 1000 different items
- End items and hard to get items are removed
- Team backpack
- Game Mode

## Configurable
- Timer
- Amount of special items to skip the current target item
- Yes / No Elytra
- Yes / No Hunger
- Amount of updraft items to jump into the air
- Backpack size
- Recommended: 2 hours, 5 skip items, with elytra and no hunger, 5 updraft items, backpacksize 27 for one player 54 for 2+
- New with 1.4: Gamemode 0 or 1 (0: random order, 1: same order of items)

## Setup
Each player joins a Team by using the red wool they got when joining the server in their hotbar or by entering the /teams command.

![](https://i.imgur.com/9XBPX4A.png)

An operator on the server enters the /startchallenge command.

![](https://i.imgur.com/dEfGnsO.png)

## Parameters:
- Time in seconds
- Amount of skip items
- With Elytra as Start Item (Yes / No)
- With Hunger (No= No Hunger, Yes = With Hunger)
- Amount of updraft items
- Backpack size (0 <= n*9 <= 54)
- You can choose to play without updraft items, skip items or backpack if you set the amounts to 0.

The challenge begins and each team must collect a random item. If one of the team members collects the item, the whole team must collect a new item and they will receive a point.

![](https://i.imgur.com/dEfGnsO.png)

The tabbed menu shows who is on which team and what items they need to collect.


![](https://i.imgur.com/t0aDeyy.png)

A link is provided in chat to the Minecraft wiki for more information on the item.
A passenger above the player's head also shows the current item to be collected [In Development might bug].

The skipitem can be used at any time to skip the current objective for the team. Using the skipitem is still worth one point.

KeepInventory is enabled.

Each team has its own backpack where they can store items.

![](https://i.imgur.com/S21SYVw.png)

If you set the amount of updraft items to greater than zero each team will get an additional updraft item after obtaining the target item.

When the timer has run out, the server operator can use the results command to display the results. The command must be run for each place, starting with the last place. An inventory will be displayed showing when the item was collected and by whom. Players can then click on a link in chat to reopen the inventory and use buttons to switch pages.

![](https://i.imgur.com/73318Vl.png)

## Commands for Server Operator
- /skipitem <targetplayer> - Can be used by admin if the target item is too difficult to obtain
- /resetchallenge - Resets the whole challenge
- /results - Shows the result for the last place team after the timer has run out. You can use the command again to show the next place.
- /timer <resume | pause>

The Plugin is inspired by the item force battles of BastiGHG.
