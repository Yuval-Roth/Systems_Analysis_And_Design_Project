package serviceLayer;


import businessLayer.BusinessFactory;
import dataAccessLayer.dalUtils.DalException;
import serviceLayer.employeeModule.Services.EmployeesService;
import serviceLayer.employeeModule.Services.UserService;
import serviceLayer.transportModule.ItemListsService;
import serviceLayer.transportModule.ResourceManagementService;
import serviceLayer.transportModule.TransportsService;

public class ServiceFactory {

    private TransportsService transportsService;
    private ResourceManagementService resourceManagementService;
    private ItemListsService itemListsService;
    private EmployeesService employeesService;
    private UserService userService;
    private final BusinessFactory businessFactory;

    public ServiceFactory(){
        try {
            businessFactory = new BusinessFactory();
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        buildInstances();
    }

    public ServiceFactory(String dbName){
        try {
            businessFactory = new BusinessFactory(dbName);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        buildInstances();
    }

    private void buildInstances() {
        userService = new UserService(this);
        itemListsService = new ItemListsService(businessFactory.itemListsController());
        transportsService = new TransportsService(businessFactory.transportsController());


        //======================== Dependents ===================== |
        /*(1)*/ resourceManagementService = new ResourceManagementService(businessFactory.sitesController(),
                    businessFactory.driversController(),
                    businessFactory.trucksController());
        /*(2)*/ employeesService = new EmployeesService(this, businessFactory.employeesController(), businessFactory.shiftsController());
        /*(3)*/ businessFactory.injectDependencies(employeesService);
        //========================================================= |

    }

    public TransportsService getTransportsService() {
        return transportsService;
    }

    public ResourceManagementService getResourceManagementService() {
        return resourceManagementService;
    }

    public ItemListsService getItemListsService() {
        return itemListsService;
    }

    public EmployeesService employeesService() {
        return employeesService;
    }

    public UserService userService() {
        return userService;
    }
}
