package CMDApp;

import CMDApp.Records.Site;
import CMDApp.Records.Truck;
import TransportModule.ServiceLayer.ResourceManagementService;

import java.util.HashMap;
import java.util.LinkedList;

import static CMDApp.Main.*;

public class SitesManagement {

    static ResourceManagementService rms = factory.getResourceManagementService();

    static void manageSites() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Sites management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new site");
            System.out.println("2. Update site");
            System.out.println("3. Remove site");
            System.out.println("4. View full site information");
            System.out.println("5. View all sites");
            System.out.println("6. Return to previous menu");
            int option = getInt();
            switch (option){
                case 1:
                    createSite();
                    break;
                case 2:
                    updateSite();
                    break;
                case 3:
                    removeSite();
                    break;
                case 4:
                    getSite();
                    break;
                case 5:
                    getAllSites();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void createSite() {
        System.out.println("=========================================");
        System.out.println("Enter site details:");
        String transportZone = getString("Transport zone: ");
        String address = getString("Address: ");
        String contactPhone = getString("Contact phone: ");
        String contactName = getString("Contact name: ");
        System.out.println("Site type: ");
        System.out.println("1. Logistical center");
        System.out.println("2. Branch");
        System.out.println("3. Supplier");
        int siteType = getInt();
        Site.SiteType type = null;
        switch (siteType){
            case 1:
                type = Site.SiteType.LOGISTICAL_CENTER;
                break;
            case 2:
                type = Site.SiteType.BRANCH;
                break;
            case 3:
                type = Site.SiteType.SUPPLIER;
                break;
        }
        Site newSite = new Site(transportZone, address, contactPhone, contactName, type);
        String json = JSON.serialize(newSite);
        String responseJson = rms.addSite(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) sites.put(newSite.address(), newSite);
        System.out.println("\n"+response.getMessage());
    }

    private static void updateSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to update:");
            fetchSites();
            String siteId = pickSite(true);
            if(siteId == null) return;
            Site site = sites.get(siteId);
            while(true) {
                System.out.println("=========================================");
                System.out.println("Site details:");
                printSiteDetails(site);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update contact name");
                System.out.println("2. Update contact phone");
                System.out.println("3. Return to previous menu");
                int option = getInt();
                switch (option) {
                    case 1:
                        String contactName = getString("Contact name: ");
                        updateSiteHelperMethod(site.transportZone(),
                                site.address(),
                                site.phoneNumber(),
                                contactName,
                                site.siteType()
                        );
                        break;
                    case 2:
                        String contactPhone = getString("Contact phone: ");
                        updateSiteHelperMethod(
                                site.transportZone(),
                                site.address(),
                                contactPhone,
                                site.contactName(),
                                site.siteType()
                        );
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid option!");
                        continue;
                }
                break;
            }
        }
    }

    private static void updateSiteHelperMethod(String transportZone, String address, String phoneNumber, String contactName, Site.SiteType siteType) {
        Site newSite = new Site(transportZone, address, phoneNumber, contactName, siteType);
        String json = JSON.serialize(newSite);
        String responseJson = rms.updateSite(json);
        Response<String> response = JSON.deserialize(responseJson, Response.class);
        if(response.isSuccess()) sites.put(newSite.address(), newSite);
        System.out.println("\n"+response.getMessage());
    }

    private static void removeSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to remove:");
            fetchSites();
            String siteId = pickSite(true);
            if (siteId == null) return;
            Site site = sites.get(siteId);
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this site? (y/n)");
            String option = getString();
            switch (option) {
                case "y":
                    String responseJson = rms.removeSite(siteId);
                    Response<String> response = JSON.deserialize(responseJson, Response.class);
                    if(response.isSuccess()) sites.remove(siteId);
                    System.out.println("\n"+response.getMessage());
                    break;
                case "n":
                    break;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }
        }
    }

    private static void getSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Enter address of site to view (enter 'cancel!' to return to previous menu):");
            String siteId = getString("Address: ");
            if(siteId.equals("cancel!")) return;
            Site site = sites.get(siteId);
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Enter 'done!' to return to previous menu");
            getString();
        }
    }

    private static void getAllSites() {
        System.out.println("=========================================");
        System.out.println("All sites:");
        fetchSites();
        for(Site site : sites.values()) {
            printSiteDetails(site);
            System.out.println("-----------------------------------------");
        }
        System.out.println("Press enter to return to previous menu");
        getString();
    }

    static void fetchSites() {
        String json = rms.getAllSites();
        Response<LinkedList<Site>> response = JSON.deserialize(json, Response.class);
        HashMap<String, Site> siteMap = new HashMap<>();
        for(Site site : response.getData()){
            siteMap.put(site.address(), site);
        }
        sites = siteMap;
    }

    private static void printSiteDetails(Site site) {
        System.out.println("Transport zone: " + site.transportZone());
        System.out.println("Address: " + site.address());
        System.out.println("Phone number: " + site.phoneNumber());
        System.out.println("Contact name: " + site.contactName());
    }

}
