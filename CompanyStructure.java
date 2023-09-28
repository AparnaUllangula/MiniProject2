package Minipackage;
import java.util.ArrayList;
abstract class Employee {  //create Employee class
	    private static int countID;
	    public String name;
	    public int employeeID;
	    public double baseSalary;
	    public double bonus;
	    public Employee manager;
	    public Accountant accountantSupport;  // use has a relationship is there
	    public int headcount=0;
	    public double bonusBudget;
	    public Employee(String name, double baseSalary){
	        this.name=name;
	        this.baseSalary=baseSalary;
	        countID++;
	        this.employeeID=countID;
	    }

	    public double getBaseSalary(){
	        return this.baseSalary;// return base salary
	    }

	    public String getName(){
	        return this.name;  // return the employee's current name
	    }

	    public int getEmployeeID(){
	        //return the employee's ID.
	        return this.employeeID;
	    }

	    public Employee getManager(){
	        return manager; // return manager
	    }

	    public Accountant getAccountantSupport() {
	        return accountantSupport;
	    }

	    public void setManager(Employee manager){
	        this.manager=manager;
	    }

	    public boolean equals(Employee other){
	        // return true if the two employee IDs are the same, false otherwise
	        return this.getEmployeeID()==other.getEmployeeID();
	    }

	    public String toString(){
	        // return a String representation of the employee  of his id and name
	        return getEmployeeID()+" "+getName();
	    }

	    public abstract String employeeStatus();

	    public void getBonus(){

	    }

	}

class BusinessEmployee extends Employee {
   public BusinessEmployee(String name){
       //return  default salary of 50000
       super(name,50000.00);
   }
   public double getBonusBudget(){
       // H budget is determined will depend on which type of Business Employee it is
       return bonusBudget;
       }
   public void setBonusBudget(double bonusBudget) {
       this.bonusBudget = bonusBudget;
   }
   public String employeeStatus(){
       //return a String representation of this BusinessEmployee that includes id name with budget
       String s= String.format("%.2f",bonusBudget);//reduce the double to 2 decimals
       return this.toString()+" with a budget of "+ s;
   }
}
class TechnicalEmployee extends Employee {
   public int checkins;


   public TechnicalEmployee(String name){
       //return default base salary of 75000
       super(name,75000.00);
       checkins=0;
   }

   public String employeeStatus(){
       // return a String representation of this TechnicalEmployee that includes their ID, name and how many successful check ins they have had.
       return super.toString()+" has "+checkins+" successful check ins";
   }

   public void setCheckin(){
       checkins++;
   }
}
class Accountant extends BusinessEmployee {
    TechnicalLead teamSupported;

   public Accountant(String name){
       //Should start with a bonus budget of 0 and no team they are officially supporting
       super(name);
       bonusBudget=0;
   }

   public TechnicalLead getTeamSupported(){
       // return a reference to the TechnicalLead that this Accountant is currently supporting.
       return teamSupported;
   }

   public void supportTeam(TechnicalLead lead){
       //  Accountant's bonus budget should be updated to be the total of each SoftwareEngineer's base salary that reports to that TechnicalLead plus 10%.
       // For example, if the TechnicalLead supports 2 SoftwareEngineers, each with a salary of 75000, the Accountant's budget should be 150000 + 15000 for a total of 165000
       this.teamSupported=lead;
       for (int i=0; i<lead.team.size(); i++){
           this.bonusBudget+=lead.team.get(i).getBaseSalary()*1.1;
       }
   }

   public boolean canApproveBonus(double bonus) {
       // If the bonus is greater than the remaining budget, false should be returned, otherwise true.
       // If the accountant is not supporting any team false should be returned.
       double requestedBonus=bonus;
       if (requestedBonus<=getBonusBudget()){
           return true;
       } else {
           System.out.print(" Rejected because Budget not sufficient. ");
           return false;
       }

   }

   public String employeeStatus(){
       // return a String representation of this Accountant that includes their ID, name
       return this.toString()+" with a budget of "+ getBonusBudget()+" is supporting "+ this.getTeamSupported();
   }
}
class BusinessLead  extends BusinessEmployee{
	    public ArrayList<Accountant> team;

	    public BusinessLead(String name){
	        // create a new BusinessLead that is a Manager.
	        // The BusinessLead's base salary should be twice that of an Accountant.
	        // They should start with a head count of 10.
	        super(name);
	        this.baseSalary=getBaseSalary()*2;
	        this.headcount=10;
	        this.team=new ArrayList<Accountant>();
	    }
	    public boolean hasHeadCount(){
	        // return true if the number of direct reports this manager has is less than their headcount.
	        if(this.team.size()<this.headcount){
	            return true;
	        } else {
	            return false;
	        }
	    }
	    public boolean addReport(Accountant e, TechnicalLead supportTeam){
	        // accept the reference to an Accountant object, and if the BusinessLead has head count left should add this employee to their list of direct reports.
	        // If the employee is successfully added to the BusinessLead's direct reports true should be returned, false should be returned otherwise.
	        // Each time a report is added the BusinessLead's bonus budget should be increased by 1.1 times that new employee's base salary.
	        // That employee's team they are supporting should be updated to reflect the reference to the TechnicalLead given.
	        // If the employee is successfully added true should be returned, false otherwise
	        if (hasHeadCount()){

	            team.add(e);
	            e.setManager(this);
	            this.bonusBudget+=e.baseSalary*1.1;
	            e.supportTeam(supportTeam);
	            supportTeam.accountantSupport=e;
	            return true;
	        } else {
	            return false;
	        }
	    }

	    public boolean requestBonus(Employee e, double bonus){
	        //check if the bonus amount requested would fit in current BusinessLead's budget.
	        // If it is, that employee should get that bonus, the BusinessLeader's budget should be deducted and true should be returned. False should be returned otherwise
	       if (bonus<=getBonusBudget()){
	           this.bonusBudget-=bonus;
	           e.bonusBudget+=bonus;
	           return true;
	       } else {
	           return false;
	       }
	    }


	    public boolean approveBonus(Employee e, double bonus){
	        //function should look through the Accountants the BusinessLead manages,
	        // and if any of them are supporting a the TechnicalLead that is the manager of the Employee passed in then the Accountant's budget should be consulted to see if the bonus could be afforded.
	        // If the team can afford the bonus it should be rewarded and true returned, false otherwise

	        for (int i=0;i<team.size();i++){
	            if((team.get(i).getTeamSupported()).equals(e.manager) && team.get(i).canApproveBonus(bonus)) {
	                e.bonus += bonus;
	                team.get(i).bonusBudget -= bonus;
	                return true;
	            }
	        }
	        return  false;
	    }

	    public String getTeamStatus(){

	        if (team.size()==0){
	            return this.employeeStatus()+ " and no direct reports yet";
	        } else {
	            String teamStatus="";
	            for (int i=0;i<team.size();i++){
	                teamStatus+=("    "+team.get(i).employeeStatus()+"\n");
	            }
	            return this.employeeStatus()+" and is managing: \n"+teamStatus;

	        }
	    }
	}
class SoftwareEngineer extends TechnicalEmployee{
	    public boolean CodeAccess;

	    public SoftwareEngineer(String name){
	        super(name);
	        setCodeAccess(true);
	    }

	    public boolean getCodeAccess(){
	        return CodeAccess;// return whether or not this SoftwareEngineer has access to make changes to the code base
	    }

	    public void setCodeAccess(boolean access){
	        //allow an external piece of code to update
	        // the SoftwareEngineer's code privileges to either true or false
	        this.CodeAccess=access;
	    }

	    public int getSuccessfulCheckIns(){
	        //return the current count of how many times this SoftwareEngineer
	        // has successfully checked in code
	        return checkins;
	    }

	    public boolean checkInCode(){
	        //Should check if this SoftwareEngineer's manager approves of their check in.
	        // If the check in is approved their successful checkin count should be increased and the method should return "true".
	        // If the manager does not approve the check in the SoftwareEngineer's code access should be changed to false and
	        // the method should return "false"
	        TechnicalLead manager=(TechnicalLead) this.getManager();
	        if (manager.approveCheckIn(this)){
	            this.checkins++;
	            return true;
	        } else {
	            CodeAccess=false;
	            return false;
	        }
	    }
}
class TechnicalLead extends TechnicalEmployee {
	    public ArrayList<SoftwareEngineer> team;


	    public TechnicalLead(String name){
	        //create a new TechnicalLead that is a Manager.
	        // The TechnicalLead's base salary should be 1.3 times that of a TechnicalEmployee.
	        // TechnicalLeads should have a default head count of 4.
	        super(name);
	        this.baseSalary*=1.3;
	        headcount=4;
	        this.team=new ArrayList<SoftwareEngineer>();
	    }

	    public boolean hasHeadCount(){
	        // return true if the number of direct reports this manager has is less than their headcount.
	        if(team.size()<headcount){
	            return true;
	        } else {
	            return false;
	        }
	    }

	    public boolean addReport(SoftwareEngineer e){
	        // accept the reference to a SoftwareEngineer object,

	        if(hasHeadCount()){
	            team.add(e); // and if the TechnicalLead has head count left should add this employee to their list of direct reports.
	            e.setManager(this);//set the Technical Lead as manager of the Software Engineer.
	            return true;// If the employee is successfully added to the TechnicalLead's direct reports true should be returned,
	        } else {
	            return false; // false should be returned otherwise
	        }
	    }

	    public boolean approveCheckIn(SoftwareEngineer e){

	        if(e.getManager().equals(this) && e.getCodeAccess()){//Should see if the employee passed in does report to this manager and if their code access is currently set to "true".
	            return true;// If both those things are true, true is returned, otherwise false is returned
	        } else {
	            return false;
	        }
	    }


	    public boolean requestBonus(Employee e, double bonus){

	        //Should check if the bonus amount requested would be approved by the BusinessLead supporting this TechnicalLead.
	        // If it is, that employee should get that bonus and true should be returned. False should be returned otherwise
	        BusinessLead businessLead= (BusinessLead)getAccountantSupport().getManager();
	        if (businessLead.approveBonus(e, bonus)){
	            return true;
	        } else {
	            return false;
	        }
	    }


	    public String getTeamStatus(){
	        //Should return a String that gives insight into this Manager and all their direct reports.
	        // It should return a string that is a combination of the TechnicalLead's employee status followed by each of their direct employee's status on subsequent lines.
	        // If the TechnicalLead has no reports it should print their employee status followed by the text " and no direct reports yet ".
	        // Example: "10 Kasey has 5 successful check ins and no direct reports yet".
	        // If the TechnicalLead does have reports it might look something like "10 Kasey has 5 successful check ins and is managing: /n 5 Niky has 2 successful check ins"

	        if (team.size()==0){
	            return this.employeeStatus()+ " and no direct reports yet";
	        } else {
	            String teamStatus="";
	            for (int i=0;i<team.size();i++){
	                teamStatus+=("    "+team.get(i).employeeStatus()+"\n");
	            }
	            return this.employeeStatus()+" and is managing: \n"+teamStatus;
	        }
	    }
	}
public class CompanyStructure {
	    public static void main(String[] args) {
	        TechnicalLead CTO = new TechnicalLead("Satya Nadella");
	        SoftwareEngineer seA = new SoftwareEngineer("Kasey");
	        SoftwareEngineer seB = new SoftwareEngineer("Breana");
	        SoftwareEngineer seC = new SoftwareEngineer("Eric");
	        CTO.addReport(seA);
	        seA.checkInCode();
	        seA.checkInCode();
	        CTO.addReport(seB);
	        CTO.addReport(seC);
	        seC.checkInCode();
	        seC.checkInCode();
	        System.out.println(CTO.getTeamStatus());
	        TechnicalLead VPofENG = new TechnicalLead("Bill Gates");
	        SoftwareEngineer seD = new SoftwareEngineer("Winter");
	        SoftwareEngineer seE = new SoftwareEngineer("Libby");
	        SoftwareEngineer seF = new SoftwareEngineer("Gizan");
	        SoftwareEngineer seG = new SoftwareEngineer("Zaynah");
	        VPofENG.addReport(seD);
	        VPofENG.addReport(seE);
	        VPofENG.addReport(seF);
	        VPofENG.addReport(seG);
	        seD.checkInCode();
	        seF.checkInCode();
	        seF.checkInCode();
	        seF.checkInCode();
	        seF.checkInCode();
	        System.out.println(VPofENG.getTeamStatus());
	        BusinessLead CFO = new BusinessLead("Amy Hood");
	        Accountant actA = new Accountant("Niky");
	        Accountant actB = new Accountant("Andrew");
	        CFO.addReport(actA, CTO);
	        CFO.addReport(actB, VPofENG);
	        System.out.println(CFO.getTeamStatus());
	        System.out.println(seB.toString() + "'s manager is " + seB.getManager().toString());
	        System.out.println(seF.toString() + "'s manager is " + seF.getManager().toString());
	        System.out.println(actB.toString() + "'s manager is " + actB.getManager().toString());
	        System.out.println();
	        System.out.println("Testing BusinessLead approvedBonus()");
	        System.out.print(seA.getManager() + " is asking for $10,000 bonus for "+seA.getName()+", (the Approval result should be TRUE): ");
	        System.out.println(CTO.requestBonus(seA, 10000));
	        System.out.println("Updated budget is: "+seA.getManager().getAccountantSupport().getBonusBudget()+"\n");
	        System.out.print(seF.getManager() + " is asking for $5,000 bonus for "+seF.getName() +", (the Approval result should be TRUE): ");
	        System.out.println(VPofENG.requestBonus(seF, 5000));
	        System.out.println("Updated budget is: "+seF.getManager().getAccountantSupport().getBonusBudget()+"\n");
	        System.out.print(seF.getManager() + " is asking for $400,000 bonus  for "+seF.getName()+", (the Approval result should be FALSE): ");
	        System.out.println(VPofENG.requestBonus(seF, 400000));
	        System.out.println("Updated budget is: "+seF.getManager().getAccountantSupport().getBonusBudget()+"\n");
	        System.out.println();
	    }

	}

