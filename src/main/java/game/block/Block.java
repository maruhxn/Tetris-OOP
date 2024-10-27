package game.block;

import static setting.GameSettings.GAME_SIZE;

public class Block {

    private Position position;
    private BlockType blockType;
    private int[][] shape;

    public Block(BlockType blockType) {
        this.position = blockType.equals(BlockType.WeightItem) ? Position.spawnRandomPosition() : new Position();
        this.blockType = blockType;
        this.shape = blockType.getShape();
    }

    public void moveDown() {
        this.position.setY(this.position.getY() + GAME_SIZE.getBlockCellSize());
    }

    public void moveLeft() {
        this.position.setX(this.position.getX() - GAME_SIZE.getBlockCellSize());
    }

    public void moveRight() {
        this.position.setX(this.position.getX() + GAME_SIZE.getBlockCellSize());
    }

    public void rotate() {
        this.shape = getRotatedShape().clone();
    }

    public int[][] getRotatedShape() {
        int n = shape.length;
        int m = shape[0].length;

        int[][] rotatedShape = new int[m][n];

        // 시계 방향으로 90도 회전
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotatedShape[j][n - 1 - i] = shape[i][j];
            }
        }
        return rotatedShape;
    }

    public Position getPosition() {
        return position;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public int getHeight() {
        return getShape().length;
    }

    public int getWidth() {
        if (getShape().length > 0) return getShape()[0].length;
        return 0;
    }

    public int[][] getShape() {
        return shape;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
