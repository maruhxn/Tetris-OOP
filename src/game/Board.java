package game;

import game.block.Block;
import game.block.BlockType;

import java.util.Random;

public class Board {

    private Block currBlock;
    private Block nextBlock;


    public void setUp() {
        spawnBlock();
        replaceNextWithCurr();
    }

    public void spawnBlock() {
        Random random = new Random();
        switch (random.nextInt(7)) {
            case 0 -> setNextBlock(new Block(BlockType.OBlock));
            case 1 -> setNextBlock(new Block(BlockType.IBlock));
            case 2 -> setNextBlock(new Block(BlockType.JBlock));
            case 3 -> setNextBlock(new Block(BlockType.LBlock));
            case 4 -> setNextBlock(new Block(BlockType.SBlock));
            case 5 -> setNextBlock(new Block(BlockType.TBlock));
            case 6 -> setNextBlock(new Block(BlockType.ZBlock));
        }
    }

    private void replaceNextWithCurr() {
        setCurrBlock(getNextBlock()); // next -> curr
    }


    public boolean canMoveDown(Block block) {
        return false;
    }

    public void fixBlock(Block block) {
    }

    public boolean canMoveLeft(Block block) {
        return false;
    }

    public boolean canMoveRight(Block block) {
        return false;
    }

    public boolean canRotate(Block block) {
        return false;
    }

    public void clearLines() {
    }

    public void checkLines() {
    }

    public boolean isGameOver() {
        return false;
    }

    public Block getCurrBlock() {
        return currBlock;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setCurrBlock(Block currBlock) {
        this.currBlock = currBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }
}
