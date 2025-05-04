package project.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import project.model.Employee;

public class EmployeeDao {
	private static final String URL = "jdbc:postgresql://localhost:5432/jsf_demo";
	private static final String USER = "postgres";
	private static final String PASSWORD = "Taskhub1";

	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<>();
		String query = "SELECT * FROM employee";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			
			while (rs.next()) {
				Employee employee = new Employee();
				employee.setEmployeeCode(rs.getString("employee_code"));
				employee.setEmployeeName(rs.getString("employee_name"));
				employee.setEmployeeAge(rs.getInt("employee_age"));
				employee.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
				employees.add(employee);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employees;
	}

	public Employee findEmployee(String employeeCode, String employeeName) {
		String query = null;
		String value = null;

		if (employeeName != null && !employeeName.trim().isEmpty()) {
			query = "SELECT * FROM employee WHERE employee_name = ?";
			value = employeeName.trim();
		} else if (employeeCode != null && !employeeCode.trim().isEmpty()) {
			query = "SELECT * FROM employee WHERE employee_code = ?";
			value = employeeCode.trim();
		} else {
			return null; // Không có điều kiện tìm kiếm hợp lệ
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, value);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Tạo và trả về đối tượng Employee
					Employee emp = new Employee();
					emp.setEmployeeCode(rs.getString("employee_code"));
					emp.setEmployeeName(rs.getString("employee_name"));
					emp.setEmployeeAge(rs.getInt("employee_age"));
					emp.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
					return emp;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Có thể thay bằng logger
		}

		return null; // Không tìm thấy
	}

	public boolean delete(String employeeCode) {

		String query = "DELETE FROM employee WHERE employee_code = ?";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, employeeCode);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Trả về true nếu xóa thành công
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // Trả về false nếu có lỗi trong quá trình xóa
	}

	public boolean add(Employee employee) {
		String query = "INSERT INTO employee (employee_code, employee_name, employee_age, date_of_birth) "
				+ "VALUES (?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, employee.getEmployeeCode());
			stmt.setString(2, employee.getEmployeeName());
			stmt.setInt(3, employee.getEmployeeAge());
			stmt.setDate(4, java.sql.Date.valueOf(employee.getDateOfBirth()));
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // true nếu thêm thành công

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // false nếu lỗi xảy ra
	}

	public boolean update(Employee employee) {
		String query = "UPDATE employee SET employee_name = ?, employee_age = ?, date_of_birth = ? "
				+ "WHERE employee_code = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, employee.getEmployeeName());
			stmt.setInt(2, employee.getEmployeeAge());
			stmt.setDate(3, java.sql.Date.valueOf(employee.getDateOfBirth()));
			stmt.setString(4, employee.getEmployeeCode());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // true nếu cập nhật thành công

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // false nếu có lỗi
	}

}
