package Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Job(
        var _id : String? = null,
        var JobTitle: String? = null,
        val Description: String? = null,
        val SkillsNeeded: String? = null,
        val Userid: String? = null,
        val JobOwner: String? = null,
        val BudgetTime: String? = null
        )
{
        @PrimaryKey(autoGenerate = true)
        var id=0
}
