package agis.guillaume.cleancode.api.utils

import java.net.UnknownHostException

/**
 * Utils to check the type of HTTP error thrown
 */
object HttpErrorUtils {

    /**
     * Check if the user has list the connexion given the throwable thrown
     * @param throwable throwable thrown
     * @return true if he user has lost connexion, false otherwise
     */
    fun hasLostInternet(throwable: Throwable?): Boolean {
        return throwable is UnknownHostException
    }
}