to give yourself the goggles run 
**/goggles**

This plugin works while using packetevents. When a player is detected wearing the goggles, a data-packet is sent to that player to change the metadata of all surrounding mobs. The thing that is changed is the display name of the mobs. It will show their health and max health.

No other plugins needed as a dependency, packetevents is being compiled in this code.
If you have other plugins that use packetevents and you have that plugin on your server, in the pom.xml file, under dependencies, change the "compile" to "provided" for the packetevents. The EntityLib Library of Tofaa2 needs to be compiled.
