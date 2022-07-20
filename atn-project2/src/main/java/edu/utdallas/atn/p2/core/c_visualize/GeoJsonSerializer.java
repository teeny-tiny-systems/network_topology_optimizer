package edu.utdallas.atn.p2.core.c_visualize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utdallas.atn.p2.domain.Edge;
import edu.utdallas.atn.p2.domain.Graph;
import edu.utdallas.atn.p2.domain.Point;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonSerializer {

  private final List<Point> points;
  private final List<Edge> edges;

  public GeoJsonSerializer(List<Point> coordinates) {
    this(coordinates, new ArrayList<>());
  }

  public GeoJsonSerializer(Graph graph) {
    this(graph.getCoordinates(), graph.getEdges());
  }

  public GeoJsonSerializer(List<Point> coordinates, List<Edge> edges) {
    this.points = coordinates;
    this.edges = edges;
  }

  public String toJson() throws JsonProcessingException {
    FeatureCollection featureCollection = new FeatureCollection();

    for (Point point : this.points) {
      Feature _point = new Feature();
      _point.setGeometry(new org.geojson.Point(point.getLng(), point.getLat()));
      _point.setProperty("marker-size", "small");
      featureCollection.add(_point);
    }

    for (Edge edge : edges) {
      Feature _line = new Feature();
      Point start = edge.getStart();
      Point end = edge.getEnd();

      _line.setGeometry(
          new LineString(
              new LngLatAlt(start.getLng(), start.getLat()),
              new LngLatAlt(end.getLng(), end.getLat())));

      featureCollection.add(_line);
    }

    return new ObjectMapper().writeValueAsString(featureCollection);
  }
}
