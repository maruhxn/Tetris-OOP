package game.block;

public class Block {

    protected Position position;
    protected BlockType blockType;

    public Block(BlockType blockType) {
        this.position = new Position();
        this.blockType = blockType;
    }

    public void moveDown() {

    }

    public void moveLeft() {

    }

    public void moveRight() {
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
}
