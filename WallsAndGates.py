from collections import deque

def wallsAndGates(rooms):
    if not rooms or not rooms[0]:
        return
    
    m, n = len(rooms), len(rooms[0])
    INF = 2147483647
    queue = deque()
    
    # All gates to queue
    for i in range(m):
        for j in range(n):
            if rooms[i][j] == 0:
                queue.append((i, j))
    
    directions = [(1,0), (-1,0), (0,1), (0,-1)]
    
    while queue:
        x, y = queue.popleft()
        for dx, dy in directions:
            nx, ny = x + dx, y + dy
            # Checking room is empty(INF)
            if 0 <= nx < m and 0 <= ny < n and rooms[nx][ny] == INF:
                rooms[nx][ny] = rooms[x][y] + 1  # recording distance
                queue.append((nx, ny))
