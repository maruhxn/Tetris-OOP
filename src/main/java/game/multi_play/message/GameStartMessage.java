package game.multi_play.message;

public class GameStartMessage {
    private String action;
    private String roomId;

    public GameStartMessage() {
    }

    public GameStartMessage(String action, String roomId) {
        this.action = action;
        this.roomId = roomId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}