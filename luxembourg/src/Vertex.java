public class Vertex
{
    private int id;
    private double longitude;
    private double latitude;
    private double coordX;
    private double coordY;
    public Vertex(int id, double longitude, double latitude)
    {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public void setCoordX(double coordX)
    {
        this.coordX = coordX;
    }
    public double getCoordX()
    {
        return coordX;
    }
    public void setCoordY(double coordY)
    {
        this.coordY = coordY;
    }
    public double getCoordY()
    {
        return coordY;
    }
    public int getId()
    {
        return id;
    }
    public double getLongitude()
    {
        return longitude;
    }
    public double getLatitude()
    {
        return latitude;
    }
}
