package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		List<Department> listDep = new ArrayList<>(); 
		listDep = dao.findAll();
		return listDep;
	}
	
	public void saverOrUpadete(Department dep) {
		if(dep.getId() == null) {
			dao.insert(dep);
		}
		else {
			dao.update(dep);
		}
	}
	
	public void remove(Department dep) {
		dao.deleteById(dep.getId());
	}
}
