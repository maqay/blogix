package net.mindengine.blogix.model;

public class Section implements Comparable<Section> {
    
    private String id;
    private String name;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int compareTo(Section otherSecction) {
        return id.compareTo(otherSecction.id);
    }

}