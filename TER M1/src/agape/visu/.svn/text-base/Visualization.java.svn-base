/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.visu;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import agape.tools.Operations;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * This class is used to visualize graphs.
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 */
/**
 * @author jf
 *
 */
public class Visualization {
    
    /**
     * @author J.-F. Lalande
     * @param <V>
     */
    protected static final class ColoredSetsTransformer<V> implements Transformer<V, Paint> {
     
        Set<V> selectedNodes_ = null;
        Set<V> selectedNodes2_ = null;
        
        /**
         * Transformer that uses a group of vertices to be colored.
         * @param selectedNodes_ set of vertices
         */
        public ColoredSetsTransformer(Set<V> selectedNodes_) {
            super();
            this.selectedNodes_ = selectedNodes_;
        }
        
             
        /**
         * Transformer that uses two groups of vertices to be colored.
         * @param selectedNodes_ first set of vertices
         * @param selectedNodes2_ second set of vertices
         */
        public ColoredSetsTransformer(Set<V> selectedNodes_, Set<V> selectedNodes2_) {
            this(selectedNodes_);
            this.selectedNodes2_ = selectedNodes2_;
        }        
        
        
        /**
         * Transform a vertex n by returning the Paint element associated to it.
         * It takes into account the group of vertices the vertex n belongs to.
         * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
         */
        public Paint transform(V n) {
                     
            if (selectedNodes_.contains(n))
                return Color.YELLOW;
            
            if (selectedNodes2_ != null && (selectedNodes2_.contains(n))) 
                return Color.GREEN;
            
            return Color.red;
         }
    }
    
    /**
     * @author J.-F. Lalande
     * This class is used to transform each set of vertices into a random color.
     * It uses a Set of Set of vertices. Each set of vertices should take a color.
     * As colors are choosen randomly, two groups may have the same or colors that are similar.
     * @param <V>
     */
    protected static final class ColoredSetsTransformerSet<V> implements Transformer<V, Paint> {
        
        Set<Set<V>> verticesGroups_ = null;
        
        Color[] table = null;
              
        /**
         * Constructs the transformers and take a set of set of vertices. 
         * Each set of vertices should take a color.
         * @param verticesGroups
         */
        public ColoredSetsTransformerSet(Set<Set<V>> verticesGroups) {
            super();
            this.verticesGroups_ = verticesGroups;
            table = new Color[verticesGroups.size()];
            
            for (int i=0; i< verticesGroups.size(); i++)
                table[i] = Color.getHSBColor((float)Math.random(), (float)Math.random(), (float)Math.random());
            
        }
        
        
        /**
         * Transform a vertex n by returning the Paint element associated to it.
         * It takes into account the group of vertices the vertex n belongs to.
         * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
         */
        public Paint transform(V n) {
                     
            Iterator<Set<V>> it_set = verticesGroups_.iterator();
            int i = 0;
            while (it_set.hasNext())
            {
                Set<V> group = it_set.next();
                if (group.contains(n))
                    return table[i];
                i++;
                
            }
                                   
            return Color.black;
         }
    }


    /**
     * This method allows a fast visualization of a graph.
     * @param G graph to visualize.
     */
    public static<V,E> void showGraph(Graph<V,E> G)
    {
    	showGraph(G, new HashSet<V>());
    }
    
    /**
     * This method allows a fast visualization of a graph.
     * @param G graph to visualize.
     */
    public static<V,E> void showGraph(Graph<V,E> G, Layout<V,E> l)
    {
        showGraph(G, new HashSet<V>(), null, l);
    }
    
    /**
     * This method allows a fast visualization of a graph. 
     * If the selectednodes set is not empty, it colors the nodes into the visualization.
     * @param G graph to visualize.
     * @param selectedNodes nodes to be selected.
     */
    public static<V,E> void showGraph(Graph<V,E> G, Set<V> selectedNodes)
    {
        showGraph(G, selectedNodes, null, new FRLayout<V, E>(G));
    }
    
    /**
     * This method allows a fast visualization of a graph. 
     * If the selectednodes and selectedNodes2 sets are not empty, 
     * it colors the two groups of nodes into the visualization.
     * @param G graph to visualize.
     * @param selectedNodes first group to color
     * @param selectedNodes2 second group to color
     */
    public static<V,E> void showGraph(Graph<V,E> G, Set<V> selectedNodes, Set<V> selectedNodes2)
    {
        showGraph(G, selectedNodes, selectedNodes2, new FRLayout<V, E>(G));
    }
    
    /**
     * This method allows a fast visualization of a graph. 
     * If the selectednodes and selectedNodes2 sets are not empty, 
     * it colors the two groups of nodes into the visualization.
     * @param G graph to visualize.
     * @param selectedNodes first group to color
     * @param selectedNodes2 second group to color
     * @param l the layout to apply
     */
    public static<V,E> void showGraph(Graph<V,E> G, Set<V> selectedNodes, Set<V> selectedNodes2, Layout<V,E> l)
    {
        JFrame jf = new JFrame();
        VisualizationViewer<V,E> vv = new VisualizationViewer<V,E>(l);
        final DefaultModalGraphMouse<V,E> graphMouse = new DefaultModalGraphMouse<V,E>();

        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        vv.setBackground(Color.white);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<V>());

        jf.getContentPane().add(vv);
        jf.pack();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        
        Transformer<V, Paint> vertexPaint = new ColoredSetsTransformer<V>(selectedNodes, selectedNodes2);
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
    }
    
    /**
     * This method allows a fast visualization of a graph. 
     * If the selectednodes and selectedNodes2 sets are not empty, 
     * it colors the two groups of nodes into the visualization.
     * @param G graph to visualize.
     * @param verticesGroups the partition to use to set colors
     */
    public static<V,E> void showGraphSets(Graph<V,E> G, Set<Set<V>> verticesGroups)
    {
        JFrame jf = new JFrame();
        VisualizationViewer<V,E> vv = new VisualizationViewer<V, E>(new FRLayout<V, E>(G));
        final DefaultModalGraphMouse<V,E> graphMouse = new DefaultModalGraphMouse<V,E>();

        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        vv.setBackground(Color.white);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<V>());

        jf.getContentPane().add(vv);
        jf.pack();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        
        Transformer<V, Paint> vertexPaint = new ColoredSetsTransformerSet<V>(verticesGroups);
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
    }
        
    
    /**
     * Returns a 2-dimension array corresponding to the B-Matrix of the graph.
     * See Bagrow and al. 2008 "Portraits of complex networks".
     * @param G graph
     * @param d size of the l-shell
     * @return B-matrix
     */
    public static<V,E> int[][] getBMatrix(Graph<V,E> G, int d)
    {
        int[][] Bmatrix=new int[d][G.getVertexCount()];

        for(int i=0;i<d;i++)
        {
            for(V x:G.getVertices())
            {
                Set<V> N=Operations.getNeighbors(G, x, i+1);
                N.remove(x);
                Bmatrix[i][N.size()]=Bmatrix[i][N.size()]+1;
            }
        }

        return Bmatrix;
    }
}
