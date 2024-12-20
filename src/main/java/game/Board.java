package game;

import game.block.Block;
import game.block.BlockType;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static setting.Constants.COMBO_SCORE_RATE;
import static setting.GameSettings.GAME_SIZE;

public class Board {

    protected Block currBlock;
    protected Block nextBlock;
    protected Color[][] lines = new Color[GAME_SIZE.getHeight() / GAME_SIZE.getBlockCellSize()][GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize()];

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

    protected void replaceNextWithCurr() {
        setCurrBlock(getNextBlock()); // next -> curr
        spawnBlock(); // Create Next block
    }

    public void fixBlock() {
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
        int[][] blockShape = currBlock.getShape();

        for (int i = 0; i < currBlock.getHeight(); ++i) {
            for (int j = 0; j < currBlock.getWidth(); ++j) {
                if (blockShape[i][j] == 1) {
                    int x = currBlock.getPosition().getX() / GAME_SIZE.getBlockCellSize() + j + xDirection;
                    int y = currBlock.getPosition().getY() / GAME_SIZE.getBlockCellSize() + i + yDirection;

                    if (x < 0 || x >= GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize() ||
                            y < 0 || y >= GAME_SIZE.getHeight() / GAME_SIZE.getBlockCellSize() || lines[y][x] != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean canMoveDown() {
        return canMove(0, 1);
    }

    public boolean canMoveLeft() {
        return canMove(-1, 0);
    }

    public boolean canMoveRight() {
        return canMove(1, 0);
    }

    public boolean canRotate() {
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

    public double clearLines() {
        double clearedUnits = 0;
        int combo = 0;

        for (int y = lines.length - 1; y >= 0; y--) {
            // 만약 라인이 모두 채워졌다면
            if (checkLineIsFull(y)) {
                clearedUnits++;
                combo++;

                pullAboveLines(y);

                // 방금 채워진 라인을 삭제했으므로 같은 위치에서 다시 검사
                y++;
            } else {
                if (combo > 1) {
                    clearedUnits += combo * COMBO_SCORE_RATE;
                }
                combo = 0;
            }
        }

        if (combo > 1) {
            clearedUnits += combo * COMBO_SCORE_RATE;
        }

        return clearedUnits;
    }

    protected void pullAboveLines(int y) {
        // 위에 있는 라인들을 모두 한 칸씩 아래로 내림
        for (int i = y; i > 0; i--) {
            lines[i] = lines[i - 1];
        }

        // 최상단 라인은 빈 라인으로 설정
        lines[0] = new Color[GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize()];
    }

    protected boolean checkLineIsFull(int lineNumber) {
        return Arrays.stream(lines[lineNumber]).allMatch(Objects::nonNull);
    }

    public boolean checkGameOver() {
        return Arrays.stream(lines[0]).anyMatch(Objects::nonNull);
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

    public void rotateBlock() {
        if (canRotate()) {
            currBlock.rotate();
        }
    }

    public void superDrop() {
        while (canMoveDown()) {
            currBlock.moveDown();
        }

        fixBlock();
        replaceNextWithCurr();
    }

    public void addRandomLineWithEmptyCell() {
        // 모든 라인 위로 하나씩 당기기
        for (int i = 1; i < lines.length; i++) {
            lines[i - 1] = lines[i];
        }

        // 최상단 라인은 빈 라인으로 설정
        Random rand = new Random();
        int emptyCellIndex = rand.nextInt(GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize());
        lines[lines.length - 1] = new Color[GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize()];
        for (int i = 0; i < lines[0].length; i++) {
            if (i == emptyCellIndex) continue;
            lines[lines.length - 1][i] = Color.LIGHT_GRAY;
        }
    }
}
