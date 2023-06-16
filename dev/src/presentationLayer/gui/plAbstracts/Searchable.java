package presentationLayer.gui.plAbstracts;

public interface Searchable {
    boolean isMatch(String query);
    String getShortDescription();
    String getLongDescription();
}
