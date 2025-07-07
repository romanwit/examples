public class Solution {
    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (dfs(board, word, 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean dfs(char[][] board, String word, int index, int row, int col) {
        if (index == word.length()) {
            return true;
        }
        
        if (row < 0 || row >= board.length ||
            col < 0 || col >= board[0].length ||
            board[row][col] != word.charAt(index)) {
            return false;
        }
        
        char temp = board[row][col];
        board[row][col] = '#';
        
        boolean found = dfs(board, word, index + 1, row - 1, col) ||
                        dfs(board, word, index + 1, row + 1, col) ||
                        dfs(board, word, index + 1, row, col - 1) ||
                        dfs(board, word, index + 1, row, col + 1);
        
        board[row][col] = temp;
        
        return found;
    }
}
