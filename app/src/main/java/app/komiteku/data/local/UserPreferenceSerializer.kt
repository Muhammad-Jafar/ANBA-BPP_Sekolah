package app.komiteku.data.local

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class LoginSession(
    val loginAt: Long? = null,
    val token: String? = null,

    val id: Int? = 0,
    val nis: String? = null,
    val name: String? = null,
    val jurusan: String? = null,
    val kelas: String? = null,
    val email: String? = null,
    val hp: String? = null,

    val billId: Int? = 0,
    val billings: Int? = 0,
    val recentBill: Int? = 0,

    val transactionId: String? = null,
    val amount: Int = 0,
    val vaNumber: String? = null,
    val expireTime: String? = null,
)

object SessionSerializer : Serializer<LoginSession> {
    override val defaultValue: LoginSession get() = LoginSession()

    override suspend fun readFrom(input: InputStream): LoginSession {
        try {
            return Json.decodeFromString(
                LoginSession.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to read LoginSession", e)
        }
    }

    override suspend fun writeTo(t: LoginSession, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(LoginSession.serializer(), t).encodeToByteArray())
        }
    }
}

val Context.dataStore by dataStore("my_session.json", SessionSerializer)
