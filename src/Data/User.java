package Data;

import java.util.*;

public class User
{
    private final String id;
    private LinkedHashMap<String, Double> ratings;
    private HashSet<String> moviesRated;
    private double mean;
    
    public User(final String id) {
        this.id = id;
        this.ratings = new LinkedHashMap<>();
        this.moviesRated = new HashSet<>();
    }
    
    public String getId() {
        return this.id;
    }
    
    public LinkedHashMap<String, Double> getRatings() {
        return this.ratings;
    }
    
    public Set<String> getMoviesRated() {
        return this.moviesRated;
    }
    
    public void addRating(final String movieId, final double score) {
        this.moviesRated.add(movieId);
        this.ratings.put(movieId, score);
    }
    
    public double getMean() {
        return this.mean;
    }
    
    /**
     * Calculate the mean rating score of the user.
     * Used in similarity and prediction functions.
     */
    public void setMean() {
        double sum = 0.0;
        for (final Double d : this.ratings.values()) {
            sum += d;
        }
        this.mean = sum / this.ratings.size();
    }
    
    public int getNumRatings() {
        return this.ratings.size();
    }
}