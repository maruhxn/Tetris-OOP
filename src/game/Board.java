package game;

import game.block.Block;
import game.block.BlockType;

import java.awt.*;
import java.util.Random;

import static setting.GameSettings.GAME_SIZE;

public class Board {

    private Block currBlock;
    private Block nextBlock;
    private Color[][] lines = new Color[GAME_SIZE.getHeight() / GAME_SIZE.getBlockCellSize()][GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize()];


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
        spawnBlock(); // Create Next block
    }


    public boolean canMoveDown() {
        if (currBlock.getBottomEdge() >= GAME_SIZE.getHeight()) return false;

        int[][] blockShape = currBlock.getBlockType().getShape();

        for (int i = 0; i < currBlock.getHeight(); ++i) {
            for (int j = 0; j < currBlock.getWidth(); ++j) {
                if (blockShape[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i;
//                    // 현재 y 바로 다음 칸의 background가 채워져 있다면 충돌.
                    if (lines[y + 1][x] != null) return false;
                }
            }
        }

        return true;
    }

    public void fixBlock() {
        int[][] shape = currBlock.getBlockType().getShape();
        int h = currBlock.getHeight();
        int w = currBlock.getWidth();
        Color color = currBlock.getBlockType().getColor();

        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                if (shape[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i;
                    lines[y][x] = color;
                }
            }
        }
    }

    public boolean canMoveLeft() {
        if (currBlock.getLeftEdge() <= 0) return false;

        int[][] blockShape = currBlock.getBlockType().getShape();

        for (int i = 0; i < currBlock.getHeight(); ++i) {
            for (int j = 0; j < currBlock.getWidth(); ++j) {
                if (blockShape[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i;
                    if (lines[y][x - 1] != null) return false;
                }
            }
        }

        return true;
    }

    public boolean canMoveRight() {
        if (currBlock.getRightEdge() >= GAME_SIZE.getGameAreaWidth()) return false;

        int[][] blockShape = currBlock.getBlockType().getShape();

        for (int i = 0; i < currBlock.getHeight(); ++i) {
            for (int j = 0; j < currBlock.getWidth(); ++j) {
                if (blockShape[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i;
                    if (lines[y][x + 1] != null) return false;
                }
            }
        }

        return true;
    }

    public boolean canRotate() {
        return false;
    }

    public void clearLines() {
        checkLines();
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

    public void moveDown() {
        if (canMoveDown()) {
            currBlock.moveDown();
        } else {
            fixBlock();
            replaceNextWithCurr();
        }
    }

    public Color[][] getLines() {
        return lines;
    }

    public void moveLeft() {
        if (canMoveLeft()) {
            currBlock.moveLeft();
        }
    }

    public void moveRight() {
        if (canMoveRight()) {
            currBlock.moveRight();
        }
    }
}
