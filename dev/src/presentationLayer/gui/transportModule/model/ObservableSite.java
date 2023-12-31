package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;

import static domainObjects.transportModule.Site.SiteType;

public class ObservableSite extends AbstractObservableModel {
    public String name;
    public String address;
    public String transportZone;
    public String phoneNumber;
    public String contactName;
    public SiteType siteType;
    public double latitude;
    public double longitude;

    @Override
    public ObservableSite getUpdate() {
        return null;
    }

    @Override
    public boolean isMatchExactly(String query) {
        return name.equals(query.trim());
    }

    @Override
    public boolean isMatch(String query) {
        return name.contains(query.trim()) || super.isMatch(query);
    }

    @Override
    public String getShortDescription() {
        return name;
    }

    @Override
    public String getLongDescription() {
        return String.format("Name: %s | Address: %s | Zone: %s | Phone Number: %s | Contact Name: %s | Type: %s",
                name, address, transportZone, phoneNumber, contactName, siteType);
    }
}
