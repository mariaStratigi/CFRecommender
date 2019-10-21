package Recommender;

import Data.*;
import java.math.*;
import java.util.*;

public class PredictionFunc
{
    private LinkedHashMap<User, Double> peers;
    private User user;
    private boolean flag;
    private int peersCount;
    private LinkedHashMap<String, Double> predictions;
    
    /**
     * The class that implements the prediction function
     * @param user The target user
     * @param peers LinkedHashMap<User,Double> of the target peers and their similarity score
     * @param flag if true the system will ignore all items that the target user has already rated (default value)
     * @param peersCount The number of peers that should have recommended the specific item, in order to take it into account.
     */
    public PredictionFunc(final User user, final LinkedHashMap<User, Double> peers, final boolean flag, final int peersCount) {
        this.peers = peers;
        this.flag = flag;
        this.peersCount = peersCount;
        this.predictions = new LinkedHashMap<>();
        this.user = user;
    }
    
    
    /**
     * The prediction function for a target user
     * @param k the number of items that the system should recommend. If it is negative recommend all possible items.
     * @return The k items with the highest prediction score.
     */
    public LinkedHashMap<String, Double> predict(final int k) {
        final HashSet<String> peersItems = this.getPeersItems();
        Set<String> ratedItems = null;
        if (this.flag) {
            ratedItems = this.user.getMoviesRated();
        }
        for (final String d : peersItems) {
            double simSum = 0.0;
            double ratSum = 0.0;
            if (this.flag && ratedItems.contains(d)) {
                continue;
            }
            int count = 0;
            for (final User p : this.peers.keySet()) {
                final double pmean = p.getMean();
                if (p.getRatings().keySet().contains(d)) {
                    ++count;
                    final double sim = this.peers.get(p);
                    final double rate = p.getRatings().get(d);
                    ratSum += sim * (rate - pmean);
                    simSum += Math.abs(sim);
                }
            }
            if (simSum == 0.0 || count <= this.peersCount) {
                continue;
            }
            double relev = this.user.getMean() + ratSum / simSum;
            relev = round(relev);
            this.predictions.put(d, relev);
        }
        this.predictions = (LinkedHashMap<String, Double>)sortByValues(this.predictions);
        if (k < 0) {
            return this.predictions;
        }
        return this.getTopK(k);
    }
    
    private HashSet<String> getPeersItems() {
        final HashSet<String> items = new HashSet<String>();
        for (final User u : this.peers.keySet()) {
            items.addAll(u.getMoviesRated());
        }
        return items;
    }
    
    private LinkedHashMap<String, Double> getTopK(final int topK) {
        final LinkedHashMap<String, Double> gList = new LinkedHashMap<String, Double>();
        int i = 0;
        for (final String id : this.predictions.keySet()) {
            if (i >= topK) {
                break;
            }
            gList.put(id, this.predictions.get(id));
            ++i;
        }
        return gList;
    }
    
    private static double round(final double value) {
        BigDecimal bd = new BigDecimal(value + "");
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    private static LinkedHashMap sortByValues(LinkedHashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        LinkedHashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}