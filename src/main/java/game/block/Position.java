package game.block;

import setting.GameSettings;

import java.util.Random;

public class Position {

    private int x;
    private int y;

    public Position() {
        this.x = GameSettings.GAME_SIZE.getGameAreaWidth() / 2;
        this.y = 0;
    }

    private Position(int x) {
        this.x = x;
        this.y = 0;
    }

    public static Position spawnRandomPosition() {
        return new Position(new Random().nextInt(GameSettings.GAME_SIZE.getGameAreaWidth()) - GameSettings.GAME_SIZE.getBlockCellSize() * 3);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
