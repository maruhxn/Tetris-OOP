package setting;

public enum GameSize {
    S(300, 400, 20, 100, 200),
    M(600, 800, 40, 200, 400),
    L(750, 1000, 50, 250, 500),
    ;

    private final int width;
    private final int height;
    private final int blockCellSize;
    private final int infoAreaWidth;
    private final int gameAreaWidth;

    GameSize(int width, int height, int blockCellSize, int infoAreaWidth, int gameAreaWidth) {
        this.width = width;
        this.height = height;
        this.blockCellSize = blockCellSize;
        this.infoAreaWidth = infoAreaWidth;
        this.gameAreaWidth = gameAreaWidth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBlockCellSize() {
        return blockCellSize;
    }

    public int getInfoAreaWidth() {
        return infoAreaWidth;
    }

    public int getGameAreaWidth() {
        return gameAreaWidth;
    }
}
