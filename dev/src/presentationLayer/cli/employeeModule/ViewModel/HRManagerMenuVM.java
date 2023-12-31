package presentationLayer.cli.employeeModule.ViewModel;

import presentationLayer.cli.employeeModule.Model.BackendController;
import serviceLayer.employeeModule.Objects.SShift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HRManagerMenuVM {
    private final BackendController backendController;

    public HRManagerMenuVM(BackendController backendController) {

        this.backendController = backendController;
    }

    public String recruitEmployee(String branchId, String fullName, String id, String bankDetails, double hourlyRate, LocalDate employmentDate, String employmentConditions, String details) {
        return backendController.recruitEmployee(branchId, fullName, id, bankDetails, hourlyRate, employmentDate, employmentConditions, details);
    }

    public String createUser(String username, String password) {
        return backendController.createUser(username, password);
    }

    public String logout() {
        return backendController.logout();
    }

    public String getNextWeekShiftRequests(String branchId) {
        try {
            List<SShift[]> shifts = backendController.getNextWeekShifts(branchId);
            // Return next week's shift requests for the HR Manager
            StringBuilder result = new StringBuilder();
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts) {
                    if (shift != null) {
                        result.append(shift.requestsString()).append("\n");
                        found = true;
                    }
                }
            }
            if (!found) {
                result = new StringBuilder("There are no requests for shifts planned for next week in branch " + branchId + ".");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getWeekShiftRequests(String branchId, LocalDate weekStart) {
        try {
            List<SShift[]> shifts = backendController.getWeekShifts(branchId, weekStart);
            // Return the week's shift requests for the HR Manager
            StringBuilder result = new StringBuilder();
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts) {
                    if (shift != null) {
                        result.append(shift.requestsString()).append("\n");
                        found = true;
                    }
                }
            }
            if (!found) {
                result = new StringBuilder("There are no requests for shifts planned in the specified week in branch " + branchId + ".");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getNextWeekShifts(String branchId) {
        try {
            List<SShift[]> shifts = backendController.getNextWeekShifts(branchId);
            // Return week schedule for HR Manager
            StringBuilder result = new StringBuilder();
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts) {
                    if (shift != null) {
                        result.append(shift).append("\n");
                        found = true;
                    }
                }
            }
            if (!found) {
                result = new StringBuilder("There are no shifts planned for next week in branch " + branchId + ".");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getWeekShifts(String branchId, LocalDate weekStart) {
        try {
            List<SShift[]> shifts = backendController.getWeekShifts(branchId, weekStart);
            // Return week schedule for HR Manager
            StringBuilder result = new StringBuilder();
            boolean found = false;
            for(SShift[] dayShifts : shifts) {
                for (SShift shift : dayShifts) {
                    if (shift != null) {
                        result.append(shift).append("\n");
                        found = true;
                    }
                }
            }
            if (!found) {
                result = new StringBuilder("There are no shifts planned in the specified week in branch " + branchId + ".");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String createWeekShifts(String branchId, LocalDate weekStart) {
        return backendController.createWeekShifts(branchId, weekStart);
    }

    public String createNextWeekShifts(String branchId) {
        return backendController.createNextWeekShifts(branchId);
    }

    public String setShiftNeededAmount(String branchId, LocalDate shiftDate, String shiftType, String role, int amount) {
        return backendController.setShiftNeededAmount(branchId, shiftDate, shiftType, role, amount);
    }

    public String setShiftEmployees(String branchId, LocalDate shiftDate, String shiftType, String role, List<String> employeeIds) {
        return backendController.setShiftEmployees(branchId, shiftDate, shiftType, role, employeeIds);
    }

    public String certifyEmployee(String employeeId, String role) {
        return backendController.certifyEmployee(employeeId, role);
    }

    public String certifyDriver(String employeeId, String driverLicense) {
        return backendController.certifyDriver(employeeId, driverLicense);
    }

    public String uncertifyEmployee(String employeeId, String role) {
        return backendController.uncertifyEmployee(employeeId, role);
    }

    public String approveShift(String branchId, LocalDate shiftDate, String shiftType) {
        return backendController.approveShift(branchId, shiftDate, shiftType);
    }

    public String addEmployeeToBranch(String employeeId, String branchId) {
        return backendController.addEmployeeToBranch(employeeId, branchId);
    }

    public String deleteShift(String branchId, LocalDate shiftDate, String shiftType) {
        return backendController.deleteShift(branchId, shiftDate, shiftType);
    }

    public String createBranch(String branchId) {
        return backendController.createBranch(branchId);
    }

    public String updateBranchWorkingHours(String branchId, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd) {
        return backendController.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
    }

    public String updateEmployeeSalary(String employeeId, double hourlySalaryRate, double salaryBonus) {
        return backendController.updateEmployeeSalary(employeeId, hourlySalaryRate, salaryBonus);
    }

    public String updateEmployeeBankDetails(String employeeId, String bankDetails) {
        return backendController.updateEmployeeBankDetails(employeeId, bankDetails);
    }

    public String updateEmployeeEmploymentConditions(String employeeId, String employmentConditions) {
        return backendController.updateEmployeeEmploymentConditions(employeeId, employmentConditions);
    }

    public String updateEmployeeDetails(String employeeId, String details) {
        return backendController.updateEmployeeDetails(employeeId, details);
    }

    public String authorizeUser(String username, String authorization) {
        return backendController.authorizeUser(username, authorization);
    }
}
