## DXCC Beaming positioning
This is a utility to assist ham radio operators in quickly assessing where they are located relative to the main lobe of a remote station, 
judging from their current QSOs.

## When to use it
Let's say you are listening to QSO between two stations and one of those is a DX station you are interested in.
It would be very helpful to quickly check what is your position realtive to where the DX station is pointing their antennas,
thus maximising your chances to contact them. If you have already listened to the details of their setup (like antenna, power) during the QSO
you can quickly decide whether it makes sense to call them now or wait until they turn their antennas to your direction.

## How it works
You run the program with your own DXCC prefix (which is resolved to your country coordinates) and let it running.
When you hear the 2 stations you type in their prefixes and you get a calculation of how closer/farther you are relative to the 
other station and how many degress to the right/left.

For example, if you are located in England and you are listening a station from Canada having a QSO with a station from Italy
you just have to enter their prefixes in the order "DX TARGET" , where DX is the station of interest (in this case the Canadian station)
and TARGET is the station of reference (in this case the Italian station).
in this case we enter "VE I" and we get this result:

```
Canada -> Italy
target vs my loc : Beaming diff -4.72 distance diff -1456.94 Km
DX -> target : Beaming 55.82 distance 6940.97 Km
DX -> my loc : Beaming 51.10 distance 5484.04 Km
```

which shows that the DX station (the Canadian station) "sees us" 4.72 to the left and 1456.94 Km closer than the other station.
Practically we are in the same direction but much closer. 

As another example, while still located in England, a station from Sweden is having a QSO with a station from Greece and we are interested 
in the station from Sweden. We enter "sm sv" and we get

```
Sweden -> Greece
target vs my loc : Beaming diff +68.01 distance diff -1088.88 Km
DX -> target : Beaming 165.00 distance 2432.35 Km
DX -> my loc : Beaming 233.00 distance 1343.47 Km
```

This shows that although we are 1088.88 Km closer than the Greek station we are 68.01 degress to their right, which is quite off the main direction.
If the station is using a highly directive antenna, we probably are off into their null.

## How to run it

Download the pre-built `jar` from the releases, make sure you have Java 1.8 or later installed and run it on a terminal like this:
```
java -jar dxccbeampos-1.0.jar MY_PREFIX
```

replacing MY_PREFIX with your callsign prefix
e.g running it for an English prefix, that would be

```
java -jar dxccbeampos-1.0.jar G
```

or 

```
java -jar dxccbeampos-1.0.jar 2E
```

