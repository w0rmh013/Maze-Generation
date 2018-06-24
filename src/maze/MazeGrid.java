package maze;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


enum Side {
    NORTH, EAST, SOUTH, WEST
}

class MazeCell {
    private int posX;
    private int posY;
    private boolean visited;
    private HashMap<Side, Boolean> walls;

    MazeCell(int posX, int posY) {
        // cell position in maze grid
        this.posX = posX;
        this.posY = posY;
        this.visited = false;

        // initialize walls: all walls are set
        this.walls = new HashMap<>(4);
        for (Side s : Side.values()) {
            this.walls.put(s, true);
        }
    }

    boolean isVisited() {
        return visited;
    }

    int getPosX() {
        return posX;
    }

    int getPosY() {
        return posY;
    }

    ArrayList<Side> getActiveWalls() {
        ArrayList<Side> active_walls = new ArrayList<>(4);
        for (Map.Entry<Side, Boolean> w : walls.entrySet()) {
            if (w.getValue()) {
                active_walls.add(w.getKey());
            }
        }
        return active_walls;
    }

    void setVisited(boolean visited) {
        this.visited = visited;
    }

    void removeWall(Side s) {
        this.walls.put(s, false);
    }

    @Override
    public String toString() {
        String walls_str = "";
        for (Map.Entry<Side, Boolean> s : walls.entrySet()) {
            if (s.getValue()) {
                if (s.getKey() == Side.NORTH) {
                    walls_str = walls_str.concat("N");
                } else if (s.getKey() == Side.EAST) {
                    walls_str = walls_str.concat("E");
                } else if (s.getKey() == Side.SOUTH) {
                    walls_str = walls_str.concat("S");
                } else {
                    walls_str = walls_str.concat("W");
                }
            }
        }
        return String.format("%d-%d,%s", posX, posY, walls_str);
    }
}

public class MazeGrid {
    private int rows;
    private int cols;
    private MazeCell[][] mazeCells;

    public MazeGrid(int rows, int cols) {
        this.rows = rows + 2;
        this.cols = cols + 2;

        // initialize maze cells. we pad the grid with null
        /*
        n n n n n
        n C C C n
        n C C C n
        n C C C n
        n n n n n
         */
        this.mazeCells = new MazeCell[this.rows][this.cols];
        for (int i = 1; i < this.rows - 1; i++) {
            for (int j = 1; j < this.cols - 1; j++) {
                this.mazeCells[i][j] = new MazeCell(i, j);
            }
        }
    }

    public MazeGrid(int size) {
        this(size, size);
    }

    int getRows() {
        return rows - 2;
    }

    int getPaddedRows() {
        return rows;
    }

    int getCols() {
        return cols - 2;
    }

    int getPaddedCols() {
        return cols;
    }

    MazeCell[][] getMazeCells() {
        return mazeCells;
    }

    private boolean allVisited() {
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (!mazeCells[i][j].isVisited()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int randomNeighbor(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }

    private HashMap<MazeCell, Side> getUnvisitedNeighbors(int posX, int posY) {
        HashMap<MazeCell, Side> neighbors = new HashMap<>(4);
        HashMap<MazeCell, Side> real_neighbors = new HashMap<>(4);

        // add clockwise (may add nulls)
        neighbors.put(mazeCells[posX - 1][posY], Side.NORTH);
        neighbors.put(mazeCells[posX][posY + 1], Side.EAST);
        neighbors.put(mazeCells[posX + 1][posY], Side.SOUTH);
        neighbors.put(mazeCells[posX][posY - 1], Side.WEST);

        for (Map.Entry<MazeCell, Side> n : neighbors.entrySet())
        {
            if (n.getKey() != null) {
                if (!n.getKey().isVisited())
                    real_neighbors.put(n.getKey(), n.getValue());
            }
        }

        return real_neighbors;
    }

    public void generate() {
        Stack<MazeCell> cell_stack = new Stack<>();
        MazeCell current_cell = mazeCells[1][1];
        
        current_cell.setVisited(true);

        while (!allVisited()) {
            HashMap<MazeCell, Side> neighbors = getUnvisitedNeighbors(current_cell.getPosX(), current_cell.getPosY());

            if (!neighbors.isEmpty()) {
                int rand = randomNeighbor(neighbors.keySet().size());
                MazeCell neighbor = (MazeCell)(neighbors.keySet().toArray()[rand]);
                Side s = neighbors.get(neighbor);  // side of neighbor relative to current cell

                cell_stack.push(current_cell);

                // remove wall between current and neighbor
                current_cell.removeWall(s);
                switch (s) {
                    case NORTH:
                        neighbor.removeWall(Side.SOUTH);
                        break;
                    case EAST:
                        neighbor.removeWall(Side.WEST);
                        break;
                    case SOUTH:
                        neighbor.removeWall(Side.NORTH);
                        break;
                    case WEST:
                        neighbor.removeWall(Side.EAST);
                        break;
                }

                current_cell = neighbor;
                current_cell.setVisited(true);
            } else if (!cell_stack.isEmpty()){
                current_cell = cell_stack.pop();
            }
        }
    }

    @Override
    public String toString() {
        String grid = String.format("%dx%d Maze Grid\n", rows - 2, cols - 2);
        String temp;

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols -1; j++) {
                temp = mazeCells[i][j].toString();
                grid = grid.concat(mazeCells[i][j].toString());
                for (int k = 0; k < 12 - temp.length(); k++) {
                    grid = grid.concat(" ");
                }
            }
            grid = grid.concat("\n");
        }
        return grid;
    }
}
