public class Bfvm {

    private final int cellSize = 30000; // Specified size is 30,000 bytes.
    private final byte[] cells = new byte[cellSize];
    private int datapointer = 0;
    private int progpointer = 0;
    private final char[] program;

    public Bfvm(char[] program) {
        this.program = program;
        System.out.println(">" + (run() == 0 ? "OK." : "Error"));
    }

    private int run() {
        try {
            while (progpointer < program.length) {
                switch (program[progpointer]) {
                    case '>' -> { datapointer++; if (datapointer == cellSize) datapointer = 0;}
                    case '<' -> { datapointer--; if (datapointer == -1) datapointer = cellSize-1;}
                    case '+' -> cells[datapointer]++;
                    case '-' -> cells[datapointer]--;
                    case '.' -> System.out.print((char)cells[datapointer]);
                    case ',' -> cells[datapointer] = (byte) System.in.read();
                    case '[' -> { if (cells[datapointer] == 0) progJump(1); }
                    case ']' -> { if (cells[datapointer] != 0) progJump(-1); }
                }
                progpointer++;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 1;
        }
        return 0;
    }

    private void progJump(int direction) {
        int loopdepth = 1;
        while (loopdepth > 0) {
            progpointer += direction;
            if (progpointer == -1) progpointer = program.length-1;
            else if (progpointer ==  program.length) progpointer = 0;
            char c = program[progpointer];
            // Whichever way the loop is going the loop counter incs/decs when the loop chats are found.
            loopdepth +=
            switch (c) {
                case '[' -> direction;
                case ']' -> direction*-1;
                default -> 0;
            };
        }
    }
}
