# 📊 Employee Management System  

A **JavaFX-based** application for managing employee records using **Java Collections, Generics, Streams, and Comparators**. Supports **CRUD operations, searching, filtering, and sorting** with a clean UI.  

---

## ✨ Features  

### **Core Functionality**  
✅ **Generic `Employee<T>` Class** – Type-safe employee records with unique IDs (UUID/Integer)  
✅ **CRUD Operations** – Add, remove, update, and retrieve employees via `HashMap<T, Employee<T>>`  
✅ **Search & Filtering** – Find employees by:  
   - Department  
   - Name (partial match)  
   - Performance rating (≥ threshold)  
   - Salary range  
✅ **Sorting** – Using `Comparable` (experience) & `Comparator` (salary, performance)  
✅ **Salary Analytics** – Raise calculations, top earners, department-wise averages  

### **Java Collections & Streams**  
📌 **HashMap** – Efficient employee storage & retrieval  
📌 **Stream API** – Filtering, sorting, and statistical operations  
📌 **Iterator** – Traversal for display/processing  

### **JavaFX UI**  
🖥️ **Interactive Dashboard** – View, add, and delete employees  
🔍 **Search & Sort** – Real-time filtering with dropdowns  
📋 **Formatted Reports** – Clean tabular display  

---

## 🧑‍💻 **Employee Class Structure**  
```java
public class Employee<T> implements Comparable<Employee<T>> {
    private T employeeId;          // UUID or Integer
    private String name;
    private String department;
    private double salary;
    private double performanceRating;  // 0-5 scale
    private int yearsOfExperience;
    private boolean isActive;
    
    // Constructor, Getters/Setters
    @Override 
    public int compareTo(Employee<T> other) { /* Sort by experience */ }
}
```

## 🛠️ Technical Implementation (Mermaid Diagram)

```mermaid

classDiagram
    direction BT

    %% Model Layer
    class Employee~T~ {
        <<Generic Class>>
        -T employeeId
        -String name
        -String department
        -double salary
        -double performanceRating
        -int yearsOfExperience
        -boolean isActive
        +Employee(T, String, String, double, double, int, boolean)
        +getEmployeeId() T
        +getName() String
        +setName(String) void
        +getDepartment() String
        +setDepartment(String) void
        +getSalary() double
        +setSalary(double) void
        +getPerformanceRating() double
        +setPerformanceRating(double) void
        +getYearsOfExperience() int
        +setYearsOfExperience(int) void
        +isActive() boolean
        +setActive(boolean) void
        +compareTo(Employee~T~) int
        +equals(Object) boolean
        +hashCode() int
        +toString() String
    }

    %% Service Layer
    class EmployeeManagementSystem~T~ {
        <<Generic Class>>
        -Map~T~, Employee~T~ employeeDatabase
        +addEmployee(Employee~T~) boolean
        +getEmployee(UUID) Employee~T~
        +removeEmployee(T) boolean
        +updateEmployeeDetails(T, String, Object) boolean
        +getAllEmployees() List~Employee~T~~
        +getEmployeesByDepartment(String) List~Employee~T~~
        +searchEmployeesByName(String) List~Employee~T~~
        +getHighPerformingEmployees(double) List~Employee~T~~
        +getEmployeesInSalaryRange(double, double) List~Employee~T~~
        +sortEmployeesByExperience() List~Employee~T~~
        +sortEmployeesBySalary() List~Employee~T~~
        +sortEmployeesByPerformance() List~Employee~T~~
        +givePerformanceRaise(double, double) void
        +getTopPaidEmployees(int) List~Employee~T~~
        +getAverageSalaryByDepartment(String) double
    }

    class EmployeeNotExistException {
        -UUID employeeId
        +EmployeeNotExistException(UUID)
    }

    %% UI Layer - Controllers
    class EmployeeManagementController {
        -EmployeeManagementSystem~UUID~ ems
        -ObservableList~Employee~UUID~~ employeeData
        -TableView~Employee~UUID~~ employeeTable
        -ComboBox~String~ filterComboBox
        -ComboBox~String~ sortComboBox
        -TextField searchField
        +initialize() void
        -loadSampleData() void
        +handleSearch() void
        +handleClearSearch() void
        +handleAddEmployee() void
        +handleEditEmployee() void
        +handleDeleteEmployee() void
        +handleApplyFilters() void
        -applyFilters(String) List~Employee~UUID~~
        -applySorting(String, List~Employee~UUID~~) List~Employee~UUID~~
        -showAlert(String, String) void
    }

    class WelcomeController {
        -ProgressIndicator progressIndicator
        -Label statusLabel
        +initialize() void
        -loadMainApplication() void
    }

    class EmployeeManagementApp {
        +start(Stage) void
        -showErrorAlert() void
        +main(String[]) void
    }

    %% UI Utilities
    class DialogUtils {
        <<Utility>>
        +createErrorLabel() Label
        +configureGridLayout(GridPane) void
        +setupValidation(TextField, Label, TextField, Label, TextField, Label, TextField, Label, TextField, Label, Node) void
        -isValidDouble(String) boolean
        -isValidInteger(String) boolean
    }

    %% JavaFX Components (simplified)
    class TableViewEmployee~UUID~ {
        +setItems(ObservableList~Employee~UUID~~) void
        +getSelectionModel() TableView.TableViewSelectionModel~Employee~UUID~~
        +refresh() void
    }

    class TableColumnEmployeeUUID~, UUID~ {
        +setCellValueFactory(Callback) void
        +setCellFactory(Callback) void
    }

    %% Relationships
    EmployeeManagementSystem "1" *-- "0..*" Employee : contains
    EmployeeManagementSystem ..> EmployeeNotExistException : throws
    EmployeeManagementController --> EmployeeManagementSystem : uses
    EmployeeManagementController --> TableView : manages
    EmployeeManagementController --> DialogUtils : uses
    WelcomeController --> EmployeeManagementApp : transitions to
    WelcomeController --> EmployeeManagementController : launches
    EmployeeManagementApp --> WelcomeController : launches
    EmployeeManagementApp ..> FXMLLoader : uses

    %% JavaFX implicit relationships
    EmployeeManagementController ..> TableColumn : configures

    WelcomeController ..> ProgressIndicator : updates

    TableView --> TableViewEmployee : maintains
    TableViewEmployee --> TableColumnEmployeeUUID : has


    %% Comparators (nested classes)
    class EmployeeSalaryComparator~T~ {
        <<Comparator>>
        +compare(Employee~T~, Employee~T~) int
    }

    class EmployeePerformanceComparator~T~ {
        <<Comparator>>
        +compare(Employee~T~, Employee~T~) int
    }

    EmployeeManagementSystem ..> EmployeeSalaryComparator : uses
    EmployeeManagementSystem ..> EmployeePerformanceComparator : uses
```

### **3. JavaFX UI Preview**  
![UI Mockup](https://via.placeholder.com/600x400?text=JavaFX+Employee+Management+UI)  
*(To be replaced with actual screenshot post-implementation)*  

---

## 🚀 **Getting Started**  

### **Prerequisites**  
- Java JDK 17+  
- JavaFX SDK  
- Maven  

### **Installation**  
```bash
git clone git@github.com:thenoblet/ems.git
cd ems
mvn clean install
```

### **Run the Application**  
```bash
mvn javafx:run
```

---

## 📌 **Lab Objectives Achieved**  
✔ **Collections Framework** – `HashMap`, `List`, `Iterator`  
✔ **Generics** – Type-safe `Employee<T>` class  
✔ **Stream API** – Filtering (`filter`, `sorted`), analytics (`average`, `max`)  
✔ **Comparable/Comparator** – Custom sorting (experience, salary, performance)  
✔ **JavaFX UI** – Interactive CRUD operations  

---

## 📜 **License**  
MIT  

---

### **Suggested Improvements**  
1. Add **export to CSV/JSON** functionality.  
2. Implement **user authentication** (admin/employee roles).  
3. Enhance UI with **charts** for salary/performance analytics.  
