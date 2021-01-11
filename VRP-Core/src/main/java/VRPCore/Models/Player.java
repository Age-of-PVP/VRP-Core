package VRPCore.Models;

public class Player {
    public String Username;

    public java.util.UUID UUID;

    public EditorStickMode StickMode = EditorStickMode.None;

    public int LogonBalance = 0;

    public Job Job;

    public boolean firstLogon = true;

    // <Stats>

    private final float ADRENALINE_MAX = 100f;
    private final float STRENGTH_MAX = 100f;

    public float Adrenaline = 0.0f;
    public float Strength = 0.0f;

    public void clipStats() {
        if(Adrenaline > ADRENALINE_MAX)
            Adrenaline = ADRENALINE_MAX;
        if(Strength > STRENGTH_MAX)
            Strength = STRENGTH_MAX;
    }

    // </Stats>
}
