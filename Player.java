package battleship;

/**
 * @author Fulkin
 * Created on 09.03.2022
 */

public class Player {
    private final Board board;

    public Player(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public boolean allShipsDestroyed() {
        return board.allShipsDestroyed();
    }

    public void printFields() {
        board.printAllFields();
    }

    public void takeATurn(Player otherPlayer, String coordinate) {
        board.takeShot(otherPlayer.getBoard(), coordinate);
    }
}
