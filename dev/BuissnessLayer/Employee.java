package dev.BuissnessLayer;

import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

class Employee{
   private String name;
   private int id;
   private BankDetails bankAccount;
   private int salary;
   private List<EmploymentConditions> empConditions;
   private Date employmentDate;
   private Constraints workingConstraints;
   private boolean isFired;

  

   public Employee(String name, int id, BankDetails bd, int salary, List<EmploymentConditions> ec, Date employmentDate){
      this.name = name;
      this.id = id;
      this.bankAccount = bd;
      this.salary = salary;
      this.empConditions = ec;
      this.employmentDate = employmentDate;
      this.workingConstraints = new Constraints(this);
      this.isFired = false;
   }

   public int getId() {
       return this.id;
   }

   public boolean getIsFired(){
      return isFired;
   }

   public void addUnavailableDate(Date d){
      this.workingConstraints.addUnavailableDate(d);
  }

   public void setAvailableDate(Date d){
      this.workingConstraints.setAvailableDate(d);
   }
   public int getSalary(){
      return this.salary;
   }
   public void setSalary(int x){
      this.salary = x;
   }
   public String toString(){
      return "UNFINISHED";
   }

   public void calculateSalaryAutomatically() throws Exception{
      throw new Exception("unimplented");
   }
}