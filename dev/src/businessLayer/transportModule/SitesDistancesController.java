package businessLayer.transportModule;

import businessLayer.transportModule.bingApi.*;
import dataAccessLayer.transportModule.DistanceBetweenSites;
import javafx.util.Pair;
import objects.transportObjects.Site;
import utils.transportUtils.TransportException;

import java.io.IOException;
import java.util.*;

public class SitesDistancesController {

    private final BingAPI bingAPI;

    public SitesDistancesController(BingAPI bingAPI) {
        this.bingAPI = bingAPI;
    }

    public Point getCoordinates(Site site) throws TransportException {
        LocationByQueryResponse queryResponse;
        try {
            queryResponse = bingAPI.locationByQuery(site.address());
        } catch (IOException e) {
            throw new TransportException(e.getMessage(), e);
        }
        LocationResource[] locationResources = queryResponse.resourceSets()[0].resources();
        if (locationResources.length != 1) {
            throw new TransportException("Could not find site or found multiple sites");
        }
        return locationResources[0].point();
    }

    public List<DistanceBetweenSites> createDistanceObjects(Site site ,List<Site> sites) throws TransportException {

        Map<Pair<String,String>,Pair<Double,Double>> travelMatrix = getTravelMatrix(site,sites);

        List<DistanceBetweenSites> distances = new LinkedList<>();

        for(var entry : travelMatrix.entrySet()){
            distances.add(new DistanceBetweenSites(
                    entry.getKey().getKey(), //source
                    entry.getKey().getValue(), //destination
                    entry.getValue().getKey(), //distance
                    entry.getValue().getValue()) //duration
            );
        }
        return distances;
    }

    public Map<Pair<String,String>, Pair<Double,Double>> getTravelMatrix(Site site, List<Site> otherSites) throws TransportException {

        Map<Pair<String,String>, Pair<Double,Double>> distances = new HashMap<>();

        Point newSitePoint = new Point(site.address(), new double[]{site.latitude(),site.longitude()});

        for(Site other : otherSites){
            Point otherSitePoint = new Point(other.address(), new double[]{other.latitude(),other.longitude()});
            Pair<Point,Point> pair1 = new Pair<>(newSitePoint,otherSitePoint);
            Pair<Point,Point> pair2 = new Pair<>(otherSitePoint,newSitePoint);

            DistanceMatrixResponse response;
            try {
                response = bingAPI.distanceMatrix(List.of(pair1,pair2));
            } catch (IOException e) {
                throw new TransportException(e.getMessage(), e);
            }
            Result[] results = Arrays.stream(response.resourceSets()[0].resources()[0].results())
                    .filter(result -> result.originIndex() == result.destinationIndex())
                    .toArray(Result[]::new);

            distances.put(new Pair<>(site.address(),other.address()),
                    new Pair<>(results[0].travelDistance(),results[0].travelDuration()));
            distances.put(new Pair<>(other.address(),site.address()),
                    new Pair<>(results[1].travelDistance(),results[1].travelDuration()));
        }

        distances.put(new Pair<>(site.address(),site.address()),new Pair<>(0.0,0.0));
        return distances;
    }
}
