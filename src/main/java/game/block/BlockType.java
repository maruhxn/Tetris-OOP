package game.block;

import java.awt.*;

public enum BlockType {
    IBlock(
            new int[][]{
                    {1, 1, 1, 1}
            },
            Color.CYAN
    ),
    JBlock(
            new int[][]{
                    {1, 1, 1},
                    {0, 0, 1}
            },
            Color.BLUE
    ),
    LBlock(
            new int[][]{
                    {1, 1, 1},
                    {1, 0, 0}
            },
            Color.ORANGE
    ),
    OBlock(
            new int[][]{
                    {1, 1},
                    {1, 1}
            },
            Color.YELLOW
    ),
    SBlock(
            new int[][]{
                    {0, 1, 1},
                    {1, 1, 0}
            },
            Color.GREEN
    ),
    TBlock(
            new int[][]{
                    {0, 1, 0},
                    {1, 1, 1}
            },
            Color.MAGENTA
    ),
    ZBlock(
            new int[][]{
                    {1, 1, 0},
                    {0, 1, 1}
            },
            Color.RED
    ),

    ;

    private int[][] shape;
    private Color color;

    BlockType(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
}
