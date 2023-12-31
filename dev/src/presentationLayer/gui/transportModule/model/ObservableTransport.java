package presentationLayer.gui.transportModule.model;

import presentationLayer.gui.plAbstracts.AbstractObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObservableTransport extends AbstractObservableModel {

    public int id;
    public List<String> route;
    public Map<String,Integer> destinations_itemListIds;
    public String driverId;
    public String truckId;
    public LocalDateTime departureTime;
    public int weight;
    public Map<String, LocalTime> estimatedArrivalTimes;
    public boolean manualOverride;

    @Override
    public ObservableModel getUpdate() {
        return null;
    }

    @Override
    public boolean isMatchExactly(String query) {
        return String.valueOf(id).equals(query.trim());
    }

    @Override
    public boolean isMatch(String query) {
        return String.valueOf(id).contains(query.trim()) || super.isMatch(query);
    }

    @Override
    public String getShortDescription() {
        return String.valueOf(id);
    }

    @Override
    public String getLongDescription() {
        return String.format("Transport ID: %d | Driver ID: %s | Truck ID: %s | Departure Time: %s | Weight: %d",
                id, driverId, truckId, departureTime, weight);
    }
}
