package world;
import java.util.Random;
import stats.GameStats; 

public class DungeonGenerator {
    private static Random rand = new Random();

    public static Room generateRoom() {
        Room room = new Room();

        if (GameStats.shotsFired > 10) {
            room.spawnEnemies(rand.nextInt(3) + 3); // Aggressive = more enemies
        } else {
            room.spawnEnemies(rand.nextInt(2) + 1); // Balanced
        }

        GameStats.shotsFired = 0;
        GameStats.resetRoomTimer();
        return room;
    }
}
