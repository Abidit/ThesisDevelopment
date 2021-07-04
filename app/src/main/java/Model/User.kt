package Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
        @PrimaryKey
        var _id : String = "",
        var Fullname : String? = null,
        var Email : String? = null,
        var Username : String? = null,
        var Password : String? = null,
        var Usertype : String? = null,
        var Image : String? = null
        )
