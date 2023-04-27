package BusinessLayer.transportModule;

import objects.transportObjects.Site;

import utils.transportUtils.TransportException;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * The `SitesController` class is responsible for managing the sites in the TransportModule.
 * It provides methods for adding, removing, updating, and retrieving sites.
 */
public class SitesController {
    private final TreeMap<String, Site> sites;

    public SitesController(){
        sites = new TreeMap<>();
    }

    /**
     * Adds a site to the `SitesController`.
     *
     * @param site The site to be added.
     * @throws TransportException If the site already exists.
     */
    public void addSite(Site site) throws TransportException {
        if (siteExists(site.address()) == false) {
            sites.put(site.address(), site);
        } else {
            throw new TransportException("Site already exists");
        }
    }

    /**
     * Removes a site from the `SitesController`.
     *
     * @param address The address of the site to be removed.
     * @throws TransportException If the site is not found.
     */
    public void removeSite(String address) throws TransportException {
        if (siteExists(address) == false) {
            throw new TransportException("Site not found");
        }

        sites.remove(address);
    }

    /**
     * Retrieves a site from the `SitesController`.
     *
     * @param address The address of the site to be retrieved.
     * @return The retrieved site.
     * @throws TransportException If the site is not found.
     */
    public Site getSite(String address) throws TransportException {
        if (siteExists(address) == false) {
            throw new TransportException("Site not found");
        }

        return sites.get(address);
    }

    /**
     * Updates a site in the `SitesController`.
     *
     * @param address The address of the site to be updated.
     * @param newSite The updated site.
     * @throws TransportException If the site is not found.
     */
    public void updateSite(String address, Site newSite) throws TransportException{
        if(siteExists(address) == false) {
            throw new TransportException("Site not found");
        }

        sites.put(address, newSite);
    }

    /**
     * Retrieves all sites from the `SitesController`.
     *
     * @return A linked list of all sites.
     */
    public LinkedList<Site> getAllSites(){
        return new LinkedList<>(sites.values());
    }

    public boolean siteExists(String address) {
        return sites.containsKey(address);
    }
}