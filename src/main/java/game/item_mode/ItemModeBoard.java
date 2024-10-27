package game.item_mode;

import game.Board;
import game.block.Block;
import game.block.BlockType;

import java.awt.*;
import java.util.Random;

import static setting.GameSettings.GAME_SIZE;

public class ItemModeBoard extends Board {

    public void spawnBlock() {
        Random random = new Random();
        switch (random.nextInt(BlockType.values().length)) {
            case 0 -> setNextBlock(new Block(BlockType.OBlock));
            case 1 -> setNextBlock(new Block(BlockType.IBlock));
            case 2 -> setNextBlock(new Block(BlockType.JBlock));
            case 3 -> setNextBlock(new Block(BlockType.LBlock));
            case 4 -> setNextBlock(new Block(BlockType.SBlock));
            case 5 -> setNextBlock(new Block(BlockType.TBlock));
            case 6 -> setNextBlock(new Block(BlockType.ZBlock));
            case 7 -> setNextBlock(new Block(BlockType.WeightItem));
        }
    }

    public void fixBlock() {
        if (currBlock.getBlockType().equals(BlockType.WeightItem)) return;

        int[][] shape = currBlock.getShape();
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

    protected boolean canMove(int xDirection, int yDirection) {
        BlockType blockType = currBlock.getBlockType();
        int[][] blockShape = currBlock.getShape();

        for (int i = 0; i < currBlock.getHeight(); ++i) {
            for (int j = 0; j < currBlock.getWidth(); ++j) {
                if (blockShape[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j + xDirection;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i + yDirection;

                    if (x < 0 || x >= GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize() ||
                            y < 0 || y >= GAME_SIZE.getHeight() / GAME_SIZE.getBlockCellSize()) {
                        return false;
                    }

                    if (lines[y][x] != null) {
                        if (blockType.equals(BlockType.WeightItem)) {
                            return true;
                        }
                        return false;
                    }
                }
            }
        }

        return true;
    }


    public boolean canMoveLeft() {
        if (currBlock.getBlockType().equals(BlockType.WeightItem)) return false;
        return canMove(-1, 0);
    }

    public boolean canMoveRight() {
        if (currBlock.getBlockType().equals(BlockType.WeightItem)) return false;
        return canMove(1, 0);
    }

    public boolean canRotate() {
        if (currBlock.getBlockType().equals(BlockType.WeightItem)) return false;

        int[][] rotatedShape = currBlock.getRotatedShape();

        for (int i = 0; i < rotatedShape.length; ++i) {
            for (int j = 0; j < rotatedShape[0].length; ++j) {
                if (rotatedShape[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i;

                    if (x < 0 || x >= GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize() ||
                            y < 0 || y >= GAME_SIZE.getHeight() / GAME_SIZE.getBlockCellSize() ||
                            lines[y][x] != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void moveDown() {
        if (canMoveDown()) {
            currBlock.moveDown();
            if (currBlock.getBlockType().equals(BlockType.WeightItem)) {
                weightBlockProgress();
            }
        } else {
            fixBlock();
            replaceNextWithCurr();
        }
    }

    public void superDrop() {
        while (canMoveDown()) {
            currBlock.moveDown();
            if (currBlock.getBlockType().equals(BlockType.WeightItem)) {
                weightBlockProgress();
            }
        }

        fixBlock();
        replaceNextWithCurr();
    }


    private void weightBlockProgress() {
        for (int i = 0; i < currBlock.getHeight(); ++i) {
            for (int j = 0; j < currBlock.getHeight(); ++j) {
                if (currBlock.getShape()[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i;
                    lines[y][x] = null;
                }
            }
        }
    }
}
