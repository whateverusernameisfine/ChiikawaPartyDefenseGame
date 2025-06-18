package util;

public class MapConfig {
    public final int mapId;                   // ✅ Add this
    public final int totalWaves;
    public final int[] zombiesPerWave;

    public MapConfig(int mapId, int[] zombiesPerWave) {
        this.mapId = mapId;
        this.totalWaves = zombiesPerWave.length;
        this.zombiesPerWave = zombiesPerWave;
    }
}
