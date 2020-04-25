import java.awt.Dimension;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Factory;

public class EssaiGraphe {

	public static void main(String[] args) {

		/*public ErdosRenyiGenerator(Factory<UndirectedGraph<V,E>> graphFactory,
				Factory<V> vertexFactory, Factory<E> edgeFactory,
				int numVertices,double p)*/
		Factory<UndirectedGraph<Integer,String>> grapheFactory = 
		            new Factory<UndirectedGraph<Integer,String>>() {
		                public UndirectedGraph<Integer,String> create() {
		                	UndirectedGraph<Integer, String> g = new UndirectedSparseGraph<Integer, String>();
		                	return g;
		                }
		            };
        Factory<Integer> vertexFactory = 
                new Factory<Integer>() {
                    int count;
                    public Integer create() {
                        return count++;
                }};
        Factory<String> edgeFactory = 
                new Factory<String>() {
        	String s =  "E";
        	int count;
                    public String create() {
                        count++;
                        return s+count;
                }};

		ErdosRenyiGenerator generator = new ErdosRenyiGenerator(grapheFactory, vertexFactory, edgeFactory, 100, 0.1);
		Graph g = generator.create();
		Layout<Integer, String> layout = new BalloonLayout((Forest) g);
		layout.setSize(new Dimension(1000,1000)); // sets the initial size of the space	
		BasicVisualizationServer<Integer,String> vv =
		new BasicVisualizationServer<Integer,String>(layout);
		vv.setPreferredSize(new Dimension(1000,1000)); //Sets the viewing area size
		//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		//vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

}
