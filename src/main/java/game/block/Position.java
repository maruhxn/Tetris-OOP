package game.block;

import setting.GameSettings;

public class Position {

    private int x;
    private int y;

    public Position() {
        this.x = GameSettings.GAME_SIZE.getGameAreaWidth() / 2;
        this.y = 0;
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