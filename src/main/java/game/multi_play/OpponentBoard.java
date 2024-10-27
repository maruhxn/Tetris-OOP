package game.multi_play;

import game.Board;
import game.block.Block;
import game.block.BlockType;
import game.block.Position;

import java.awt.*;

import static setting.GameSettings.GAME_SIZE;

public class OpponentBoard extends Board {

    public void spawnBlock(BlockType blockType) {
        this.nextBlock = new Block(blockType);
    }

    public void replaceNextWithCurr() {
        this.currBlock = this.nextBlock;
    }

    public void moveBlock(Position position) {
        currBlock.setPosition(position);
    }

    public void addLineWithEmptyCell(Integer emptyCellIndex) {
        // 모든 라인 위로 하나씩 당기기
        for (int i = 1; i < lines.length; i++) {
            lines[i - 1] = lines[i];
        }

        // 최상단 라인은 빈 라인으로 설정
        lines[lines.length - 1] = new Color[GAME_SIZE.getGameAreaWidth() / GAME_SIZE.getBlockCellSize()];
        for (int i = 0; i < lines[0].length; i++) {
            if (i == emptyCellIndex) continue;
            lines[lines.length - 1][i] = Color.LIGHT_GRAY;
        }
    }
}
