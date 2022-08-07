package src

import java.util.logging.Level
import java.util.logging.Logger

class Utils


val logger: Logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

fun logInfo(msg: String) = logger.log(Level.INFO, msg)
fun logError(msg: String) {
    logger.log(Level.SEVERE, "ERROR ERROR ERROR ERROR ERROR $msg")
}