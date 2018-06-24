package maze;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


@SuppressWarnings("Serial")
public class MazeDraw extends JFrame {
    private MazeGrid maze;
    private int cell_size;
    private int offset;

    private MazeDraw(MazeGrid maze, int cell_size) {
        this.maze = maze;
        this.cell_size = cell_size;
        this.offset = 5;

        int height = cell_size * maze.getRows();
        int width = cell_size * maze.getCols();
        this.setSize(width + offset * cell_size, height + offset * cell_size);
        this.setTitle("A-MAZE-ing Java");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new DrawGrid(), BorderLayout.CENTER);
        this.setVisible(true);
    }

    public MazeDraw(MazeGrid maze) {
        this(maze, 15);
    }

    private class DrawGrid extends JComponent {
        public void paint(Graphics g) {
            Graphics2D g2D = (Graphics2D)g;
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2D.setColor(Color.GREEN);
            g2D.fill(new Rectangle2D.Float(cell_size + offset, cell_size + offset, cell_size, cell_size));

            g2D.fill(new Rectangle2D.Float(cell_size * maze.getRows() + offset, cell_size * maze.getCols() + offset,
                    cell_size, cell_size));

            g2D.setPaint(Color.BLACK);
            for (int i = 1; i < maze.getPaddedRows() - 1; i++) {
                for (int j = 1; j < maze.getPaddedCols() - 1; j++) {
                    MazeCell temp = maze.getMazeCells()[i][j];

                    ArrayList<Line2D> walls = new ArrayList<>(4);
                    for (Side s : temp.getActiveWalls()) {
                        switch (s) {
                            case NORTH:
                                walls.add(new Line2D.Float(cell_size * j + offset, cell_size * i + offset,
                                                cell_size * (j + 1) + offset, cell_size * i + offset));
                                break;
                            case EAST:
                                walls.add(new Line2D.Float(cell_size * (j + 1) + offset, cell_size * i + offset,
                                        cell_size * (j + 1) + offset, cell_size * (i + 1) + offset));
                                break;
                            case SOUTH:
                                walls.add(new Line2D.Float(cell_size * j + offset, cell_size * (i + 1) + offset,
                                        cell_size * (j + 1) + offset, cell_size * (i + 1) + offset));
                                break;
                            case WEST:
                                walls.add(new Line2D.Float(cell_size * j + offset, cell_size * i + offset,
                                        cell_size * j + offset, cell_size * (i + 1) + offset));
                                break;
                        }
                    }
                    for (Line2D line : walls) {
                        g2D.draw(line);
                    }
                }
            }
        }
    }
}
