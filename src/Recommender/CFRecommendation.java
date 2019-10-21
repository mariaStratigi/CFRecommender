package Recommender;

import Data.*;
import java.util.*;

public class CFRecommendation
{
    private LinkedHashMap<String, User> users;
    private int numPeers;
    private double th;
    private int numCommon;
    private boolean flag;
    private int peersCount;
    
    public CFRecommendation(final LinkedHashMap<String, User> users) {
        this.numPeers = 100;
        this.th = 0.7;
        this.numCommon = 5;
        this.flag = true;
        this.peersCount = 3;
        this.users = users;
    }
    
    public void setPearsonVariables(final double th, final int peers, final int common) {
        this.numPeers = peers;
        this.th = th;
        this.numCommon = common;
    }
    
    public void setPredFuncVariables(final boolean igoreRated, final int peersCount){
        this.peersCount = peersCount;
        this.flag = igoreRated;
    }
    
    /**
     * Make all the necessary operations in order to recommend items to a specific user.
     * @param id The id of the user
     * @param k How many items to recommend. If this is a negative number, recommend all available items.
     * @return The recommended items to the user.
     */
    public LinkedHashMap<String, Double> recommend(final String id, final int k) {
        if (!this.users.containsKey(id)) {
            System.out.println("The user Id: " + id + " is invalid");
            return null;
        }
        final PearsonCor pc = new PearsonCor(this.users, this.th, this.numPeers, this.numCommon);
        final LinkedHashMap<User, Double> peers = pc.getPeers(id);
        final PredictionFunc pf = new PredictionFunc(this.users.get(id), peers, this.flag, this.peersCount);
        final LinkedHashMap<String, Double> predictions = pf.predict(k);
        this.printPredictions(id, predictions);
        return predictions;
    }
    
    private void printPredictions(final String id, final LinkedHashMap<String, Double> pred) {
        System.out.println("Predicions for User: " + id);
        for (final String s : pred.keySet()) {
            System.out.println(s + "\t" + pred.get(s));
        }
    }
}