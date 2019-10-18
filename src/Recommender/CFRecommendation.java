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
    
    public void setPearsonVariables(final double th, final int peers, final int common, final int peersCount) {
        this.numPeers = peers;
        this.th = th;
        this.numCommon = common;
        this.peersCount = peersCount;
    }
    
    public void ignoreRated(final boolean flag) {
        this.flag = flag;
    }
    
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