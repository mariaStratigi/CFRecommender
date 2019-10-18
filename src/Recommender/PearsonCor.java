package Recommender;

import Data.*;
import java.math.*;
import java.util.*;

public class PearsonCor
{
    private LinkedHashMap<String, User> users;
    private int numPeers;
    private double th;
    private int numCommon;
    private LinkedHashMap<User, Double> sims;
    
    public PearsonCor(final LinkedHashMap<String, User> users, final double th, final int numPeers, final int numCommon) {
        this.users = users;
        this.th = th;
        this.numPeers = numPeers;
        this.numCommon = numCommon;
        this.sims = new LinkedHashMap<User, Double>();
    }
    
    private void calcPearsonSim(final String id) {
        if (this.users.containsKey(id)) {
            final User gUser = this.users.get(id);
            for (final User p : this.users.values()) {
                double nomi = 0.0;
                double denom = 0.0;
                double sqrt1 = 0.0;
                double sqrt2 = 0.0;
                double peaSim = 0.0;
                int count = 0;
                for (final String m : gUser.getRatings().keySet()) {
                    if (p.getRatings().containsKey(m)) {
                        final double gRating = gUser.getRatings().get(m) - gUser.getMean();
                        final double oRating = p.getRatings().get(m) - p.getMean();
                        nomi += gRating * oRating;
                        sqrt1 += Math.pow(gRating, 2.0);
                        sqrt2 += Math.pow(oRating, 2.0);
                        ++count;
                    }
                }
                sqrt1 = Math.sqrt(sqrt1);
                sqrt2 = Math.sqrt(sqrt2);
                denom = sqrt1 * sqrt2;
                if (denom != 0.0) {
                    peaSim = nomi / denom;
                }
                else {
                    peaSim = 0.0;
                }
                final String key = p.getId();
                peaSim = round(peaSim);
                if (peaSim >= this.th && count > this.numCommon) {
                    this.sims.put(p, peaSim);
                }
            }
            return;
        }
        System.out.println("PearsonCor-calcPearsonSim says:\nThis user Id is not valid.");
    }
    
    private static double round(final double value) {
        BigDecimal bd = new BigDecimal(value + "");
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public LinkedHashMap<User, Double> getPeers(final String userId) {
        this.calcPearsonSim(userId);
        this.sims = (LinkedHashMap<User, Double>)sortByValues(this.sims);
        if (this.numPeers >= this.sims.size()) {
            return this.sims;
        }
        if (this.numPeers <= 0) {
            System.out.println("PearsonCor-getPeers says:\nInvalid number of peers");
            return null;
        }
        final LinkedHashMap<User, Double> peers = new LinkedHashMap<User, Double>();
        int count = 0;
        for (final User u : this.sims.keySet()) {
            peers.put(u, this.sims.get(u));
            if (++count == this.numPeers) {
                break;
            }
        }
        return peers;
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