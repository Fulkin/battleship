package battleship;

import java.util.Scanner;

/**
 * @author Fulkin
 * Created on 09.03.2022
 */

public class Game {

    Scanner sc = new Scanner(System.in);
    private final Player firstPlayer = new Player(new Board());
    private final Player secondPlayer = new Player(new Board());
    private static final String PASS_TURN_MESSAGE = "Press Enter and pass the move to another player";

    private Ship[] ships = new Ship[] {
         Ship.AIRCRAFT_CARRIER,
         Ship.BATTLESHIP,
         Ship.SUBMARINE,
         Ship.CRUISER,
         Ship.DESTROYER
    };

    public void start() {
        System.out.println("Player 1, place your ships on the game field");
        setShips(firstPlayer);
        System.out.println(PASS_TURN_MESSAGE);
        sc.nextLine();

        System.out.println("Player 2, place your ships on the game field");
        setShips(secondPlayer);
        System.out.println(PASS_TURN_MESSAGE);
        sc.nextLine();

        Player current = firstPlayer;
        Player other = secondPlayer;
        while (true) {
            printBeginTurnMessage(current);
            current.printFields();
            current.takeATurn(other, sc.nextLine());

            boolean isFinished = firstPlayer.allShipsDestroyed() || secondPlayer.allShipsDestroyed();
            if (isFinished) {
                break;
            }

            current = current == firstPlayer ? secondPlayer : firstPlayer;
            other = current == firstPlayer ? secondPlayer : firstPlayer;
            System.out.println(PASS_TURN_MESSAGE);
            sc.nextLine();
        }

        System.out.println("You sank the last ship. You won. Congratulations!");

        /*board.printGameField();
        setShips();
        System.out.println("The game starts!\n");
        board.printFogBoard();
        System.out.println("Take a shot!\n");

        takeShot();*/
    }

    private void printBeginTurnMessage(Player currentPlayer) {
        if (currentPlayer == firstPlayer) {
            System.out.println("Player 1, it's your turn:");
        } else {
            System.out.println("Player 2, it's your turn:");
        }
    }

    private void setShips(Player player) {
        player.getBoard().printOwnField();
        for (Ship ship : ships) {
            System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.getName(), ship.getSize());
            while (true) {
                String[] coordinates = sc.nextLine().trim().toUpperCase().split("\\s+");
                if (player.getBoard().setShip(coordinates[0], coordinates[1], ship)) {
                    break;
                }
            }
        }
    }
}
