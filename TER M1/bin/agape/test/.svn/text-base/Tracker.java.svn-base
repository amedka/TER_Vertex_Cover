package agape.test;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author V. Levorato
 * This class allows to follow an algorithm and memorizes how many times
 * the algorithm passes through a branch or an heuristic.
 */
public class Tracker {


    HashMap<String,Integer> branchmap;

    public Tracker() {  branchmap=new HashMap<String,Integer>(); }

    /**
     * Increase by one the "index" trace.
     * @param index point to survey.
     */
    public void increase(String index)
    {
        if(branchmap.get(index)!=null)
        {
             int v=branchmap.get(index);
             branchmap.put(index, v+1);
        }
        else
            branchmap.put(index, 1);
    }

    /**
     * Give a trace a specific value.
     * @param index point to survey.
     * @param value value to give to point.
     */
    public void set(String index, int value)
    {
        branchmap.put(index, value);
    }

    /**
     * Return the amount of times the aimed method passed through a point.
     * @param index point to get.
     * @return number of times the algorithm passed through the given point.
     */
    public int get(String index)
    {
        return branchmap.get(index);
    }

    @Override
    public String toString()
    {
        String res="";
        for(Entry<String,Integer> entry : branchmap.entrySet())
        {
            res+=entry.getKey()+":"+entry.getValue()+"\n";
        }

        return res;

    }



}
