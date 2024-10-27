package game.multi_play.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.block.BlockType;
import game.block.Position;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameActionMessage {

    @JsonProperty("action")
    private GameActionType action;

    @JsonProperty("blockType")
    private BlockType blockType;

    @JsonProperty("position")
    private Position position;

    @JsonProperty("emptyCellIndex")
    private Integer emptyCellIndex;

    @JsonProperty("score")
    private Integer score;

    @JsonProperty("level")
    private Integer level;

    public GameActionMessage() {
    }

    public GameActionMessage(GameActionType action, BlockType blockType, Position position) {
        this.action = action;
        this.blockType = blockType;
        this.position = position;
        this.emptyCellIndex = null;
        this.score = null;
        this.level = null;
    }

    private GameActionMessage(GameActionType action, int value) {
        this.action = action;
        this.blockType = null;
        this.position = null;
        this.emptyCellIndex = action == GameActionType.ATTACKED ? value : null;
        this.score = action == GameActionType.SET_SCORE ? value : null;
        this.level = action == GameActionType.SET_LEVEL ? value : null;
    }

    public static GameActionMessage attackedConfirmMessage(int emptyCellIndex) {
        return new GameActionMessage(GameActionType.ATTACKED, emptyCellIndex);
    }

    public static GameActionMessage setScoreMessage(int score) {
        return new GameActionMessage(GameActionType.SET_SCORE, score);
    }

    public static GameActionMessage setLevelMessage(int level) {
        return new GameActionMessage(GameActionType.SET_LEVEL, level);
    }

    public GameActionType getAction() {
        return action;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public Position getPosition() {
        return position;
    }

    public Integer getEmptyCellIndex() {
        return emptyCellIndex;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getLevel() {
        return level;
    }
}
