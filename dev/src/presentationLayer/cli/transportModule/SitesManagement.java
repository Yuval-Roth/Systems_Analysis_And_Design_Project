package presentationLayer.cli.transportModule;

import domainObjects.transportModule.Site;
import serviceLayer.transportModule.ResourceManagementService;
import utils.JsonUtils;
import utils.Response;

import java.util.Collection;
import java.util.Comparator;

public class SitesManagement {

    private final CLIData cliData;
    private final ResourceManagementService rms;

    public SitesManagement(CLIData transportAppData, ResourceManagementService rms) {
        this.cliData = transportAppData;
        this.rms = rms;
    }
    
    void manageSites() {
        while(true){
            System.out.println("=========================================");
            System.out.println("Sites management");
            System.out.println("Please select an option:");
            System.out.println("1. Create new site");
            System.out.println("2. Update site");
            System.out.println("3. View full site information");
            System.out.println("4. View all sites");
            System.out.println("5. Return to previous menu");
            int option = cliData.readInt();
            switch (option) {
                case 1 -> createSite();
                case 2 -> updateSite();
                case 3 -> viewSite();
                case 4 -> viewAllSites();
                case 5 -> {
                    return;
                }
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void createSite() {
        System.out.println("=========================================");
        System.out.println("Enter site details:");
        String name = cliData.readLine("Name: ");
        String address = cliData.readLine("Address: ");
        String transportZone = cliData.readLine("Transport zone: ");
        String contactPhone = cliData.readLine("Contact phone: ");
        String contactName = cliData.readLine("Contact name: ");
        System.out.println("Site type: ");
        System.out.println("1. Logistical center");
        System.out.println("2. Branch");
        System.out.println("3. Supplier");
        int siteType = cliData.readInt();
        Site.SiteType type;
        switch (siteType) {
            case 1 -> type = Site.SiteType.LOGISTICAL_CENTER;
            case 2 -> type = Site.SiteType.BRANCH;
            case 3 -> type = Site.SiteType.SUPPLIER;
            default -> {
                System.out.println("\nInvalid option!");
                return;
            }
        }
        Site newSite = new Site(name, address, transportZone, contactPhone, contactName, type);
        String json = newSite.toJson();
        final String[] responseJson = {null};
        Thread worker = new Thread(()-> {
            String _response = rms.addSite(json);
            responseJson[0] = _response;
        });
        worker.start();
        System.out.print("Creating site, sit tight! ...");
        while(worker.isAlive()){
            System.out.print(".");
            try {
                worker.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        Response response = JsonUtils.deserialize(responseJson[0], Response.class);
        if(response.success()) {
            Site addedSite = Site.fromJson(response.data());
            cliData.sites().put(addedSite.name(), addedSite);
        }
        System.out.println("\n"+response.message());
    }

    private void updateSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to update:");
            Site site = cliData.pickSite(true);
            if(site == null) {
                return;
            }
            while(true) {
                System.out.println("=========================================");
                System.out.println("Site details:");
                printSiteDetails(site);
                System.out.println("=========================================");
                System.out.println("Please select an option:");
                System.out.println("1. Update contact name");
                System.out.println("2. Update contact phone");
                System.out.println("3. Return to previous menu");
                int option = cliData.readInt();
                switch (option) {
                    case 1 -> {
                        String contactName = cliData.readLine("Contact name: ");
                        updateSiteHelperMethod(
                                site.name(),
                                site.address(),
                                site.transportZone(),
                                site.phoneNumber(),
                                contactName,
                                site.siteType()
                        );
                    }
                    case 2 -> {
                        String contactPhone = cliData.readLine("Contact phone: ");
                        updateSiteHelperMethod(
                                site.name(),
                                site.address(),
                                site.transportZone(),
                                contactPhone,
                                site.contactName(),
                                site.siteType()
                        );
                    }
                    case 3 -> {
                        return;
                    }
                    default -> {
                        System.out.println("\nInvalid option!");
                        continue;
                    }
                }
                break;
            }
        }
    }

    private void updateSiteHelperMethod(String name, String address, String transportZone, String phoneNumber, String contactName, Site.SiteType siteType) {
        Site newSite = new Site(name, address, transportZone, phoneNumber, contactName, siteType);
        String json = newSite.toJson();
        String responseJson = rms.updateSite(json);
        Response response = JsonUtils.deserialize(responseJson, Response.class);
        if(response.success()) {
            Site updatedSite = Site.fromJson(response.data());
            cliData.sites().put(updatedSite.name(), updatedSite);
        }
        System.out.println("\n"+response.message());
    }

    /**
     * @deprecated currently not fully supported and could cause unexpected behavior
     */
    @Deprecated
    private void removeSite() {
        while(true) {
            System.out.println("=========================================");
            System.out.println("Select site to remove:");
            Site site = cliData.pickSite(true);
            if (site == null) {
                return;
            }
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Are you sure you want to remove this site? (y/n)");
            String option = cliData.readLine();
            switch (option) {
                case "y"-> {
                    String json = site.toJson();
                    String responseJson = rms.removeSite(json);
                    Response response = JsonUtils.deserialize(responseJson, Response.class);
                    if(response.success()) {
                        cliData.sites().remove(site.name());
                    }
                    System.out.println("\n"+response.message());
                }
                case "n"-> {}
                default -> System.out.println("\nInvalid option!");
            }
        }
    }

    private void viewSite() {
        while(true) {
            System.out.println("=========================================");
            String siteName = cliData.readLine("Enter name of site to view (enter 'done!' to return to previous menu): ");
            if(siteName.equals("done!")) {
                return;
            }
            if(cliData.sites().containsKey(siteName) == false) {
                System.out.println("Site not found!");
                continue;
            }
            Site site = cliData.sites().get(siteName);
            System.out.println("=========================================");
            System.out.println("Site details:");
            printSiteDetails(site);
            System.out.println("=========================================");
            System.out.println("Enter 'done!' to return to previous menu");
            cliData.readLine();
        }
    }

    private void viewAllSites() {
        System.out.println("=========================================");
        System.out.println("All sites:");
        Collection<Site> _sites = cliData.sites().values().stream().sorted(new SitesComparator()).toList();
        for(Site site : _sites) {
            printSiteDetails(site);
            System.out.println("-----------------------------------------");
        }
        System.out.println("\nEnter 'done!' to return to previous menu");
        cliData.readLine();
    }

    void printSiteDetails(Site site) {
        System.out.println("Name:           " + site.name());
        System.out.println("Address:        " + site.address());
        System.out.println("Transport zone: " + site.transportZone());
        System.out.println("Phone number:   " + site.phoneNumber());
        System.out.println("Contact name:   " + site.contactName());
        System.out.println("Site type:      " + site.siteType());
    }

    public static class SitesComparator implements Comparator<Site> {
        @Override
        public int compare(Site o1, Site o2) {
            if(o1.siteType().equals(o2.siteType())){
                return o1.name().compareTo(o2.name());
            } else {
                return o1.siteType().compareTo(o2.siteType());
            }
        }
    }

}
