package VRPCore.Models;

import VRPCore.Enums.FeeInterval;
import VRPCore.Enums.FeeTypes;

public class Fee {
    public String FeeName;
    public long LastPaid;
    public FeeTypes FeeType;
    public FeeInterval FeeInterval;
}
