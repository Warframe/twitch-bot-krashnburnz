![http://s28.postimg.org/4nc11uwzh/phreakbotlogo123.png](http://s28.postimg.org/4nc11uwzh/phreakbotlogo123.png)
Logo created by: Arthur Keshishyan [Portfolio](http://dspixels.com/portfolio)


# The Project #

# RECENT NEWS #
## Twitch Hacked! ##
Twitch was hacked recently, so it seems they have not only changed some of their IP addresses, but they have also reset OAUTHKEYS, Password, etc. This means you may need to log into your bot's Twitch account and change your password. NOTE: It may be a good idea as well to backup your bot and your user points (just copy and paste the entire bot folder somewhere safe)! Doing backups should be something you are doing on a regular basis anyhow, at least every few weeks should be the minimum.

As far as Twitch being hacked, because of this, Twitch has sent an email to your Bot's email address with information on how to reset your password. Once the password is reset, you need to GENERATE THE OAuthkey for your bot. You can do this by clicking the bottom button on the Bot's login screen that says "GET OAUTHKEY" . Lastly, you will need to update the Twitch IP on the bot's LOGIN screen. The IP is irc.twitch.tv

## What is the bot? ##
Phreak Bot is a chat bot for www.twitch.tv that can perform a variety of moderating tasks specifically designed to create a more efficient and friendly environment for your community, including a Rank System, Voting System, Event System, and many other options! Did we mention it's OPEN SOURCE! You can download the source code and change as you like. The bot will accumulate points for viewers in your chat and these points can be used for various events built into the software or events held by the streamer. The events built into the bot are as follows:


---



---


## Current Implemented Events ##
- Rank System

- Accumulated User Points (from watching the stream)

- Lottery

- Auction

- Voting

- Wager

- Misc. Giveaway

- Giveaways by the Streamer by removing/adding points!


---

# CURRENT PROJECT STATUS: **OPEN BETA** (as of July 7th 2014) #
OPEN BETA ACCESS: The software was moved into Open Beta in July of 2014. This means the Bot is able to be used by any Twitch streamer. Just download the software, run it, and fill in your bot account details to connect. More information on how to connect can be found in the documentation that comes with the bot download.

> NOTE: At this stage the software has went through many revisions and at least contains the basic implementations of the planned systems ( Login/Console, Current Viewer system, Events system, User System), Please keep in mind the software is still in BETA and many features may be incomplete or contain bugs. At this time, the GUI elements are not fully completed (the Tabs on the interface).



---


## Latest News: ##
**Feb 22nd 2015** - We have recently Implemented the ability to set the amount of points (the actual amount) accumulated over a certain period of time (delay between each point accumulation). This feature has been requested by many people so we decided it was time to implement it. The values you put into the input boxes are then saved to your settings file along with your other settings when you close the software (only if you have the Save Credentials checked). If you try to connect without a VALUE for either of these new settings, the software will add it for you. The next time you close the software it will save those settings.

**Jan 23 2015** - The documentation for the bot has been updated to version 81. There was a large amount of information in version 52 that did not apply any longer. Links on the website for the new version have been updated, and the version 81 of the Bot download has been updated to include the new documentation.


**July 7th 2014 -** The website that used to host the files that checked for authorized channels that were participating in the Closed Beta has been shut down. So people that are using an older version of the software (Pre-version 081) may get a popup saying their channel is not authorized. This is because the file containing authorized channels cannot be downloaded since the website is down. To fix this issue, update to the newest version of the software (currently version 082 is the newest version). **PLEASE NOTE** BACK UP your settings and user points before attempting an upgrade! Please see the Text and/or Video tutorial listed below if your unsure how to upgrade the bot to a newer version. **IMPORTANT** Be sure to always backup all your bot files before you attempt the update!


---


## Downloads: ##

### Latest Version (Version 082) ###

Mirror 1: http://s000.tinyupload.com/?file_id=86137997706043982708

Mirror 2: http://www.filedropper.com/phreakbotver082

Mirror 3: http://www.megafileupload.com/en/file/611482/Phreak-Bot-ver-082-zip.html


---


### Legacy Downloads: ###

[Download Section](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)


---


## Documentations and Video Tutorials ##


### Full Bot Documentation Version 81B (Updated Feb 8th 2015): ###

http://s000.tinyupload.com/?file_id=35293723098761282882


---


### How to upgrade the bot Video Tutorial: ###

#### Video Tutorial ####
https://www.youtube.com/watch?v=xrjC06xQM7k

#### Documentation Tutorial ####
http://s000.tinyupload.com/?file_id=50875945163676569583

---


# Donate and show your support for open source software #

> If you got this far, I'm thinking you are considering using this software! I hope you are as excited as we are! Please consider making a donation to support the development of this software. Currently only one person is working on the project, and although I try to devote as much time as I can into improving the project, I still need to eat and pay bills.

If you feel the software is useful, whether for operation in your Twitch chat or even as a learning tool for MIRC bot development, please consider making a donation of any size to help offset some of the costs towards developing the software!

I want you to decide what the software is worth to you and we hope you will make a donation depending on that worth. Even $5 donations are very welcome and appreciated! Thank you in advance for your support!



![http://www.giveforward.org/blog/wp-content/uploads/2010/02/paypal_donate1.jpg](http://www.giveforward.org/blog/wp-content/uploads/2010/02/paypal_donate1.jpg)

CLICK HERE TO SHOW YOUR SUPPORT ---> https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ7PBS9ZSQLQA


---


# Commands and Documentation #

http://s000.tinyupload.com/?file_id=62899559243735719843


---


# Beta Changelog #

## Beta Version 082 ##

**(bot [revision 082](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=082), document version 81)**

- Implemented the ability to set the amount of points accumulated over a certain period of time (delay between each point accumulation). This feature has been requested by many people so we decided it was time to implement it. The values you put are then saved to your settings file when you disconnect the bot then close the software (only if you have the Save Credentials checked)

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)

## Beta Version 081 ##

**(bot [revision 081](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=081), document version 52)**

- BOT Moved into OPEN BETA (July 7th 2014). Many other new implementations are in this new version. As you can see there have not been any public releases of the compiled code (JAR file) for nearly 30 revisions. This is because a large portion of the GUI has been worked on which needed alot of changes to accomplish this. The current Viewer Tab and Users Tab are now nearly fully functional. The GUI for the update has been implemented but is not yet operational. This will come in a future patch.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)

## Beta Version 055 ##

**(bot [revision 055](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=055), document version 52)**

- Removed where when a viewer places a wager, if they disregard the args, it will
ignore their wager. this is implemented to avoid spamming of the bot.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)

## Beta Version 054 ##

**(bot [revision 054](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=054), document version 52)**

- Critical fix for the Rank system. Bug in the code where because of the way the
rank is sorted, we SHOULD reverse the list at the end for ascending order, but
the reverse was put in the wrong position (inside a loop, which was causing
abnormal ranks. Thank you to Ching-Ting for catching this, you rock!

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)

## Beta Version 053 ##

**(bot [revision 053](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=053), document version 52)**

- Fixed issues with wager changes. Was getting an error because of typo setting
a boolean when closing the wager, and reopening.

-Removed where subtracting points reveals the remainder amount of points.

-Added new commands to disable new wagers and to enable new wagers (if wagers were closed previously). This was implemented to disallow people sniping in wagers right before the event that is being wagered on ends.
> -!closewager
> -!openwager

- When a user checks their rank, it will now try to update the Rank list before
telling them their rank. This is try to avoid the user being return -1 because
they just joined the chat and showing them the wrong rank.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)


## Beta Version 050 ##

**(bot [revision 050](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=050), document version 50)**

- Implemented features to Import and Export a plain text version of the points in and out of the system that represent all the user points stored in the User\_Map\_File. This implementation was done to ensure another way to backup and recover user points (was primarily implemented to support legacy users of software before changes to the User Object type were made which disallowed them to use their old User\_Map\_File with newer version of the software) (8+ months old)

- Implemented the Wager system which allows the owner to enable a bet system, to allow the viewers to use their points by placing wagers on certain events going on in the stream. The following commands have been included with the system and will be detailed more in depth in the documentation included with the software.

> !startwager 

&lt;-args&gt;

    (dash delimited)        - This command can be run by the owner (or moderators if mods have been enabled)  It will start the wager system with the specified arguments set as the options to bet on. The args need to be delimited by dashes       Example:   !startwager -Die -Live -Cry                        which would start a new wager for 3 options.

> !wager 

&lt;choice&gt;

 

&lt;amount&gt;

           - This command will allow a user to place a wager on one of the options Example: !wager 2 1200.

> !wagerchoices      - This command will echo all the options that were given when the wager system was activated (meaning stuff they can the options they can bet on)

> !mywager      - This will echo in the chat room what the user placed wager on and for what amount.

> !wagerwinners 

&lt;choice&gt;

      - This will choose the winning options, and reward all people that placed a wager for this option by 2**their wager amount.**

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)


## Beta Version 049 ##

**(bot [revision 049](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=049), document version 49)**

- Implemented a rank system based off the points system. the command !rank will
display the user's current rank out of the total amount of all users.

- When using !checkpoints, the rank will be displayed along with this
information.

-Implemented a new vote system. Commands from this system are as follows:
!startvote, !endvote, !getvoteoptions, !vote, !myvote

Please refer to the updated documenation included with this version to learn how these
new commands work.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)


## Beta Version 048 ##

**(bot [revision 048](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=048), document version 48)**

- Fixed an issue where the bot name, Channel Name, and Points name if not entered
all lower case could cause issues when logging in. Now the login screen will
take these strings and convert them to lower case (since twitch does not use
upper case letters for user name, or channel names). Thanks Alex and Dave for helping me find this issue!

- Proof read more of the included document. There are still many typos in this document which will eventually be corrected. Thank you for your patience.


[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)


## Beta Version 047 ##

**(bot [revision 047](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=047), document version 47)**

- The lottery can now be initiated with a argument for the ticket cost. If the command was !lottoon 200     A ticket would cost 200 points. If there is no command given, the default amount of 5 points per ticket is used.

- The lottery can now be manually ended with the command !pickwinnernow     This command will end the lottery, pick a winner, give the winner their points, and turn the lottery off. This command DOES NOT HAVE TO BE USED. If its not used, the 30 minutes timer will eventually pick the winner, and do everything listed that this command does.

- Total revamp of the Documentation that comes with the bot. This new version should be much easier to read, and each section is first organized by the type of command (Lotto, Auction, Administrative, Misc, etc...), then sorted alphabetically)

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)

## Beta Version 046 ##

**(bot [revision 046](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=046), document version 46)**

- Bot now keeps track of ops in the chat. Can now use command !modswatching
to see the amount of mods in chat, and a list of them.

- Now all mods can use commands if enabled (off by default). To enable, the
channel owner or developer must use the command in the chat channel
!enableModCommands      Mods are not allowed to add or remove points from people
because if done incorrectly, this could devistate the points system. Use the
command !disableModCommands   to disable this feature after enabling it.

- Made some changes to the login in the bot code to facilitate mods being able
to use commands. now there exists a list of developers that work on the project
for testing purposes and also to help channel owners that need help with their
bot in their channel. So this separates developers from moderators considering a
developer may not be a moderator in all channels that use the bot.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)


## Beta Version 045 ##

**(bot [revision 045](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=045), document version 40)**

**(Changes below implemented in [revision 44](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=44))**

- Fix a major issue found in the lotto system. The lotto system would hang with
many viewers in the channel trying to enter the lotto. This was found to be an issue with the implementation of the GUI console, and the way the Points engine VERBOS listed when it cycled through the points to find users. This and many other System.out messages have been removed to improve the performance of the software and GUI in general.

- Fixed some issues with the VERBOS of the addpointsall, addpoints, and
subpoints.

- Fixed issue with the lotto command and implementation to me more fluent and
informative.

**(Changes below implemented in [revision 45](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=45))**

+ Implemented improvements to the Auction system ([revision 45](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=45)).

> -Bids can only be placed below a certain increment level. The admin will specify this amount when starting the auction. eg. !startauction 100      This command would start the auction with no bid able to exceed 100 points more then the current high bid. This stops the bids from going high very quickly (makes it a bit more suspenseful like a real auctioneer)

> - A user can only successfully put a bid in with the following conditions.

> /Check if user actually has the points they are trying to bid with. If they don't, increment the counter for not enough points warning. If the counter reaches a certain amount depending on room size, it will display then reset the counter.

> /Check if user is trying to bid more then the increment limit set by the admin. If more then add 1 to the counter for  this warning. If the counter reaches a certain amount depending on room size, it will display then reset the counter.

> /Check to see if the user is already in the Bidder Map, then check timestamp and be sure 10 seconds atleast has passed. If not, add 1 to the SpamWrning counter. if this counter reaches a certain amount, it will display this error to the chat room.

> /If all these checks are good, then make this user's bid the highbidder amount.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



---

## Beta Version 042 ##

**(bot [revision 042](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=042), document version 40)**

+ Made some in depth improvements to the User point system.

> - The system now updates users in the channel every 10 seconds (before it was only when the points increment, 5 minutes).

> - If a user joins and instantly tries to check points (an error will tell them, and the system will say they have -1 points), if they try again after the next 10 second update, then the system should recognize them if they actually have points. (This is true if Twitch is not bugged. During peak hours sometimes Twitch Chat has serious issues, and this surely would affect the point system because of this. The bot cannot update Viewers if it cannot get a list of them from the Twitch Servers)

> - If the user doesn't exist in the User map file, then it is assumed they are a new user, and will tell them this and welcome them to the channel.

+ Implemented changes to the document included adding a GNU GPL license.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



## Beta Version 040 ##

**(bot [revision 040](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=040), document version 20)**

- Implemented new feature in settings for automatic backups of User Points.

+ Implemented the ability for the program to backup your User Map File with a
unique time stamp during the time of shutdown. This will ensure if your User Map
file is ever corrupted, or if your channel's user points are all removed by accident, the admin can recover these files by deleting the corrupted "User\_Map\_File" and replacing it with one of the backups (remove the time stamp from the backup and change the backup file name to "User\_Map\_File"). The files are stored in the same folder/directory that the .JAR file of the bot is loaded. Time Stamp is in the format DD\_MM\_YYYY\_HourMinuteSecond

+ Implemented the settings above to be loaded and saved during startup and
shutdown so the software remembers what the user selected previously. Default =
backups OFF

**NOTE** As with the previous version, if after this build you have issue loading the bot, please try to DELETE the "user\_settings" file located in the same folder/directory you are loading the .JAR file for the bot. This is because some of the new additions to the save file listed above may cause issues with outdated/malformed user\_settings file.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



---

## Beta Version 033 ##
**(bot [revision 033](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=033), document version 20)**

> - Implemented new feature to control when the bot Advertises that it is accumulating points.

+ Implemented the ability (under settings menu) to choose how often the bot will advertise that it is accumulating points. (Never, 5 mins, 10 mins, 30mins) Please keep in mind that this setting can only be changed while the bot is not logged in.

+ Implemented the ability for the program to save/load this preference to/from the user\_settings file.

**NOTE** If after this build you have issue loading the bot, please try to DELETE the "user\_settings" file located in the same folder/directory you are loading the .JAR file for the bot. This is because of the new addition to the save file listed above, which might cause issues with outdated/malformed user\_settings file.

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



---

## Beta Version 027 ##
**(bot [revision 027](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=027), document version 2)**

- Implemented improvements on the GUI.

> + Implemented Tabbedpane view for the primary GUI

> + Started work on a voting system

> + misc other small fixes

> + Fixes to lottery system (lottery system will now tell how many points are in the pool when it adverts. Also the system will shut down automatically after the winner is chosen, where previously an admin would have to manually turn the system off)

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



---

## Beta Version 020 ##
**(bot [revision 020](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=020), document version 2)**

- Implemented some major improvements on the login screen including the following.

> + Created an animated login button so the user knows its trying to connect

> + Text Fields disable while trying to login

> + Implemented threads for the connection method so the program does not freeze while trying to connect

> + Created Help and Support menus

> + Connection console was added to supply errors if incorrect

> + Overhaul on the Documentation for the bot was done, including an indepth section about the Oauthkey for twitch information was given. More information--> https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=20

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



---


## Beta Version 016 ##
**(bot [revision 016](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=016))**

+ Implemented a console to be used for information, errors messages, and IRC chat. More information--> https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=16

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



---


## Beta Version 015 ##
**(bot [revision 002](https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=002))**

+ Implemented connect, log out features to control when the bot is online, and when the bot is offline.

+ Fixed error with the program not fully closing if the save credentials box was not clicked. Added menu items to enable debug and to exit the program
More Information--> https://code.google.com/p/twitch-bot-krashnburnz/source/detail?r=15

[Download](https://code.google.com/p/twitch-bot-krashnburnz/#Beta_Downloads)



---


# PhreakBot Beta Downloads #

## Version 082 (released 2/22/2015) ##
http://s000.tinyupload.com/?file_id=86137997706043982708

## Version 081 (released 7/7/2014) ##
http://s000.tinyupload.com/?file_id=23705336725731823200

## Version 055 (released 3/8/2014) ##
http://s000.tinyupload.com/?file_id=05911233787910843590

## Version 054 (released 3/7/2014) ##
http://s000.tinyupload.com/?file_id=81098619684866775977

## Version 053 (released 3/1/2014) ##
http://s000.tinyupload.com/?file_id=48031924304938084109

## Version 050 (released 2/26/2014) ##
http://s000.tinyupload.com/?file_id=40590885814242798473

## Version 049 (released 2/24/2014) ##
http://s000.tinyupload.com/?file_id=79001415237841495497

## Version 048 (released 2/22/2014) ##
http://s000.tinyupload.com/?file_id=08083935328838787733

## Version 047 (released 2/19/2014) ##
http://s000.tinyupload.com/?file_id=03713513981309178576

## Version 046 (released 1/16/2014) ##
http://s000.tinyupload.com/?file_id=44847294195900830945

## Version 045 (released 12/30/2013) ##
http://s000.tinyupload.com/?file_id=24905571951615048868

## Version 042 (released 12/19/2013) ##
http://s000.tinyupload.com/?file_id=27237994057624417887

## Version 040 (released 12/15/2013) ##
http://s000.tinyupload.com/?file_id=39910883597554912006

## Version 033 (released 12/15/2013) ##
http://s000.tinyupload.com/?file_id=29239101013606049245

## Version 027 (released 12/15/2013) ##
http://s000.tinyupload.com/?file_id=65697324109939590082

## Version 020 (released 12/10/2013) ##
http://s000.tinyupload.com/?file_id=53217626229453420129

## Version 016 (released 12/7/2013) ##
http://s000.tinyupload.com/?file_id=00885338025587170709

## Version 002 (released 12/1/2013) ##
http://s000.tinyupload.com/?file_id=39130707865602486048

## ALL VERSIONS from Version 002 to 081 ##
http://s000.tinyupload.com/?file_id=12143415298649681295