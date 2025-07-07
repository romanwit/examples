const directions = [
  [-1, -1], [-1, 0], [-1, 1],
  [0, -1],           [0, 1],
  [1, -1], [1, 0],   [1, 1]
];

function updateBoard(board, click) {
  const [row, col] = click;

  if (board[row][col] === 'M') {
    board[row][col] = 'X';
  } else {
    dfs(board, row, col);
  }

  return board;
}

function dfs(board, row, col) {
  if (!isValid(board, row, col) || board[row][col] !== 'E') return;

  const mines = countMines(board, row, col);

  if (mines > 0) {
    board[row][col] = String(mines);
  } else {
    board[row][col] = 'B';
    for (const [dr, dc] of directions) {
      dfs(board, row + dr, col + dc);
    }
  }
}

function countMines(board, row, col) {
  let count = 0;
  for (const [dr, dc] of directions) {
    const r = row + dr, c = col + dc;
    if (isValid(board, r, c) && board[r][c] === 'M') {
      count++;
    }
  }
  return count;
}

function isValid(board, row, col) {
  return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
}

/*
let board = [
  ['E', 'E', 'E', 'E', 'E'],
  ['E', 'E', 'M', 'E', 'E'],
  ['E', 'E', 'E', 'E', 'E'],
  ['E', 'E', 'E', 'E', 'E']
];

console.log(updateBoard(board, [3, 0]));

*/
