package game.multi_play.message;

public enum GameActionType {
    MOVE_BLOCK,
    ROTATE_BLOCK,
    FIXED,
    SPAWN_NEW_BLOCK,
    REPLACE_NEXT_WITH_CURR,
    ATTACK_LINES,
    ATTACKED,
    GAME_OVER,
    PAUSE,
    SET_SCORE,
    SET_LEVEL,
    CONTINUE
}