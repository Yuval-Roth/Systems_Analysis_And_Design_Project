package presentationLayer.gui.plUtils;

import presentationLayer.gui.plAbstracts.Searchable;

public class SearchableString implements Searchable {

    private String string;

    public SearchableString(String string) {
        this.string = string;
    }

    @Override
    public boolean isMatch(String query) {
        return string.contains(query);
    }

    @Override
    public String getShortDescription() {
        return string;
    }

    @Override
    public String getLongDescription() {
        return null;
    }
}