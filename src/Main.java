import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int[][] mat = new int[10][10];
    static String[] ships = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    static Integer[] shipLengths = {5, 4, 3, 3, 2};
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        for (String ship : ships) {
            printMat();
            System.out.println("Enter the coordinates of the " + ship + " (" + shipLengths[Arrays.asList(ships).indexOf(ship)] + " cells):");
            placeShip(ship, shipLengths[Arrays.asList(ships).indexOf(ship)]);
        }
    }

    public static void printMat() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        int ascii = 65;
        for (int[] row : mat) {
            System.out.print((char) ascii++);
            for (int e : row) {
                switch (e) {
                    case 0 -> System.out.print(" ~");
                    case 1 -> System.out.print(" X");
                    case 2 -> System.out.print(" O");
                }
            }
            System.out.println();
        }
    }

    public static void placeShip(String shipName, int shipLength) {
        int[] coord1, coord2;
        do {
            String[] coords = sc.nextLine().split("\\s+");
            coord1 = getCoord(coords[0]);
            coord2 = getCoord(coords[1]);
        } while (!checkCoords(coord1, coord2, shipLength));
    }

    public static int[] getCoord(String coord) {
        int[] coords = new int[2];
        coords[0] = coord.charAt(0) - 65;
        coords[1] = Integer.parseInt(coord.substring(1)) - 1;
        return coords;
    }

    public static boolean checkCoords(int[] coord1, int[] coord2, int shipLength) {
        if (coord1[0] == coord2[0] ) {
            if (Math.abs(coord1[1] - coord2[1]) + 1 != shipLength) {
                System.out.println("Error! Wrong length  " + ships[Arrays.asList(shipLengths).indexOf(shipLength)] + "! Try again:");
                return false;
            }
            return true;
        }  else if  (coord1[1] == coord2[1]) {
            if (Math.abs(coord1[0] - coord2[0]) + 1 != shipLength) {
                System.out.println("Error! Wrong length of the " + ships[Arrays.asList(shipLengths).indexOf(shipLength)] + "! Try again:");
                return false;
            }
            return true;
        } else {
            System.out.println("Error! Wrong coordinates");
            return false;
        }
    }
}