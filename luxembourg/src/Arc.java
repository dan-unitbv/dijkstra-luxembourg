import java.awt.*;
import java.awt.geom.Line2D;

public class Arc {
    private int sourceId;
    private int destinationId;
    private Vertex startNode;
    private Vertex endNode;
    private Point start;
    private Point end;
    private int cost;
    private boolean selected=false;
    public Arc(int sourceId, int destinationId, int cost)
    {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.cost = cost;
    }
    public void setStart(Point start)
    {
        this.start = start;
    }
    public void setEnd(Point end)
    {
        this.end = end;
    }
    public void setStartNode(Vertex startNode)
    {
        this.startNode = startNode;
    }
    public void setEndNode(Vertex endNode)
    {
        this.endNode = endNode;
    }
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    public int getSourceId() {
        return sourceId;
    }
    public int getDestinationId() {
        return destinationId;
    }
    public int getCost() {
        return cost;
    }

    public void drawArc(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(start != null)
        {
            if(selected)
            {
                g.setColor(Color.red);
            }
            else
            {
                g.setColor(Color.black);
            }
            Shape line = new Line2D.Double(start.x, start.y, end.x, end.y);
            g2.draw(line);
        }
    }
}
