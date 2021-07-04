package Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Application(
        @PrimaryKey
        var _id : String = "",
        var JobName: String? = null,
        var JobOwner: String? = null,
        var UserName: String? = null,
        var Email: String? = null,
        var Accepted: String? = null
)
