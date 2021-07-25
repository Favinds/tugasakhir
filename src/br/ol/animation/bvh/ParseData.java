package br.ol.animation.bvh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Parser class.
 * 
 *
 */
public class ParseData {
    
    private String line = "";
    private BufferedReader bufferedReader;
    
    public void load(String resource) {
        InputStream is = getClass().getResourceAsStream("/res/gedruglombo/" + resource);
        InputStreamReader isr = new InputStreamReader(is);
        bufferedReader = new BufferedReader(isr);
        nextLine();
    }
    
    public String getLine() {
        return line;
    }
    
    public void nextLine() {
        try {
            line = bufferedReader.readLine();
            if (line == null) {
                line = "";
            }
            line = line.trim();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public String[] expect(String token) {
        if (!line.startsWith(token)) {
            throw new RuntimeException("Expected '" + token + "' token !");
        }
        String[] tokens = line.split("\\s+");
        nextLine();
        return tokens;
    }
    
    public void close() {
        try {
            bufferedReader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
