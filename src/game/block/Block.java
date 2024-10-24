package game.block;

import static setting.GameSettings.GAME_SIZE;

public class Block {

    protected Position position;
    protected BlockType blockType;

    public Block(BlockType blockType) {
        this.position = new Position();
        this.blockType = blockType;
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

    }

    public Position getPosition() {
        return position;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public int getHeight() {
        return blockType.getShape().length;
    }

    public int getWidth() {
        if (blockType.getShape().length > 0) return blockType.getShape()[0].length;
        return 0;
    }

    public int getBottomEdge() {
        return this.position.getY() + getHeight() * GAME_SIZE.getBlockCellSize();
    }

    public int getLeftEdge() {
        return this.position.getX();
    }

    public int getRightEdge() {
        return this.position.getX() + (getWidth() * GAME_SIZE.getBlockCellSize());
    }
}
