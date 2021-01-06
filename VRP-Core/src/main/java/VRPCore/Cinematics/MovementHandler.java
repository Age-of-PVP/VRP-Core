package VRPCore.Cinematics;

import VRPCore.VRPCore;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MovementHandler extends BukkitRunnable {

    public static double[] interpolate(double start, double end, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("interpolate: illegal count!");
        }
        double[] array = new double[count + 1];
        for (int i = 0; i <= count; ++ i) {
            array[i] = start + i * (end - start) / count;
        }
        return array;
    }

    // Player Data Pre-Cinematic
    public Location startingLocation;
    public GameMode startingGamemode;
    public Boolean wasFlying;
    public Boolean wasAllowFlight;

    private int tick = 0;

    Cinematic cinematic;

    int TicksPerSequence;

    Float[] allXPoints,
            allYPoints,
            allZPoints,
            allYawPoints,
            allPitchPoints;

    double[] allXPointsD,
            allYPointsD,
            allZPointsD,
            allYawPointsD,
            allPitchPointsD;

    public Player player;
    private VRPCore core;

    public MovementHandler(Player _player, Cinematic _cinematic, VRPCore _core) {
        core = _core;
        player = _player;
        cinematic = _cinematic;
        TicksPerSequence = cinematic.Duration / cinematic.KeyPoints.size() * 20;
        startingLocation = player.getLocation();
        startingGamemode = player.getGameMode();
        wasAllowFlight = player.getAllowFlight();
        player.setGameMode(GameMode.SPECTATOR);
        player.setAllowFlight(true);
        player.setFlying(true);
        wasFlying = player.isFlying();
        PreCalculations();
    }

    public void PreCalculations(){
        player.sendMessage("Starting calculation of points");

        allXPoints = new Float[cinematic.Duration * 20];
        allYPoints = new Float[cinematic.Duration * 20];
        allZPoints = new Float[cinematic.Duration * 20];
        allYawPoints = new Float[cinematic.Duration * 20];
        allPitchPoints = new Float[cinematic.Duration * 20];

        allXPointsD = new double[cinematic.Duration * 20];
        allYPointsD = new double[cinematic.Duration * 20];
        allZPointsD = new double[cinematic.Duration * 20];
        allYawPointsD = new double[cinematic.Duration * 20];
        allPitchPointsD = new double[cinematic.Duration * 20];

        CalculatePositions();
    }

    public void CalculatePositions() {
        player.sendMessage("Cinematic.Duration * 20: " + cinematic.Duration * 20);
        player.sendMessage("TPS * Cinematic.KeyPoints.Count: " + TicksPerSequence * cinematic.KeyPoints.size());
        player.sendMessage("TPS: " + TicksPerSequence);
        Data[]  dataPointsX = new Data[cinematic.KeyPoints.size()],
                dataPointsY = new Data[cinematic.KeyPoints.size()],
                dataPointsZ = new Data[cinematic.KeyPoints.size()],
                dataPointsYaw = new Data[cinematic.KeyPoints.size()],
                dataPointsPitch = new Data[cinematic.KeyPoints.size()];

        int ttps = ((cinematic.Duration  * 20) / (cinematic.KeyPoints.size() - 1));
        for(int i = 0; i < cinematic.KeyPoints.size(); i++){
            dataPointsX[i] = new Data(ttps * i, cinematic.KeyPoints.get(i).getX());
            dataPointsY[i] = new Data(ttps * i, cinematic.KeyPoints.get(i).getY());
            dataPointsZ[i] = new Data(ttps * i, cinematic.KeyPoints.get(i).getZ());
            dataPointsYaw[i] = new Data(ttps * i, cinematic.KeyPoints.get(i).getYaw());
            dataPointsPitch[i] = new Data(ttps * i, cinematic.KeyPoints.get(i).getPitch());
            player.sendMessage("X: " + dataPointsPitch[i].x + " | Y: " + dataPointsPitch[i].y);
        }

        for(int i = 0; i < cinematic.Duration * 20; i++){
            allXPoints[i] = (float)interpolate(dataPointsX, i, cinematic.KeyPoints.size());
            allYPoints[i] = (float)interpolate(dataPointsY, i, cinematic.KeyPoints.size());
            allZPoints[i] = (float)interpolate(dataPointsZ, i, cinematic.KeyPoints.size());
            allYawPoints[i] = (float)interpolate(dataPointsYaw, i, cinematic.KeyPoints.size());
            allPitchPoints[i] = (float)interpolate(dataPointsPitch, i, cinematic.KeyPoints.size());
        }

        for(int i = 0; i < cinematic.Duration * 20; i++){
            allXPointsD[i] = interpolate(dataPointsX, i, cinematic.KeyPoints.size());
            allYPointsD[i] = interpolate(dataPointsY, i, cinematic.KeyPoints.size());
            allZPointsD[i] = interpolate(dataPointsZ, i, cinematic.KeyPoints.size());
            allYawPointsD[i] = interpolate(dataPointsYaw, i, cinematic.KeyPoints.size());
            allPitchPointsD[i] = interpolate(dataPointsPitch, i, cinematic.KeyPoints.size());
        }

        // We generate an graph of each axis for debug reasons
        double[] time = new double[allXPoints.length];
        for(int i = 0; i < time.length; i++){
            time[i] = i;
        }

        double[] timeRough = new double[cinematic.KeyPoints.size()];
        for(int i = 0; i < time.length; i++){
            time[i] = i;
        }
    }

    @Override
    public void run() {
        player.teleport(new Location(player.getWorld(), allXPoints[tick], allYPoints[tick], allZPoints[tick], allYawPoints[tick], allPitchPoints[tick]));
        if(tick++ == (cinematic.Duration * 20)-1){
            if(cinematic.LinkedCinematic.equals("")){
                player.teleport(startingLocation);
                player.setFlying(wasFlying);
                player.setGameMode(startingGamemode);
                player.setAllowFlight(wasAllowFlight);
                player.sendMessage(ChatColor.YELLOW + "Finished Cinematic: " + ChatColor.LIGHT_PURPLE + cinematic.Name);
                this.cancel();
            }else{
                Cinematic newCinematic = core.CinematicManager.get(cinematic.LinkedCinematic);
                if(newCinematic == null){
                    player.teleport(startingLocation);
                    player.setFlying(wasFlying);
                    player.setGameMode(startingGamemode);
                    player.setAllowFlight(wasAllowFlight);
                    player.sendMessage(ChatColor.YELLOW + "Finished Cinematic: " + ChatColor.LIGHT_PURPLE + cinematic.Name);
                    this.cancel();
                }else{
                    player.sendMessage(ChatColor.YELLOW + "Switched to " + ChatColor.YELLOW + newCinematic.Name);
                    cinematic = newCinematic;
                    tick = 0;
                    PreCalculations();
                }
            }

        }
    }


    static class Data
    {
        double x, y;

        public Data(double x, double y)
        {
            super();
            this.x = x;
            this.y = y;
        }

    };

    // function to interpolate the given
// data points using Lagrange's formula
// xi corresponds to the new data point
// whose value is to be obtained n
// represents the number of known data points
    static double interpolate(Data f[], int xi, int n)
    {
        double result = 0; // Initialize result

        for (int i = 0; i < n; i++)
        {
            // Compute individual terms of above formula
            double term = f[i].y;
            for (int j = 0; j < n; j++)
            {
                if (j != i)
                    term = term*(xi - f[j].x) / (f[i].x - f[j].x);
            }

            // Add current term to result
            result += term;
        }

        return result;
    }
}
