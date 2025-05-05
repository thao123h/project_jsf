package project.bean;

import javax.inject.Named;

import project.dao.EmployeeDao;
import project.model.Employee;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.validator.ValidatorException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import java.util.List;



@Named
@SessionScoped
public class EmployeeBean implements Serializable {

	private boolean showForm = false;
	private static final long serialVersionUID = 1L;
	private List<Employee> employees;
	private Employee selectedEmployee = new Employee();
	private EmployeeDao dao = new EmployeeDao();
	private String message;
	public void toggleForm() {
		this.showForm = !this.showForm;

	}


	public List<Employee> getEmployees() {
	    if (employees == null) {
	        employees = dao.getEmployees();
	    }
	    return employees;
	}

	public void delete() {
	    if (dao.delete(selectedEmployee.getEmployeeCode())) {
	        selectedEmployee = new Employee();
	        employees = null; // reset lại danh sách
	    }
	}

	public void save() {
	    if (selectedEmployee.getDateOfBirth() != null) {
	        LocalDate today = LocalDate.now();
	        LocalDate birthDate = selectedEmployee.getDateOfBirth();
	        int age = Period.between(birthDate, today).getYears();
	        selectedEmployee.setEmployeeAge(age);
	    }

	    if (selectedEmployee.getEmployeeCode() != null && !selectedEmployee.getEmployeeCode().isEmpty()) {
	        dao.update(selectedEmployee);
	    } else {
	        String nextCode = generateNextEmployeeCode();
	        selectedEmployee.setEmployeeCode(nextCode);
	        dao.add(selectedEmployee);
	    }

	    selectedEmployee = new Employee();
	    employees = null; // refresh list
	    toggleForm();
	}

	
	// Validate
	public void validateName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		 if (component instanceof EditableValueHolder) {
		        ((EditableValueHolder) component).setSubmittedValue(null);
		    }	
		if (value == null) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name cannot be empty!", null));
		}
		String valueString = value.toString();
		if (valueString.length() < 2 || valueString.length() > 50) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"The name must be between 2 and 50 characters!", null));
		}
		if (!valueString.matches("^[\\p{L}\\s'.-]+$")) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"The name cannot contain number or special characters other than apostrophes, periods, and hyphens.",
					null));
		}

	}

	public void validateDate(FacesContext context, UIComponent component, LocalDate value) throws ValidatorException {
		 if (component instanceof EditableValueHolder) {
		        ((EditableValueHolder) component).setSubmittedValue(null);
		    }	
	    if (value == null) {
	        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date cannot be empty!", null));
	    }

	    LocalDate today = LocalDate.now();
	    
	    if (!value.isBefore(today)) {
	        throw new ValidatorException(
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date must be before today.", null));
	    }

	    int age = Period.between(value, today).getYears();
	 
	    if (age < 18) {
	        throw new ValidatorException(
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Age must be at least 18.", null));
	    }
	    if (age > 100) {
	        throw new ValidatorException(
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Age must be less than 100.", null));
	    }
	}


	public Employee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}
	private String generateNextEmployeeCode() {
	    List<Employee> allEmployees = dao.getEmployees(); // lấy từ DB
	    int max = 0;

	    for (Employee emp : allEmployees) {
	        String code = emp.getEmployeeCode();
	        if (code != null && code.matches("E\\d{3}")) {
	            int num = Integer.parseInt(code.substring(1));
	            if (num > max) max = num;
	        }
	    }

	    return String.format("E%03d", max + 1);
	}

}
