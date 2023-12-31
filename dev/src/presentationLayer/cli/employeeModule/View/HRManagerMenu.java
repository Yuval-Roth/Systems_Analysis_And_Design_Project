package presentationLayer.cli.employeeModule.View;

import presentationLayer.cli.employeeModule.Model.BackendController;
import presentationLayer.cli.employeeModule.ViewModel.HRManagerMenuVM;
import utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

public class HRManagerMenu implements Menu {
    private final HRManagerMenuVM hrManagerMenuVM;
    private final Scanner scanner;

    public HRManagerMenu(BackendController backendController) {
        hrManagerMenuVM = new HRManagerMenuVM(backendController);
        scanner = new Scanner(System.in);
        System.out.println("Welcome to the HR Manager Menu.");
    }

    public void printCommands() {
        System.out.println();
        System.out.println("1. `logout` - Logout command");
        System.out.println("2. `recruit_employee` - Recruit a new employee");
        System.out.println("3. `create_week_shifts` - Create week shifts");
        System.out.println("4. `update_shift_needed` - Update shift needed amount by role");
        System.out.println("5. `update_shift_employees` - Update the week's shifts needed amount by role");
        System.out.println("6. `week_shifts` - Show next week shifts");
        System.out.println("7. `certify` - Certify employee to the given role");
        System.out.println("8. `uncertify` - Removes the employee's certification of the given role");
        System.out.println("9. `approve_shift` - Approve the specified shift");
        System.out.println("10. `add_employee_to_branch` - Adds an existing employee to a branch");
        System.out.println("11. `delete_shift` - Deletes the specified shift");
        //System.out.println("12. `create_branch` - Creates a new branch with the given branch id"); // This operation has moved to Transport Module when creating a new site
        System.out.println("12. `update_branch_hours` - Updates the branch's working hours");
        System.out.println("13. `update_employee` - Updates an existing employee's details");
        System.out.println("14. `authorize` - Authorizes an existing user to the given authorization");
        System.out.println("15. `exit` - Exit command");
    }

    public Menu run() {
        printCommands();
        String input = scanner.nextLine();
        String[] command = input.split(" ", -1);
        String output;
        if (command.length == 0) {
            output = "Invalid command, command cannot be empty.";
        } else if (command[0].equals("exit") && command.length == 1) {
            output = "Exiting CLI.";
            MenuManager.terminate();
        }
        else if (command[0].equals("logout") && command.length == 1) {
            output = hrManagerMenuVM.logout();
            System.out.println(output);
            return new LoginMenu();
        }
        else if (command[0].equals("recruit_employee") && command.length == 1) {
            // Parameters: <first_name> <last_name> <branch_id> <employee_id> <bank_number> <bank branch> <hourly_rate> <employment_date: DD/MM/YYYY>
            try {
                System.out.println("Please enter the name of the employee:");
                String employeeName = scanner.nextLine();
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the employee id:");
                String employeeId = scanner.nextLine();
                System.out.println("Please enter the employee's bank number and bank branch number: <bank_number> <bank_branch>");
                String[] bankDetails = scanner.nextLine().split(" ",-1);
                int bankNumber = Integer.parseInt(bankDetails[0]);
                int branchNumber = Integer.parseInt(bankDetails[1]);
                System.out.println("Please enter the employee's hourly salary rate:");
                String hourlyRateStr = scanner.nextLine();
                double hourlyRate = Double.parseDouble(hourlyRateStr);
                System.out.println("Please enter the employee's employment date <DD/MM/YYYY>:");
                String employmentDateStr = scanner.nextLine();
                LocalDate employmentDate = DateUtils.parse(employmentDateStr);
                System.out.println("Please enter the employment conditions:");
                String employmentConditions = scanner.nextLine();
                System.out.println("Please enter other employee details (optional):");
                String details = scanner.nextLine();
                output = hrManagerMenuVM.recruitEmployee(employeeName, branchId, employeeId, bankNumber + " " + branchNumber, hourlyRate, employmentDate, employmentConditions, details);
                if (output == null) {
                    System.out.println("Recruited employee successfully.");
                    System.out.println("Would you like to create a user for the new employee?");
                    System.out.println("1. To create user, enter: <password>");
                    System.out.println("2. Enter any other input to skip it for now.");
                    input = scanner.nextLine();
                    command = input.split(" ", -1);
                    if (command.length == 1) {
                        String password = command[0];
                        output = hrManagerMenuVM.createUser(employeeId,password);
                    }
                }
            } catch (NumberFormatException e) {
                output = "Invalid input, expected an integer value. " + e.getMessage();
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("create_week_shifts") && command.length == 1) {
            // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty to create for next week):");
            String weekStartStr = scanner.nextLine();
            if(weekStartStr.isEmpty()) {
                output = hrManagerMenuVM.createNextWeekShifts(branchId);
            }
            else {
                try {
                    LocalDate weekStart = DateUtils.parse(weekStartStr);
                    output = hrManagerMenuVM.createWeekShifts(branchId, weekStart);
                } catch (DateTimeParseException e) {
                    output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                }
            }
        }
        else if (command[0].equals("update_shift_needed") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening> <role> <amount>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the shift date <DD/MM/YYYY>:");
                String shiftDateStr = scanner.nextLine();
                LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                System.out.println("Please enter the shift type (Morning/Evening):");
                String shiftType = scanner.nextLine();
                System.out.println("Please enter the shift role:");
                String role = scanner.nextLine();
                System.out.println("Please enter the needed amount of employees for this role:");
                String amountStr = scanner.nextLine();
                int amount = Integer.parseInt(amountStr);
                output = hrManagerMenuVM.setShiftNeededAmount(branchId, shiftDate, shiftType, role, amount);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            } catch (NumberFormatException e) {
                output = "Invalid input, expected an integer amount of employees.";
            }
        }
        else if (command[0].equals("update_shift_employees") && command.length == 1) {
            // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
            String weekStartStr = scanner.nextLine();
            if (!weekStartStr.isEmpty()) {
                LocalDate weekStart = DateUtils.parse(weekStartStr);
                // Prints the week's shift requests
                System.out.println("The week's shift requests:");
                System.out.println(hrManagerMenuVM.getWeekShiftRequests(branchId, weekStart));
            }
            else { // Prints the next week's shift requests
                System.out.println("The next week's shift requests:");
                System.out.println(hrManagerMenuVM.getNextWeekShiftRequests(branchId));
            }
            System.out.println("Please enter which shift date (DD/MM/YYYY) to update:");
            String dateInput = scanner.nextLine();
            while(!DateUtils.validDate(dateInput)) {
                System.out.println("Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ", try again.");
                dateInput = scanner.nextLine();
            }
            LocalDate shiftDate = DateUtils.parse(dateInput);
            System.out.println("Please enter which shift type (Morning/Evening):");
            String shiftType = scanner.nextLine();
            while (!shiftType.equals("Morning") && !shiftType.equals("Evening")) {
                System.out.println("Invalid input, expected a shift type of `Morning` or `Evening`.");
                shiftType = scanner.nextLine();
            }
            System.out.println("Please enter which role (Cashier/Storekeeper/ShiftManager/GeneralWorker/Driver):");
            String role = scanner.nextLine();

            System.out.println("Please enter the employee ids for this role: `<id1> <id2> <id3> ...`");
            String idsInput = scanner.nextLine();
            String[] employeeIds = idsInput.split(" ", -1);
            output = hrManagerMenuVM.setShiftEmployees(branchId, shiftDate, shiftType, role, Arrays.stream(employeeIds).toList());
        }
        else if (command[0].equals("week_shifts") && command.length == 1) {
            // Parameters: <branch_id>  |  <branch_id> <week_start: DD/MM/YYYY>
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
            String weekStartStr = scanner.nextLine();
            if (weekStartStr.isEmpty()) {
                output = hrManagerMenuVM.getNextWeekShifts(branchId);
            }
            else {
                try {
                    LocalDate weekStart = DateUtils.parse(weekStartStr);
                    output = hrManagerMenuVM.getWeekShifts(branchId, weekStart);
                } catch (DateTimeParseException e) {
                    output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
                }
            }
        }
        else if (command[0].equals("certify") && command.length == 1) {
            // Parameters: <employee_id> <role>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
            System.out.println("Please enter the role to certify:");
            String role = scanner.nextLine();
            if (role.equals("Driver")) {
                System.out.println("Please enter the driver's license: (A1,A2,B1,B2,B3,C1,C2,C3)");
                String driverLicense = scanner.nextLine();
                output = hrManagerMenuVM.certifyDriver(employeeId, driverLicense);
            }
            else {
                output = hrManagerMenuVM.certifyEmployee(employeeId, role);
            }
        }
        else if (command[0].equals("uncertify") && command.length == 1) {
            // Parameters: <employee_id> <role>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
            System.out.println("Please enter the role to uncertify:");
            String role = scanner.nextLine();
            output = hrManagerMenuVM.uncertifyEmployee(employeeId, role);
        }
        else if (command[0].equals("approve_shift") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                String shiftDateStr = scanner.nextLine();
                LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                System.out.println("Please enter the shift type (Morning/Evening):");
                String shiftType = scanner.nextLine();
                output = hrManagerMenuVM.approveShift(branchId, shiftDate, shiftType);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("add_employee_to_branch") && command.length == 1) {
            // Parameters: <employee_id> <branch_id>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
            System.out.println("Please enter the branch id (branch address):");
            String branchId = scanner.nextLine();
            output = hrManagerMenuVM.addEmployeeToBranch(employeeId, branchId);
        }
        else if (command[0].equals("delete_shift") && command.length == 1) {
            // Parameters: <branch_id> <shift_date: DD/MM/YYYY> <shift_type: Morning/Evening>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the starting date <DD/MM/YYYY> of the shifts week (or leave empty for next week's shifts):");
                String shiftDateStr = scanner.nextLine();
                LocalDate shiftDate = DateUtils.parse(shiftDateStr);
                String shiftType = scanner.nextLine();
                output = hrManagerMenuVM.deleteShift(branchId, shiftDate, shiftType);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a date in the form " + DateUtils.DATE_PATTERN + ".";
            }
        }
        else if (command[0].equals("update_branch_hours") && command.length == 1) {
            // Parameters: <branch_id> <morning_start> <morning_end> <evening_start> <evening_end>
            try {
                System.out.println("Please enter the branch id (branch address):");
                String branchId = scanner.nextLine();
                System.out.println("Please enter the morning start time (HH:MM):");
                LocalTime morningStart = LocalTime.parse(scanner.nextLine());
                System.out.println("Please enter the morning end time (HH:MM):");
                LocalTime morningEnd = LocalTime.parse(scanner.nextLine());
                System.out.println("Please enter the evening start time (HH:MM):");
                LocalTime eveningStart = LocalTime.parse(scanner.nextLine());
                System.out.println("Please enter the evening end time (HH:MM):");
                LocalTime eveningEnd = LocalTime.parse(scanner.nextLine());
                output = hrManagerMenuVM.updateBranchWorkingHours(branchId, morningStart, morningEnd, eveningStart, eveningEnd);
            } catch (DateTimeParseException e) {
                output = "Invalid input, expected a time in the form HH:MM.";
            }
        }
        else if (command[0].equals("update_employee") && command.length == 1) {
            // Parameters: <employee_id>
            System.out.println("Please enter the employee id:");
            String employeeId = scanner.nextLine();
            System.out.println("Please choose which detail to update:");
            System.out.println("1. Salary");
            System.out.println("2. Bank Details");
            System.out.println("3. Employment Conditions");
            System.out.println("4. Optional Details");
            String detailsInput = scanner.nextLine();
            switch (detailsInput) {
                case "1" -> { // Salary
                    String hourlyRateString = "", salaryBonusString = "";
                    try {
                        System.out.println("Please enter the updated employee's hourly rate:");
                        hourlyRateString = scanner.nextLine();
                        System.out.println("Please enter the updated employee's salary bonus:");
                        salaryBonusString = scanner.nextLine();
                        double hourlySalaryRate = Double.parseDouble(hourlyRateString);
                        double salaryBonus = Double.parseDouble(salaryBonusString);
                        output = hrManagerMenuVM.updateEmployeeSalary(employeeId, hourlySalaryRate, salaryBonus);
                    } catch (NumberFormatException e) {
                        output = "Invalid input, expected only decimal numbers, but received: " + hourlyRateString + " " + salaryBonusString + " try again.";
                    }
                }
                case "2" -> { // Bank Details
                    System.out.println("Please enter the updated employee's bank details:");
                    String bankDetails = scanner.nextLine();
                    output = hrManagerMenuVM.updateEmployeeBankDetails(employeeId, bankDetails);
                }
                case "3" -> { // Employment Conditions
                    System.out.println("Please enter the updated employee's employment conditions:");
                    String employmentConditions = scanner.nextLine();
                    output = hrManagerMenuVM.updateEmployeeEmploymentConditions(employeeId, employmentConditions);
                }
                case "4" -> { // Optional Details
                    System.out.println("Please enter the updated employee's optional details:");
                    String details = scanner.nextLine();
                    output = hrManagerMenuVM.updateEmployeeDetails(employeeId, details);
                }
                default -> output = "Invalid input, expected a number between 1 and 4, try again.";
            }
        }
        else if (command[0].equals("authorize") && command.length == 1) {
            // Parameters: <username> <authorization>
            System.out.println("Please enter the username to authorize:");
            String username = scanner.nextLine();
            System.out.println("Please enter the authorization to authorize:");
            String authorization = scanner.nextLine();
            output = hrManagerMenuVM.authorizeUser(username, authorization);
        }
        else {
            output = "Invalid command was given, try again.";
        }
        System.out.println(output);
        return this;
    }
}