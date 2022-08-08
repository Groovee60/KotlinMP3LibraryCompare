package src

import java.io.File

const val rootOld = "/Volumes/Passport Backup Drive/Full Music Catalog"
const val rootNew = "/Volumes/External OneDrive/OneDrive/Full Music Catalog"

var loggingBandName = false
var loggingAlbumName = false
var loggingTrackName = false

fun main() {

    if (!azFolderCompare("_Christmas")) return
    if (!azFolderCompare("_Numeric")) return
    if (!azFolderCompare("A")) return
    if (!azFolderCompare("B")) return
    if (!azFolderCompare("C")) return
    if (!azFolderCompare("D")) return
    if (!azFolderCompare("E")) return
    if (!azFolderCompare("F")) return
    if (!azFolderCompare("G")) return
    if (!azFolderCompare("H")) return
    if (!azFolderCompare("I")) return
    if (!azFolderCompare("J")) return
    if (!azFolderCompare("K")) return
    if (!azFolderCompare("L")) return
    if (!azFolderCompare("M")) return
    if (!azFolderCompare("N")) return
    if (!azFolderCompare("O")) return
    if (!azFolderCompare("P")) return
    if (!azFolderCompare("Q")) return
    if (!azFolderCompare("R")) return
    if (!azFolderCompare("S")) return
    if (!azFolderCompare("T")) return
    if (!azFolderCompare("U")) return
    if (!azFolderCompare("V")) return
    if (!azFolderCompare("W")) return
    if (!azFolderCompare("X-Z")) return

}

private fun azFolderCompare(folderName: String): Boolean {

    File("$rootOld/$folderName").listFiles()?.let { oldFiles ->
        File("$rootNew/$folderName").listFiles()?.let { newFiles ->

            val oldBandPaths = oldFiles.sorted().map { it.absolutePath }.filterSysFiles
            val newBandPaths = newFiles.sorted().map { it.absolutePath }.filterSysFiles

            return compareBandFolders(folderName, oldBandPaths, newBandPaths)
        } ?: run {
            println("ERROR: no files in new Folder $folderName")
        }
    } ?: run {
        println("ERROR: no files in old Folder $folderName")
    }

    return false
}

private fun compareBandFolders(folderName: String, oldBandPaths: List<String>, newBandPaths: List<String>) : Boolean {

    val zippedOldNewBandPaths = oldBandPaths.zip(newBandPaths)

    println("Folder $folderName - ${zippedOldNewBandPaths.size} bands")

    zippedOldNewBandPaths.forEach { (a, b) ->

        val bandA = a.lastSection
        val bandB = b.lastSection
        if (bandA != bandA.trim()) {
            println("ERROR in folder $folderName (Non-Fatal): Old band name $bandA needs to be trimmed")
        }
        if (bandB != bandB.trim()) {
            println("ERROR in folder $folderName (Non-Fatal): New band name $bandB needs to be trimmed")
        }

        val bandNameMatches = bandA.trim() == bandB.trim()
        if (!bandNameMatches) {
            println("ERROR in folder $folderName: band name discrepancy\n$bandA\n$bandB")
            return false
        }
    }

    if (oldBandPaths.size != newBandPaths.size) {
        println("ERROR in folder $folderName: band count discrepancy - old: ${oldBandPaths.size} new: ${newBandPaths.size}")
        return false
    }

    for (oldNewPair in zippedOldNewBandPaths) {
        val success = compareBand(oldNewPair)
        if (!success) return false
    }

    //println("Folder $folderName has no unfixable errors in ${oldBandPaths.count()} bands")
    return true

}

private fun compareBand(oldNewPair: Pair<String, String>): Boolean {

    val oldBandPath = oldNewPair.first
    val newBandPath = oldNewPair.second
    val bandName = oldBandPath.lastSection
    
    File(oldBandPath).listFiles()?.let { oldAlbumFiles ->
        File(newBandPath).listFiles()?.let { newAlbumFiles ->

            val oldAlbumPaths = oldAlbumFiles.sorted().map { it.absolutePath }.filterSysFiles
            val newAlbumPaths = newAlbumFiles.sorted().map { it.absolutePath }.filterSysFiles

            return compareAlbumFolders(bandName, oldAlbumPaths, newAlbumPaths, isBoxSet = false)
        } ?: run {
            println("ERROR: New Folder $newBandPath is empty")
            return false
        }
    } ?: run {
        println("ERROR: Old Folder $oldBandPath is empty")
        return false
    }
}

private fun compareAlbumFolders(bandOrBoxSetName: String, oldAlbums: List<String>, newAlbums: List<String>, isBoxSet: Boolean = false): Boolean {

    val zippedOldNewAlbumPaths = oldAlbums.zip(newAlbums)

    val boxSetPrefix = if (isBoxSet) "  SUBFOLDER: " else String()
    if (loggingBandName) println("  $boxSetPrefix$bandOrBoxSetName - ${zippedOldNewAlbumPaths.size} album(s)")

    zippedOldNewAlbumPaths.forEach { (a, b) ->

        val albumA = a.lastSection
        val albumB = b.lastSection

        if (albumA != albumA.trim()) {
            println("ERROR: Old album name $albumB needs to be trimmed")
            return false
        }
        if (albumB != albumB.trim()) {
            println("ERROR: New album name $albumB needs to be trimmed")
            return false
        }

        val albumNameMatches = albumA.trim() == albumB.trim()
        if (!albumNameMatches) {
            println("ERROR: album name discrepancy\n$albumA\n$albumB")
            return false
        }
    }

    if (oldAlbums.size != newAlbums.size) {
        println("ERROR: album count discrepancy - old: ${oldAlbums.size} new: ${newAlbums.size}")
        return false
    }

    for (oldNewPair in zippedOldNewAlbumPaths) {
        val success = compareTracks(bandOrBoxSetName, oldNewPair, isBoxSet)
        if (!success) return false
    }

    return true
}

private fun compareTracks(bandOrBoxSetName: String, oldNewPair: Pair<String, String>, isBoxSet: Boolean): Boolean {

    val oldAlbumPath = oldNewPair.first
    val newAlbumPath = oldNewPair.second
    val albumName = oldAlbumPath.lastSection

    File(oldAlbumPath).listFiles()?.let { oldTrackFiles ->
        File(newAlbumPath).listFiles()?.let { newTrackFiles ->

            val oldTracks = oldTrackFiles.sorted().map { it.absolutePath }.filterSysFiles
            val newTracks = newTrackFiles.sorted().map { it.absolutePath }.filterSysFiles

            if (oldTracks.isEmpty()) {
                val albumName = oldAlbumPath.lastSection
                println("ERROR (Non-Fatal): $bandOrBoxSetName / $albumName is empty!")
                return true
            }

            // Some bands (e.g., Beatles) have sub-folders for box sets
            // check for this scenario and recursively call back to the album compare method
            val fileName = oldTracks.first()
            val testFile = File(fileName)
            if (testFile.isDirectory) {
                val boxSetName = oldAlbumPath.lastSection
                return compareAlbumFolders(boxSetName, oldTracks, newTracks, isBoxSet = true)
            }

            val extraSpaceForBoxSet = if (isBoxSet) "  " else String()
            if (loggingAlbumName) println("$extraSpaceForBoxSet    ${oldAlbumPath.lastSection} - ${newTracks.size} track(s)")

            val zippedOldNewTrackPaths = oldTracks.zip(newTracks)

            zippedOldNewTrackPaths.forEach { (pathOld, pathNew) ->

                var trackOld = pathOld.lastSection
                var trackNew = pathNew.lastSection

                if (loggingTrackName) println("$extraSpaceForBoxSet      $trackNew")

                if (trackOld.contains(" .mp3") || trackOld.contains("?")) {
                    //println("ERROR (Non-Fatal): Old track name $trackOld in $bandName/$albumName needs to be patched")
                    val fixedTrackName = trackOld.replace(" .mp3", ".mp3").replace("?", "")
                    val fixedPath = pathOld.replace(trackOld, fixedTrackName)
                    trackOld = fixedTrackName
                    val renameSuccess = pathOld.renameFileAtPath(fixedPath)
                    if (!renameSuccess) return false
                }
                if (trackNew.contains(" .mp3") || trackNew.contains("?")) {
                    //println("ERROR: New track name '$trackNew' in $bandName/$albumName needs to be patched")
                    val fixedTrackName = trackNew.replace(" .mp3", ".mp3").replace("?", "")
                    val fixedPath = pathNew.replace(trackNew, fixedTrackName)
                    trackNew = fixedTrackName
                    val renameSuccess = pathNew.renameFileAtPath(fixedPath)
                    if (!renameSuccess) return false
                }

                if (trackOld != trackNew) {
                    println("ERROR: $bandOrBoxSetName/$albumName has track name discrepancy\n$trackOld\n$trackNew")
                    return false
                }
            }

            if (oldTracks.size != newTracks.size) {
                println("ERROR: $bandOrBoxSetName/$albumName has track count discrepancy - old: ${oldTracks.size} new: ${newTracks.size}")
                return false
            }

            return true
        } ?: run {
            println("ERROR in New $bandOrBoxSetName/$albumName: no files")
        }
    } ?: run {
        println("ERROR in Old $bandOrBoxSetName/$albumName: no files")
    }
    return false
}

val List<String>.filterSysFiles: List<String>
    get() {
        return filter { filename ->
            val isSysFile = filename.contains("DS_Store") ||
                    filename.contains("Thumbs.db") ||
                    filename.contains("desktop.ini") ||
                    filename.contains(".jpg") ||
                    filename.contains(".jpeg") ||
                    filename.contains(".png")
            !isSysFile
        }
    }

val String.lastSection: String
    get() {
        return substringAfterLast("/")
    }

fun String.renameFileAtPath(newPath: String) : Boolean {

    val oldPath = this
    val srcFile = File(this)
    if (!srcFile.exists()) {
        logError("Source file doesn't exist")
        return false
    }

    val renamedFile = File(newPath)
    if (renamedFile.exists()) {
        logError("Destination file already exist")
        return false
    }
    val success = srcFile.renameTo(renamedFile)
    if (success) {
        println("Renaming succeeded from $oldPath to $newPath")
        return true
    }  else {
        logError("Renaming failed")
        return false
    }

}