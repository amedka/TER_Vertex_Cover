/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agape.test;

/**
 *
 * @author V. Levorato
 */
import java.util.Set;

import org.apache.commons.collections15.Factory;

import agape.algos.MIS;
import agape.io.Import;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Test {

    public static void main(String[] args) {

        Graph<String, Integer> g = Import.readNet("test.net");
        final int ei=g.getEdgeCount();
        final int vi=g.getVertexCount();


        Factory<Integer> edgeFactory = new Factory<Integer>() {
            int c=ei;
            public Integer create() {
                c++;
                return Integer.valueOf(c);
            }
        };

        Factory<String> vertexFactory = new Factory<String>() {
            int c=vi;
            public String create() {
                c++;
                return "v"+c;
            }
        };

        Factory<Graph<String,Integer>> graphFactory = new Factory<Graph<String,Integer>>() {
            public Graph<String,Integer> create() {
                return new UndirectedSparseGraph<String, Integer>();
            }
        };
        

        MIS<String,Integer> alg = new MIS<String,Integer>(graphFactory, vertexFactory, edgeFactory);

        System.out.println("Stable Maximum (V1 (min vertex))");
        double t=System.currentTimeMillis();
        Set<String> S=alg.maximumIndependentSetMoonMoser(g);
        t=System.currentTimeMillis()-t;
        System.out.println(t);
        System.out.println(S.size()+" : "+S);
    }
}
