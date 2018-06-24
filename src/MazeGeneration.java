import maze.MazeGrid;
import maze.MazeDraw;

public class MazeGeneration {
    public static void main(String[] args) {
        MazeGrid maze = new MazeGrid(50, 51);
        maze.generate();
        new MazeDraw(maze);
    }
}
