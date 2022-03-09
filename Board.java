package battleship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Fulkin
 * Created on 09.03.2022
 */

public class Board {

    private static final int SIZE_BOARD = 10;
    private static final char CELL_FOG_OF_WAR = '~';
    private static final char CELL_SHIP = 'O';
    private static final char CELL_SHIP_HIT = 'X';
    private static final char CELL_MISSED = 'M';
    private final char[][] ownField;
    private final char[][] enemyField;
    private final Map<Ship, Set<Point>> ships = new HashMap<>();

    public Board() {
        ownField = new char[SIZE_BOARD][SIZE_BOARD];
        enemyField = new char[SIZE_BOARD][SIZE_BOARD];
        initializeBoard(ownField);
        initializeBoard(enemyField);
    }

    public void printAllFields() {
        printField(enemyField);
        System.out.println("---------------------");
        printField(ownField);
    }

    public boolean setShip(String first, String second, Ship ship) {
        Point firstCoord = getCoordinate(first);
        Point secondCoord = getCoordinate(second);
        setSecondCoordLarger(firstCoord, secondCoord);
        if (!checkPlacement(firstCoord, secondCoord, ship)) {
            return false;
        }
        placeShipOnBoard(firstCoord, secondCoord, ship);
        printField(ownField);
        return true;
    }

    public void takeShot(Board enemyBoard, String coordinates) {
        Point shot = getCoordinate(coordinates);
        int x = shot.getX();
        int y = shot.getY();
        if (x == -1 || y == -1) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
        }
        if (enemyBoard.ownField[x][y] == CELL_SHIP || enemyBoard.ownField[x][y] == CELL_SHIP_HIT) {
            enemyBoard.ownField[x][y] = CELL_SHIP_HIT;
            enemyField[x][y] = CELL_SHIP_HIT;
            hitShip(shot,enemyBoard);
        } else {
            enemyBoard.ownField[x][y] = CELL_MISSED;
            enemyField[x][y] = CELL_MISSED;
            System.out.println("You missed!\n");
        }
    }

    private void hitShip(Point shot , Board enemyBoard) {
        enemyBoard.ships.values().forEach(points -> points.remove(shot));
        var iter = enemyBoard.ships.keySet().iterator();

        while (iter.hasNext()) {
            var shipM = iter.next();
            if (enemyBoard.ships.get(shipM).isEmpty()) {
                iter.remove();
                if (allShipsDestroyed()) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                } else {
                    System.out.println("You sank a ship! Specify a new target:");
                }
                return;
            }
        }
        System.out.println("You hit a ship!\n");
    }

    public void printEnemyBoard() {
        printField(enemyField);
    }

    public void printOwnField() {
        printField(ownField);
    }

    public boolean allShipsDestroyed() {
        return ships.isEmpty();
    }

    private void initializeBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = CELL_FOG_OF_WAR;
            }
        }
    }

    private void placeShipOnBoard(Point firstPoint, Point secondPoint, Ship ship) {
        ships.putIfAbsent(ship, new HashSet<>());
        if (firstPoint.getY() == secondPoint.getY()) {
            for (int i = firstPoint.getX(); i <= secondPoint.getX(); i++) {
                ownField[i][firstPoint.getY()] = CELL_SHIP;
                ships.get(ship).add(new Point(i, firstPoint.getY()));
            }
        } else {
            for (int i = firstPoint.getY(); i <= secondPoint.getY(); i++) {
                ownField[firstPoint.getX()][i] = CELL_SHIP;
                ships.get(ship).add(new Point(firstPoint.getX(), i));
            }
        }
    }

    private Point getCoordinate(String strCoordinate) {
        int x = strCoordinate.charAt(0) - 65;
        int y = strCoordinate.charAt(1) - 49;
        try {
            if (x > 9) {
                x = -1;
            }
            if (strCoordinate.charAt(2) == '0') {
                y += 9;
            } else {
                y = -1;
            }

        } catch (StringIndexOutOfBoundsException e) {

        }
        return new Point(x, y);
    }

    private void setSecondCoordLarger(Point firstPoint, Point secondPoint) {
        if (firstPoint.getX() > secondPoint.getX() || firstPoint.getY() > secondPoint.getY()) {
            Point tempPoint = new Point(firstPoint.getX(), firstPoint.getY());
            firstPoint.setX(secondPoint.getX());
            firstPoint.setY(secondPoint.getY());

            secondPoint.setX(tempPoint.getX());
            secondPoint.setY(tempPoint.getY());
        }
    }

    private boolean checkPlacement(Point firstPoint, Point secondPoint, Ship ship) {
        if (firstPoint.getX() != secondPoint.getX() && firstPoint.getY() != secondPoint.getY()) {
            System.out.printf("Error! Wrong ship location! Try again:%n");
            return false;
        } else if (checkSizeShip(firstPoint, secondPoint, ship)) {
            System.out.printf("Error! Wrong length of the %s! Try again:%n", ship.getName());
            return false;
        } else if (!checkBorders(firstPoint, secondPoint)) {
            System.out.println("Error! You placed it too close to another one. Try again:");
            return false;
        }
        return true;
    }

    private boolean checkSizeShip(Point firstPoint, Point secondPoint, Ship ship) {
        return ((secondPoint.getX() - firstPoint.getX() > ship.getSize() - 1)
                || (secondPoint.getY() - firstPoint.getY() > ship.getSize() - 1)) ||
                ((secondPoint.getX() - firstPoint.getX() < ship.getSize() - 1)
                        && (secondPoint.getY() - firstPoint.getY() < ship.getSize() - 1));
    }

    private boolean checkBorders(Point firstPoint, Point secondPoint) {
        Point leftUpperCorner = getLeftUpperCornerOfCheckingArea(firstPoint);
        Point bottomRightCorner = getBottomRightCornerOfCheckingArea(secondPoint);
        for (int i = leftUpperCorner.getX(); i <= bottomRightCorner.getX(); i++) {
            for (int j = leftUpperCorner.getY(); j <= bottomRightCorner.getY(); j++) {
                if (ownField[i][j] != CELL_FOG_OF_WAR) {
                    return false;
                }
            }
        }
        return true;
    }

    private Point getBottomRightCornerOfCheckingArea(Point point) {
        int bottomRightCornerX = point.getX() == SIZE_BOARD - 1 ? point.getX() : point.getX() + 1;
        int bottomRightCornerY = point.getY() == SIZE_BOARD - 1 ? point.getX() : point.getY() + 1;
        return new Point(bottomRightCornerX, bottomRightCornerY);
    }

    private Point getLeftUpperCornerOfCheckingArea(Point point) {
        int leftUpperCornerX = point.getX() == 0 ? 0 : point.getX() - 1;
        int leftUpperCornerY = point.getY() == 0 ? 0 : point.getY() - 1;
        return new Point(leftUpperCornerX, leftUpperCornerY);
    }

    private void printField(char[][] board) {
        char letters = 'A';
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (char[] chars : board) {
            for (int l = 0; l < board[0].length; l++) {
                if (l == 0) {
                    System.out.print(letters++ + " ");
                }
                System.out.print(chars[l] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
