package IO;

import Data.*;
import java.util.logging.*;
import java.io.*;
import java.util.*;

public class ReadInput
{
    private LinkedHashMap<String, User> users;
    
    public ReadInput() {
        this.users = new LinkedHashMap<String, User>();
    }
    
    public void readRatingsFile(final String file) {
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            final List<String> tmpLines = new ArrayList<String>();
            final int chCount = 0;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("")) {
                    continue;
                }
                final String[] el = line.split(",");
                final String userId = el[0];
                final String movieId = el[1];
                final double score = Double.parseDouble(el[2]);
                if (this.users.containsKey(userId)) {
                    final User u = this.users.get(userId);
                    u.addRating(movieId, score);
                }
                else {
                    final User u = new User(userId);
                    u.addRating(movieId, score);
                    this.users.put(userId, u);
                }
            }
            in.close();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(ReadInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex2) {
            Logger.getLogger(ReadInput.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (OutOfMemoryError outOfMemoryError) {}
    }
    
    public LinkedHashMap<String, User> getUsers() {
        return this.users;
    }
}