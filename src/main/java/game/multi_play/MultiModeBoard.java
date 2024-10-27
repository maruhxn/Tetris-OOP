package game.multi_play;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.Board;
import game.multi_play.message.GameActionMessage;
import game.multi_play.message.GameActionType;

import java.awt.*;
import java.util.Random;

import static setting.GameSettings.GAME_SIZE;

public class MultiModeBoard extends Board {

    private ObjectMapper objectMapper;
    private GameWebSocketClient webSocketClient;

    public MultiModeBoard(GameWebSocketClient webSocketClient, ObjectMapper objectMapper) {
        this.webSocketClient = webSocketClient;
        this.objectMapper = objectMapper;
    }

    private void sendMessage(GameActionMessage message) {
        try {
            webSocketClient.sendMessage(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void spawnBlock() {
        super.spawnBlock();
        sendMessage(new GameActionMessage(GameActionType.SPAWN_NEW_BLOCK, nextBlock.getBlockType(), nextBlock.getPosition()));
    }

    @Override
    public void replaceNextWithCurr() {
        setCurrBlock(getNextBlock());
        sendMessage(new GameActionMessage(GameActionType.REPLACE_NEXT_WITH_CURR, null, null));
        spawnBlock();
    }

    @Override
    public void fixBlock() {
        super.fixBlock();
        sendMessage(new GameActionMessage(GameActionType.FIXED, currBlock.getBlockType(), currBlock.getPosition()));
    }

    @Override
    public void moveDown() {
        if (canMoveDown()) {
            currBlock.moveDown();
            sendMessage(new GameActionMessage(GameActionType.MOVE_BLOCK, currBlock.getBlockType(), currBlock.getPosition()));
        } else {
            fixBlock();
            replaceNextWithCurr();
        }
    }

    @Override
    public void moveLeft() {
        if (canMoveLeft()) {
            currBlock.moveLeft();
            sendMessage(new GameActionMessage(GameActionType.MOVE_BLOCK, currBlock.getBlockType(), currBlock.getPosition()));
        }
    }

    @Override
    public void moveRight() {
        if (canMoveRight()) {
            currBlock.moveRight();
            sendMessage(new GameActionMessage(GameActionType.MOVE_BLOCK, currBlock.getBlockType(), currBlock.getPosition()));
        }
    }

    @Override
    public void rotateBlock() {
        if (canRotate()) {
            currBlock.rotate();
            sendMessage(new GameActionMessage(GameActionType.ROTATE_BLOCK, currBlock.getBlockType(), currBlock.getPosition()));
        }
    }

    @Override
    public void superDrop() {
        while (canMoveDown()) {
            currBlock.moveDown();
        }

        sendMessage(new GameActionMessage(GameActionType.MOVE_BLOCK, currBlock.getBlockType(), currBlock.getPosition()));
        fixBlock();
        replaceNextWithCurr();
    }


    @Override
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

        sendMessage(GameActionMessage.attackedConfirmMessage(emptyCellIndex));
    }
}
