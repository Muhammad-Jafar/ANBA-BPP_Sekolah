package app.komiteku.ui.utils

import app.komiteku.data.model.LoginRequest
import app.komiteku.data.model.LoginResponse
import app.komiteku.data.model.LoginResult

object Dummy {

    /* REQUEST */
    fun dummyLoginReq(): LoginRequest = LoginRequest("user@mail.com", "user123")


    /* RESPONSE */
    fun dummyLoginRes(): LoginResponse = LoginResponse(
        false,
        "Success",
        LoginResult("token", 1, "10999", "user", "ipa", "MIPA 2", "08XXXXX")
    )
}