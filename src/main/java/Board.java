
public class Board {

    public Board() {
    }

    private String[][] board;

    public String[][] board(int size) {

        board = new String[size + size + 1][size + size + 1];
        int i = 0;
        int j = 0;
        int count = 1;
        while (j < size + size + 1) {
            while (i < size + size + 1) {
                board[j][i] = String.valueOf(count);
                i += 1;
                count += 1;
                if (i < size + size + 1) {
                    board[j][i] = "-";
                    i += 1;
                }

            }
            i = 0;
            j += 1;
            if (j < board.length) {
                while (i < size + size + 1) {
                    board[j][i] = ("|");
                    i += 1;
                    if (i < size + size + 1) {
                        board[j][i] = "";
                        i += 1;
                    }
                }
                i = 0;
                j += 1;
            }
        }
        return board;
    }


}
