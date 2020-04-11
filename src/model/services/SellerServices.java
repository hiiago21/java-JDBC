package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerServices {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		List<Seller> listDep = new ArrayList<>(); 
		listDep = dao.findAll();
		return listDep;
	}
	
	public void saverOrUpadete(Seller seller) {
		if(seller.getId() == null) {
			dao.insert(seller);
		}
		else {
			dao.update(seller);
		}
	}
	
	public void remove(Seller seller) {
		dao.deleteById(seller.getId());
	}
}
