import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.*;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyPanel extends JPanel
{
    private final Vector<Vertex> nodesList;
    private Vector<Arc> arcsList;
    private Point sourcePoint = null;
    private Point destinationPoint = null;
    private final int maxLatitude = 652685;
    private final int minLatitude = 573929;
    private final int maxLongitude = 5018275;
    private final int minLongitude = 4945029;
    private List<Integer> path;
    List<List<Arc>> adjacencyList = null;
    private Pair pair = new Pair(new Vertex(-1, -1, -1), new Vertex(-1, -1, -1));

    private static final String FILENAME = "luxembourgMap.xml";

    static class Pair
    {
        private Vertex source;
        private Vertex destination;
        public Pair(Vertex source, Vertex destination)
        {
            this.source = source;
            this.destination = destination;
        }
    }
    public void initializeAdjacencyList()
    {
        adjacencyList = new ArrayList<>();

        for(int i = 0; i < nodesList.size(); i++)
        {
            adjacencyList.add(new ArrayList<>());
        }
    }
    public void createAdjacencyList()
    {
        for(Arc arc : arcsList)
        {
            adjacencyList.get(arc.getSourceId()).add(arc);
        }
    }
    double getDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
    private void getClosestNodes(Point sourcePoint, Point destinationPoint)
    {
        Vertex source = null;
        Vertex destination = null;
        double minSource = Integer.MAX_VALUE;
        double minDestination=Integer.MAX_VALUE;
        for(Vertex node : nodesList)
        {
            double dist1 = getDistance(node.getCoordX(), node.getCoordY(), sourcePoint.x, sourcePoint.y);
            if(dist1 < minSource)
            {
                source = node;
                minSource = dist1;
            }
            double dist2 = getDistance(node.getCoordX(), node.getCoordY(), destinationPoint.x, destinationPoint.y);
            if(dist2 < minDestination)
            {
                destination = node;
                minDestination = dist2;
            }
        }
        pair.destination = destination;
        pair.source = source;
    }
    public void readMap()
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler()
            {
                Integer id, latitude, longitude, arcFrom, arcTo, arcLenght;
                int maxLat = 652685;
                int minLat = 573929;
                int maxLong = 5018275;
                int minLong = 4945029;

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
                {
                    if(qName.equalsIgnoreCase("node"))
                    {
                        id = Integer.parseInt(attributes.getValue("id"));
                        latitude = Integer.parseInt(attributes.getValue("latitude"));
                        longitude = Integer.parseInt(attributes.getValue("longitude"));
                        if(latitude > maxLat)
                            maxLat = latitude;
                        if(latitude < minLat)
                            minLat = latitude;
                        if(longitude > maxLong)
                            maxLong = longitude;
                        if(longitude < minLong)
                            minLong = longitude;
                        Vertex node = new Vertex(id, longitude, latitude);
                        nodesList.add(node);
                    }
                    if(qName.equalsIgnoreCase("arc"))
                    {
                        arcFrom = Integer.parseInt(attributes.getValue("from"));
                        arcTo = Integer.parseInt(attributes.getValue("to"));
                        arcLenght = Integer.parseInt(attributes.getValue("length"));
                        Arc arc = new Arc(arcFrom, arcTo, arcLenght);
                        double x1 = (1300) * (nodesList.get(arcFrom).getLongitude() - minLongitude) / (maxLongitude - minLongitude);
                        double y1 = (700) * (maxLatitude - nodesList.get(arcFrom).getLatitude()) / (maxLatitude - minLatitude);
                        double x2 = (1300) * (nodesList.get(arcTo).getLongitude() - minLongitude) / (maxLongitude - minLongitude);
                        double y2 = (700) * (maxLatitude - nodesList.get(arcTo).getLatitude()) / (maxLatitude - minLatitude);
                        arc.setStart(new Point((int)x1, (int)y1));
                        arc.setEnd(new Point((int)x2, (int)y2));
                        arc.setStartNode(nodesList.elementAt(arcFrom));
                        arc.setEndNode(nodesList.elementAt(arcTo));
                        nodesList.elementAt(arcFrom).setCoordX(x1);
                        nodesList.elementAt(arcFrom).setCoordY(y1);
                        nodesList.elementAt(arcTo).setCoordX(x2);
                        nodesList.elementAt(arcTo).setCoordY(y2);
                        arcsList.add(arc);
                    }
                }
            };
            saxParser.parse("luxembourgMap.xml", handler);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public MyPanel()
    {
        nodesList = new Vector<Vertex>();
        arcsList = new Vector<Arc>();
        readMap();
        initializeAdjacencyList();
        createAdjacencyList();
        repaint();
        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                if(sourcePoint == null)
                {
                    sourcePoint = e.getPoint();
                }
                else
                {
                    if (sourcePoint != null && destinationPoint == null)
                    {
                        destinationPoint = e.getPoint();
                        getClosestNodes(sourcePoint, destinationPoint);
                        Dijkstra dijkstra = new Dijkstra(pair.source.getId(), pair.destination.getId(), nodesList.size(), adjacencyList);
                        dijkstra.findShortestPath();
                        path = dijkstra.getPath();
                        paintPath(path);
                        repaint();
                    }
                    else
                    {
                        if (sourcePoint != null && destinationPoint != null)
                        {
                            sourcePoint = e.getPoint();
                            destinationPoint = null;
                        }
                    }
                }
            }
        });
    }
    public void paintPath(List<Integer> path)
    {
        for(int i = path.size() - 1; i > 0; i--)
        {
            for(Arc arc : arcsList)
            {
                if(arc.getDestinationId() == path.get(i) && arc.getSourceId() == path.get(i - 1))
                {
                    arc.setSelected(true);
                }
            }
        }
    }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for(Arc arc : arcsList)
        {
            arc.drawArc(g);
        }
    }
}