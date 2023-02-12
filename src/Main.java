import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int[][] mat = new int[10][10];
    static String[] ships = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    static Integer[] shipLengths = {5, 4, 3, 3, 2};
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        printMat();
        for (String ship : ships) {
            System.out.println("Enter the coordinates of the " + ship + " (" + shipLengths[Arrays.asList(ships).indexOf(ship)] + " cells):");
            placeShip(shipLengths[Arrays.asList(ships).indexOf(ship)]);
            printMat();
        }
        System.out.println("The game starts!");
        printEmptyMat();
        shoot();

    }

    public static void shoot() {
        boolean shoot = true;
        do {
            try {
                System.out.println("Take a shoot!");
                String coord = sc.nextLine();
                int[] shotCoord = getCoord(coord);

                switch (mat[shotCoord[0]][shotCoord[1]]) {
                    case 0 -> {
                        mat[shotCoord[0]][shotCoord[1]] = 3;
                        printFogMat(shotCoord);
                        System.out.println("You missed!");
                        printMat();
                    }
                    case 1 -> {
                        mat[shotCoord[0]][shotCoord[1]] = 2;
                        printFogMat(shotCoord);
                        System.out.println("You hit a ship!");
                        printMat();
                    }
                    case 2, 3 -> {
                        printMat();
                        System.out.println("You already shoot there!");
                    }
                }
                shoot = false;
            } catch (Exception e) {
                System.out.println("Coordinate index out of the map. Try new coordinate");
            }

        } while (shoot);

    }

    public static void printMat() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        int ascii = 65;
        for (int[] row : mat) {
            System.out.print((char) ascii++);
            for (int e : row) {
                switch (e) {
                    case 0 -> System.out.print(" ~");
                    case 1 -> System.out.print(" O");
                    case 2 -> System.out.print(" X");
                    case 3 -> System.out.print(" M");
                }
            }
            System.out.println();
        }
    }

    public static void printEmptyMat() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        int ascii = 65;
        for (int[] row : mat) {
            System.out.print((char) ascii++);
            for (int ignored : row) {
                System.out.print(" ~");
            }
        }
        System.out.println();
    }

    public static void printFogMat(int[] coord) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        int ascii = 65;
        for (int nRow = 0; nRow < 9; nRow++) {
            System.out.print((char) ascii++);
            for (int nCol = 0; nCol < 9; nCol++) {
                if (coord[0] == nRow && coord[1] == nCol) {
                    switch (mat[nRow][nCol]) {
                        case 2 -> System.out.print(" X");
                        case 3 -> System.out.print(" M");
                    }
                } else {
                    System.out.print(" ~");
                }
            }
            System.out.println();
        }
    }


    public static void placeShip(int shipLength) {
        int[] coord1, coord2;
        do {
            String[] coords = sc.nextLine().split("\\s+");
            coord1 = getCoord(coords[0]);
            coord2 = getCoord(coords[1]);
        } while (!checkCoords(coord1, coord2, shipLength) || !checkShip(coord1, coord2));

        if (coord1[0] == coord2[0]) {
            for (int i = Math.min(coord1[1], coord2[1]); i <= Math.max(coord1[1], coord2[1]); i++) {
                mat[coord1[0]][i] = 1;
            }
        } else {
            for (int i = Math.min(coord1[0], coord2[0]); i <= Math.max(coord1[0], coord2[0]); i++) {
                mat[i][coord1[1]] = 1;
            }
        }
    }


    public static ArrayList<Integer[]> getTouchArea(int[] coord) {
        ArrayList<Integer[]> touchArea = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (coord[0] + i >= 0 && coord[1] + j >= 0 && coord[0] + i <= 9 && coord[1] + j <= 9) {
                    Integer[] coordArea = {coord[0] + i, coord[1] + j};
                    touchArea.add(coordArea);
                }
            }
        }
        return touchArea;
    }

    public static boolean checkShip(int[] coord1, int[] coord2) {
        if (coord1[0] == coord2[0]) {
            for (int i = Math.min(coord1[1], coord2[1]); i <= Math.max(coord1[1], coord2[1]); i++) {
                if (mat[coord1[0]][i] == 1) {
                    System.out.println("Error! Cannot place a ship in another");
                    return false;
                }
                ArrayList<Integer[]> touchArea = getTouchArea(new int[]{coord1[0], i});
                for (Integer[] coord : touchArea) {
                    if (mat[coord[0]][coord[1]] == 1) {
                        System.out.println("Error! Cannot place a ship close to another");
                        return false;
                    }
                }
            }
        } else {
            for (int i = Math.min(coord1[0], coord2[0]); i <= Math.max(coord1[0], coord2[0]); i++) {
                if (mat[i][coord1[1]] == 1) {
                    System.out.println("Error! Cannot place a ship in another");
                    return false;
                }
                ArrayList<Integer[]> touchArea = getTouchArea(new int[]{i, coord1[1]});
                for (Integer[] coord : touchArea) {
                    if (mat[coord[0]][coord[1]] == 1) {
                        System.out.println("Error! Cannot place a ship close to another");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static int[] getCoord(String coord) {
        int[] coords = new int[2];
        coords[0] = coord.charAt(0) - 65;
        coords[1] = Integer.parseInt(coord.substring(1)) - 1;
        return coords;
    }

    public static boolean checkCoords(int[] coord1, int[] coord2, int shipLength) {
        if (coord1[0] == coord2[0]) {
            if (Math.abs(coord1[1] - coord2[1]) + 1 != shipLength) {
                System.out.println("Error! Wrong length of the  " + ships[Arrays.asList(shipLengths).indexOf(shipLength)] + "! Try again:");
                return false;
            }
            return true;
        } else if (coord1[1] == coord2[1]) {
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