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
        
        /**
         * Read the input.
         * The input should be a comma separated and have the following format:
         * (String)userId,(String)movieId,(double)rating
         */
        ReadInput ri = new ReadInput();
        ri.readRatingsFile("C:\\Users\\mariastr\\Documents\\2M MovieLens Dataset\\ml-20m\\ratings.csv");
        
        LinkedHashMap<String, User> users = ri.getUsers();
        
        CFRecommendation rec = new CFRecommendation(users);
        
        /**
         * With these two functions you can redefine the variables of the recommender. 
         * The default values are the ones shown here.
         */
        
//        rec.setPearsonVariables(0.7, 100, 5);
//        rec.setPredFuncVariables(true, 3);
        
        /**
         * Recommend 10 items for the user with id '100'
         */
        rec.recommend("100", 10);
    }
    
}
