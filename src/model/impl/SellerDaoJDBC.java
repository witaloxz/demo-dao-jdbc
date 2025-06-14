package model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		
		
	}

	@Override
	public void update(Seller obj) {
		
		
	}

	@Override
	public void deleById(Integer id) {
		
		
	}

	
	@Override
	public Seller findById(Integer id) {
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    try {
	        st = conn.prepareStatement(
	            "SELECT seller.*, department.Name AS DepartmentName " +
	            "FROM seller " +
	            "INNER JOIN department ON seller.DepartmentId = department.Id " +
	            "WHERE seller.Id = ?"
	        );

	        st.setInt(1, id);
	        rs = st.executeQuery();

	        if (rs.next()) {
	            Department dep = instantiateDepartment(rs);
	            Seller obj = instatiateSeller(rs, dep);
	            return obj;
	        }
	        return null;
	    } catch (SQLException e) {
	        throw new DbException(e.getMessage());
	    } finally {
	        DB.closeStatement(st);
	        DB.closeResultSet(rs);
	    }
	}


	private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
	
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepartmentName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		
		return null;
	}
	

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT s.*, d.Name AS DepartmentName "
							+ "FROM seller AS s "
							+ "JOIN department AS d ON d.Id = s.DepartmentId "
							+ "WHERE s.DepartmentId = ? "
							+ "ORDER BY d.Name, s.Name "); 

			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list =  new ArrayList<>();
			Map<Integer,Department> map = new HashMap<>();
			
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentID"));
				
				if(dep == null){
					dep= instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentID"), dep);
				}
				
				Seller obj = instatiateSeller(rs,dep);
				list.add(obj);
				
		
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

}
