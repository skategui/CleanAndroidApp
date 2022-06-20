package agis.guillaume.cleancode.tracker

import agis.guillaume.cleancode.BuildConfig

/**
 * In a more advanced app, this class would send event to an external tool (like mixpanel, segment, etc..)
 * in order to track the behaviour of the users , crashes and be able to reproduce issues /
 * understand how they use the app.
 */
object Tracker {

    fun trackError(e : Throwable?) {
        if (BuildConfig.DEBUG)
            e?.printStackTrace()
        // will send request to the server to track crashes and errors
    }

}