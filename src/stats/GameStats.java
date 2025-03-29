package stats;
public class GameStats {
    public static int shotsFired = 0;
    public static int damageTaken = 0;
    public static long roomStartTime = System.currentTimeMillis();

    public static void resetRoomTimer() {
        roomStartTime = System.currentTimeMillis();
    }

    public static long getRoomTime() {
        return System.currentTimeMillis() - roomStartTime;
    }
}