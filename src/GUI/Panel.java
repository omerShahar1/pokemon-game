package GUI;

import api.Edge;
import api.Location;
import api.Node;
import run.Agent;
import run.Game;
import run.Pokemon;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * This class panel is a part of the frame that contains the graph
 */
public class Panel extends JPanel
{
    private final Game game;
    private double xMin; //min x in graph
    private double yMin; //min y in graph
    private double xMax; //max x in graph
    private double yMax; //max y in graph
    private BufferedImage image_agent; //image to draw the agents
    private BufferedImage image_pok1; //image to draw pokemon with positive type
    private BufferedImage image_pok2; //image to draw pokemon with negative type


    public Panel(Game game)
    {
        this.game = game;
        xMin = Integer.MAX_VALUE;
        yMin = Integer.MAX_VALUE;
        xMax = Integer.MIN_VALUE;
        yMax = Integer.MIN_VALUE;
        Iterator<Node> nodeIter = this.game.getAlgo().getGraph().nodeIter();
        while (nodeIter.hasNext()) //go over all the nodes to check min and max locations.
        {
            Node node = nodeIter.next();
            xMin = Math.min(node.getLocation().x(), xMin);
            yMin = Math.min(node.getLocation().y(), yMin);
            xMax = Math.max(node.getLocation().x(), xMax);
            yMax = Math.max(node.getLocation().y(), yMax);
        }
        setPreferredSize(new Dimension(900, 600));
        try
        {
            image_pok1 = ImageIO.read(new File("images/pika.png"));
            image_pok2 = ImageIO.read(new File("images/balbazor.png"));
            image_agent = ImageIO.read(new File("images/ash.png"));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        repaint();
    }

    /**
     * This function draw the graph and the components on the panel.
     * it's draw the nodes, edges, pockemons and agents.
     */
    @Override
    public void paint(Graphics graphics)
    {
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        Iterator<Node> nodeIter = this.game.getAlgo().getGraph().nodeIter();
        while (nodeIter.hasNext()) //draw nodes
        {
            Node node = nodeIter.next();
            graphics.setColor(Color.red);
            int x = get_x_Scale(node.getLocation());
            int y = get_y_Scale(node.getLocation());
            graphics.fillOval(x, y, 14, 14);
            graphics.drawString("" + node.getKey(), x, y); //draw the node id above the node
        }
        Iterator<Edge> edgeIter = this.game.getAlgo().getGraph().edgeIter();
        while (edgeIter.hasNext()) //draw the edges
        {
            Edge currEdge = edgeIter.next();
            Node src = this.game.getAlgo().getGraph().getNode(currEdge.getSrc());
            Node dest = this.game.getAlgo().getGraph().getNode(currEdge.getDest());
            graphics.setColor(Color.BLACK);
            int x1=get_x_Scale(src.getLocation()) + 7;
            int x2=get_x_Scale(dest.getLocation()) + 7;
            int y1=get_y_Scale(src.getLocation()) + 7;
            int y2=get_y_Scale(dest.getLocation()) + 7;
            graphics.drawLine(x1, y1, x2 ,y2);
            drawArrow(graphics2D, x1, y1, x2 ,y2); //draw the edge arrow to point its direction
        }

        graphics.setColor(Color.gray);
        for (Agent agent: game.getAgents().values()) //draw agents
            graphics.drawImage(image_agent, get_x_Scale(agent.getPos()) - 7, get_y_Scale(agent.getPos()) - 7, 30, 30, null);

        for (Pokemon pokemon: game.getPokemons()) //draw pokemons
        {
            if(pokemon.getType() > 0)
                graphics.drawImage(image_pok1, get_x_Scale(pokemon.getPos()) - 6, get_y_Scale(pokemon.getPos()) - 6, 30, 30, null);
            else
                graphics.drawImage(image_pok2, get_x_Scale(pokemon.getPos()) - 6, get_y_Scale(pokemon.getPos()) - 6, 30, 30, null);
        }
    }


    /**
     * this function draw an arrow for a specific edge
     * @param x1 src x of the edge
     * @param y1 src y of the edge
     * @param x2 dest x of the edge
     * @param y2 dest y of the edge
     */
    void drawArrow(Graphics graphics, int x1, int y1, int x2, int y2)
    {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        double distanceX = x2 - x1;
        double distanceY = y2 - y1;
        double angle = Math.atan2(distanceY, distanceX);
        int len = (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        graphics2D.transform(at);
        graphics2D.fillPolygon(new int[]{len, len - 5, len - 5, len}, new int[]{0, -5, 5, 0}, 4);
    }


    /**
     * This function calculate the scale x position for specific node
     * to put in the panel
     *
     * @param loc the location we want to check
     * @return the correct scale for x in the graph
     */
    private int get_x_Scale(Location loc)
    {
        return (int)((((loc.x()-xMin)/(xMax-xMin))*getWidth()*0.9)+(0.05*getWidth()));
    }


    /**
     * This function calculate the scale y position for specific node
     * to put in the panel
     * @param loc the location we want to check
     * @return the correct scale for y in the graph
     */
    private int get_y_Scale(Location loc)
    {
        return (int)((((loc.y()-yMin)*(getHeight()-100)/(yMax-yMin))*0.9)+(0.05*(this.getHeight()-100)));
    }


}