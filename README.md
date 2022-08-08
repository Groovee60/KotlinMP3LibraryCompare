# KotlinMP3LibraryCompare

Standalone Kotlin app (not Android)

Developed in IntelliJ IDEA

Nothing fancy but might be useful for someone else based on my use case

I have a very large MP3 collection and have always shadowed it on two
portable hard drives. First on a pair of 250GB drives, then a pair of 50GB drives,
etc. Fast forward to recently when the collection (just under one terabyte)
was shadowed onto a pair of 2TB SSD drives. 

As a Microsoft 365 subscriber, I get 1 TB of OneDrive space as part of the
subscription. So I've painstakingly uploaded the collection to there. Due
to the wonkiness of syncing to/from One Drive, drive formatting issues,
etc., I ended without full confidence that the most recent version of my 
collection was up in One Drive. But I did know that all the latest versions
existed on one or the other of the two portable drives.

This simple but useful standalone Kotlin app allows you to specify, by editing
the source code, two separate volumes to be compared. I'm on a Macbook Pro
but wrote the original version of this app on Windows, so it should be trivial
to adjust the path format accordingly.

My library exists in alphabetical folders, so the main function makes calls
like this:

    // pass logBandName = true as the second parameter to see progress in the log  
    if (!azFolderCompare("_Christmas")) return
    if (!azFolderCompare("_Numeric")) return
    if (!azFolderCompare("A")) return
    if (!azFolderCompare("B")) return

If your volumes are basically synced, you might just run it on everything. The
app will fix simple filename dispcrepancies that I have encountered (question
marks in the file name, space in front of the .mp3 extension). This can be
added to as necessary.

Other issues such as missing folders (band name, album name), count discrepancies,
track count or name discrepancies will stop the processing and let you fix 
them. That was fine for me, as the processing (after the first time for each
folder) is pretty quick, and once a folder is clean, I will comment it out and
run only on the remaining folders. For my just under 1TB catalog, it only takes
about 15 seconds to process everything and report that the two volumes match
exactly. And the non-verbose log output looks like this:

Procssing folder _Christmas...
Folder _Christmas has no unfixable errors in 77 bands
Procssing folder _Numeric...
Folder _Numeric has no unfixable errors in 22 bands
Procssing folder A...
Folder A has no unfixable errors in 233 bands
Procssing folder B...
Folder B has no unfixable errors in 314 bands
Procssing folder C...
Folder C has no unfixable errors in 235 bands
Procssing folder D...
Folder D has no unfixable errors in 242 bands
Procssing folder E...
Folder E has no unfixable errors in 121 bands
Procssing folder F...
Folder F has no unfixable errors in 114 bands
etc...

Now that I know my volumes match, I'll just trust OneDrive to stay in sync
with one SSD drive and return the other to be Time Machine backup drive.
