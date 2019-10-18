/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfrecommender;

import Data.User;
import IO.ReadInput;
import Recommender.CFRecommendation;
import java.util.LinkedHashMap;

/**
 *
 * @author mariastr
 */
public class CFRecommender {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReadInput ri = new ReadInput();
        ri.readRatingsFile("C:\\Users\\mariastr\\Documents\\2M MovieLens Dataset\\ml-20m\\ratings.csv");
        
        LinkedHashMap<String, User> users = ri.getUsers();
        
        CFRecommendation rec = new CFRecommendation(users);
        rec.recommend("100", 10);
    }
    
}
