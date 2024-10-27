package game.multi_play;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.block.Position;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockActionMessage {

    @JsonProperty("action")
    private ActionType action;

    @JsonProperty("direction")
    private Direction direction;

    @JsonProperty("blockType")
    private String blockType;

    @JsonProperty("position")
    private Position position;

    @JsonProperty("rotation")
    private int rotation;

    // 기본 생성자 및 getter/setter
    public BlockActionMessage() {
    }

    public BlockActionMessage(ActionType action, String blockType, Position position, int rotation) {
        this.action = action;
        this.blockType = blockType;
        this.position = position;
        this.rotation = rotation;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    // 내부 enum 타입
    public enum ActionType {
        MOVE_BLOCK, ROTATE_BLOCK
    }

    public enum Direction {
        LEFT, RIGHT, DOWN
    }

}
