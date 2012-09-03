package net.mindengine.blogix.tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;



import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.tuple.Pair;

public class RequestSampleParser {
    private List<Pair<String, String>> samples;
    private String url = null;
    private StringBuffer response = new StringBuffer("");
    
    LineIterator lineIterator;
    public RequestSampleParser(List<Pair<String, String>> samples, LineIterator lineIterator) {
        this.samples = samples;
        this.lineIterator = lineIterator;
    }
    public RequestSampleParser process(String line) {
        if ( line.matches("[\\=]+") ) {
            if ( alreadyStarted() ) {
                done();
            }
            return new RequestSampleParser(samples, lineIterator);
        }
        else if ( alreadyStarted() ) {
            if ( response.length() > 0) {
                response.append("\n");
            }
            response.append(line);
        }
        else {
            url = line;
        }
        return this;
    }
    public void done() {
        if ( !url.trim().isEmpty()) {
            samples.add(Pair.of(url, response.toString()));
        }
    }
    private boolean alreadyStarted() {
        return url != null && !url.isEmpty();
    }
    
    
    public static List<Pair<String, String>> loadRequestChecksFromFile(File file) throws IOException {
        List<Pair<String, String>> samples = new LinkedList<Pair<String,String>>();
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        
        RequestSampleParser parser = new RequestSampleParser(samples, it);
        while( it.hasNext() ) {
            parser = parser.process(it.nextLine());
        }
        parser.done();
        
        return samples;
    }
    public static String[][] loadSamplesAsDataProvider(File file) throws IOException, URISyntaxException {
        List<Pair<String, String>> checks = loadRequestChecksFromFile(file);
        
        String[][] arr = new String[checks.size()][];
        int i=-1;
        for (Pair<String, String> check : checks) {
            i++;
            arr[i] = new String[]{check.getLeft(), check.getRight()};
        }
        return arr;
    }
}