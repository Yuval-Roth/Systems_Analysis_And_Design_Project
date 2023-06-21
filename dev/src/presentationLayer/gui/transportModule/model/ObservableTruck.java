package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import static domainObjects.transportModule.Truck.CoolingCapacity;

public class ObservableTruck extends AbstractObservableModel {

    public String id;
    public String model;
    public String weightError;
    public int baseWeight;
    public int maxWeight;
    public CoolingCapacity coolingCapacity;
    public String response;

    @Override
    public ObservableModel getUpdate() {
        return null;
    }

    @Override
    public boolean isMatch(String query) {
        return id.contains(query.trim());
    }

    @Override
    public String getShortDescription() {
        return id + " - " + model;
    }

    @Override
    public String getLongDescription() {
        return String.format("Id: %s | Model: %s | Weight: %s | Max Weight: %s | Cooling: %s",
                id, model, baseWeight, maxWeight, coolingCapacity);
    }
}
