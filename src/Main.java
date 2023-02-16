import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int[][] matP1 = new int[10][10];
    static int[][] matP2 = new int[10][10];
    static int[][][] mats = {matP1, matP2};

    static String[] ships = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    static Integer[] shipLengths = {5, 4, 3, 3, 2};
    static Scanner sc = new Scanner(System.in);
    static Integer[] arrIndex = {1, 2, 3, 4, 5};


    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            System.out.printf("Player %d, place your ships on the game field%n", i + 1);
            System.out.println();
            printMat(mats[i]);
            for (String ship : ships) {
                System.out.println("Enter the coordinates of the " + ship + " (" + shipLengths[Arrays.asList(ships).indexOf(ship)] + " cells):");
                System.out.println();
                placeShip(mats[i], shipLengths[Arrays.asList(ships).indexOf(ship)], Arrays.asList(ships).indexOf(ship) + 1);
                System.out.println();
                printMat(mats[i]);
            }
            System.out.println("Press Enter and pass the move to another player");
            sc.nextLine();
        }

        do {
            for (int i = 0; i < 2; i++) {
                printFields(mats[i == 0 ? 1 : 0], (mats[i]));
                System.out.printf("Player %d, it's your turn:%n", i + 1);
                shoot(mats[i == 0 ? 1 : 0]);
                if (isGameEnd(mats[i == 0 ? 1 : 0])) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                }
                System.out.println("Press Enter and pass the move to another player");
                sc.nextLine();
            }

        } while (!isGameEnd(mats[0]) && !isGameEnd(mats[1]));

    }

    public static boolean isShipSunk(int[][] mat, int index) {
        for (int[] row : mat) {
            for (int e : row) {
                if (e == index) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void shoot(int[][] mat) {
        boolean shoot = true;
        do {
            try {
                System.out.println("Take a shoot!");
                String coord = sc.nextLine();
                int[] shotCoord = getCoord(coord);

                switch (mat[shotCoord[0]][shotCoord[1]]) {
                    case 0 -> {
                        mat[shotCoord[0]][shotCoord[1]] = 7;
                        System.out.println("You missed!");
                    }
                    case 1, 2, 3, 4, 5 -> {
                        int index = mat[shotCoord[0]][shotCoord[1]];
                        mat[shotCoord[0]][shotCoord[1]] = 6;
                        if (isShipSunk(mat, index)) {
                            System.out.println("You sank a ship!");
                        } else {
                            System.out.println("You hit a ship!");
                        }
                    }
                    case 6, 7 -> System.out.println("You already shoot there!");

                }
                shoot = false;
            } catch (Exception e) {
                System.out.println("Coordinate index out of the map. Try new coordinate");
            }

        } while (shoot);

    }

    public static void printFields(int[][] foggedMat, int[][] playerMat) {
        printFogMat(foggedMat);
        System.out.println("---------------------");
        printMat(playerMat);
    }

    public static void printMat(int[][] mat) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        int ascii = 65;
        for (int[] row : mat) {
            System.out.print((char) ascii++);
            for (int e : row) {
                switch (e) {
                    case 0 -> System.out.print(" ~");
                    case 1, 2, 3, 4, 5 -> System.out.print(" O");
                    case 6 -> System.out.print(" X");
                    case 7 -> System.out.print(" M");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printFogMat(int[][] mat) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        int ascii = 65;
        for (int[] row : mat) {
            System.out.print((char) ascii++);
            for (int e : row) {
                switch (e) {
                    case 6 -> System.out.print(" X");
                    case 7 -> System.out.print(" M");
                    default -> System.out.print(" ~");
                }
            }
            System.out.println();
        }
    }

    public static boolean isGameEnd(int[][] mat) {
        for (int[] row : mat) {
            for (int e : row) {
                if (Arrays.asList(arrIndex).contains(e)) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void placeShip(int[][] mat, int shipLength, int shipIndex) {
        int[] coord1, coord2;
        do {
            String[] coords = sc.nextLine().split("\\s+");
            coord1 = getCoord(coords[0]);
            coord2 = getCoord(coords[1]);
        } while (!checkCoords(coord1, coord2, shipLength) || !checkShip(mat, coord1, coord2));

        if (coord1[0] == coord2[0]) {
            for (int i = Math.min(coord1[1], coord2[1]); i <= Math.max(coord1[1], coord2[1]); i++) {
                mat[coord1[0]][i] = shipIndex;
            }
        } else {
            for (int i = Math.min(coord1[0], coord2[0]); i <= Math.max(coord1[0], coord2[0]); i++) {
                mat[i][coord1[1]] = shipIndex;
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

    public static boolean checkShip(int[][] mat, int[] coord1, int[] coord2) {
        if (coord1[0] == coord2[0]) {
            for (int i = Math.min(coord1[1], coord2[1]); i <= Math.max(coord1[1], coord2[1]); i++) {
                if (Arrays.asList(arrIndex).contains(mat[coord1[0]][i])) {
                    System.out.println("Error! Cannot place a ship in another");
                    return false;
                }
                ArrayList<Integer[]> touchArea = getTouchArea(new int[]{coord1[0], i});
                for (Integer[] coord : touchArea) {
                    if (Arrays.asList(arrIndex).contains(mat[coord[0]][coord[1]])) {
                        System.out.println("Error! Cannot place a ship close to another");
                        return false;
                    }
                }
            }
        } else {
            for (int i = Math.min(coord1[0], coord2[0]); i <= Math.max(coord1[0], coord2[0]); i++) {
                if (Arrays.asList(arrIndex).contains(mat[i][coord1[1]])) {
                    System.out.println("Error! Cannot place a ship in another");
                    return false;
                }
                ArrayList<Integer[]> touchArea = getTouchArea(new int[]{i, coord1[1]});
                for (Integer[] coord : touchArea) {
                    if (Arrays.asList(arrIndex).contains(mat[coord[0]][coord[1]])) {
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
