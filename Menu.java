package battleship;

import java.util.Scanner;

/**
 * @author Fulkin
 * Created on 09.03.2022
 */

public class Menu {

    Scanner sc = new Scanner(System.in);
    private static Board board = new Board();
    private Ship[] ships = new Ship[] {
         Ship.AIRCRAFT_CARRIER,
         Ship.BATTLESHIP,
         Ship.SUBMARINE,
         Ship.CRUISER,
         Ship.DESTROYER
    };

    public void start() {
        board.printGameField();
        setShips();
        System.out.println("The game starts!\n");
        board.printFogBoard();
        System.out.println("Take a shot!\n");

        takeShot();
    }

    private void takeShot() {
        while (!board.allShipDestroyed()) {
            board.takeShot(sc.nextLine());
        }
    }


    private void setShips() {
        for (Ship ship : ships) {
            System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.getName(), ship.getSize());
            while (true) {
                String[] coordinates = sc.nextLine().trim().toUpperCase().split("\\s+");
                if (board.setShip(coordinates[0], coordinates[1], ship)) {
                    break;
                }
            }
        }
    }
}
