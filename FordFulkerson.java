import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import org.jgrapht.Graph;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.ext.JGraphXAdapter;

public class FordFulkerson {
   private static final String EDGE_FILE_PATH = "roadNet-CA-with-weights.txt";  // Path to the road network file
   // private static final String EDGE_FILE_PATH = "roadNet-CA-with-weights(dummy).txt";  // Path to the road network file
   private Graph<Integer, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

   // Load edges from the CSV with weights
   public void loadEdges(String filePath) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    String line;
    
    while ((line = reader.readLine()) != null) {
        // Skip comment lines (e.g., lines starting with "#")
        if (line.startsWith("#")) {
            continue;
        }
        
        // Split the line into node IDs and weight
        String[] nodes = line.split("\t");
        
        // Parse node IDs and weight
        int u = Integer.parseInt(nodes[0]);
        int v = Integer.parseInt(nodes[1]);
        double weight = Double.parseDouble(nodes[2]);  // Assuming weight is a decimal (e.g., 1.0)
        
        // Ensure that the graph contains both nodes (vertices)
        this.graph.addVertex(u);
        this.graph.addVertex(v);
        
        // Add an edge if it does not already exist
        DefaultWeightedEdge edge = this.graph.getEdge(u, v);
        
        // If the edge does not exist, add a new one and set the weight
        if (edge == null) {
            edge = this.graph.addEdge(u, v);
            this.graph.setEdgeWeight(edge, weight);
        } else {
            // If the edge exists, update its weight (optional, depending on your logic)
            this.graph.setEdgeWeight(edge, weight);
        }
    }
    reader.close();
}


   // Modify this function to print intermediate flow values
   public double calculateMaxFlow(int source, int sink) {
      EdmondsKarpMFImpl<Integer, DefaultWeightedEdge> maxFlow = new EdmondsKarpMFImpl<>(this.graph);

      // Calculate maximum flow once
      double maxFlowValue = maxFlow.calculateMaximumFlow(source, sink);
      
      // Get the flow details after calculating the maximum flow
      System.out.println("Flow details for each edge:");
      Map<DefaultWeightedEdge, Double> flowMap = maxFlow.getFlowMap();

      // Print out intermediate flow values (including final values)
      for (DefaultWeightedEdge edge : this.graph.edgeSet()) {
         double edgeFlow = flowMap.getOrDefault(edge, 0.0);
         System.out.println("Edge " + this.graph.getEdgeSource(edge) + " -> " + this.graph.getEdgeTarget(edge)
                 + " | Capacity: " + this.graph.getEdgeWeight(edge));
      }

      System.out.println("------------------------------------");

      return maxFlowValue;
   }

   public static void main(String[] args) {
      try {
         FordFulkerson network = new FordFulkerson();
         network.loadEdges(EDGE_FILE_PATH);  // Load edges with weights from the file

         int source = 0;  // Example source node
         int sink = network.graph.vertexSet().stream().max(Integer::compare).orElse(0);  // Example sink (the highest node ID)

         // Calculate and print max flow with intermediate flow details
         double maxFlow = network.calculateMaxFlow(source, sink);
         System.out.println("Max flow from " + source + " to " + sink + ": " + maxFlow);

         network.visualizeNetwork();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void visualizeNetwork() {
      // Create JGraphXAdapter for JGraphT
      JGraphXAdapter<Integer, DefaultWeightedEdge> graphAdapter = new JGraphXAdapter<>(this.graph);

      // Arrange graph nodes in a circular layout
      mxCircleLayout layout = new mxCircleLayout(graphAdapter);
      layout.execute(graphAdapter.getDefaultParent());

      // Save the visualization as an image file
      BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, java.awt.Color.WHITE, true, null);
      try {
         File imgFile = new File("network-visualization.png");
         ImageIO.write(image, "PNG", imgFile);
         System.out.println("Network visualization saved as network-visualization.png");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
