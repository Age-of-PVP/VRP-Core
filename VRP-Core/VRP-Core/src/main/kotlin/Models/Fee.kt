package VRPCore.Models

import VRPCore.Enums.FeeInterval
import VRPCore.Enums.FeeType

class Fee {
    var FeeName: String? = null
    var LastPaid: Long = 0
    var FeeType: FeeType? = null
    var FeeInterval: FeeInterval? = null
}