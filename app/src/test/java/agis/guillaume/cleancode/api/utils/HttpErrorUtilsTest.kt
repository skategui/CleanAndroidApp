package agis.guillaume.cleancode.api.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.UnknownHostException

class HttpErrorUtilsTest {

     @Test
     fun `Check if the user has lost the connexion given a UnknownHostException`() {
         assertEquals(true, HttpErrorUtils.hasLostInternet(UnknownHostException()))
     }

     @Test
     fun `Check if the user has lost the connexion given an exception that is not UnknownHostException`() {
         assertEquals(false, HttpErrorUtils.hasLostInternet(RuntimeException()))
     }

 }