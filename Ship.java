package battleship;

/**
 * @author Fulkin
 * Created on 09.03.2022
 */

public enum Ship {
    AIRCRAFT_CARRIER ("Aircraft Carrier",5),
    BATTLESHIP ("Battleship", 4),
    SUBMARINE ("Submarine", 3),
    CRUISER ("Cruiser", 3),
    DESTROYER ("Destroyer", 2);

    private String name;
    private int size;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
