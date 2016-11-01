package mums.ticketea.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDaoImpl implements ProductDao {

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	@Override
	public List<Product> findAll() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		String sql = "SELECT * FROM PRODUCT";
		
        List<Product> result = namedParameterJdbcTemplate.query(sql, params, new ProductMapper());
        
        return result;
        
	}

	private static final class ProductMapper implements RowMapper<Product> {

		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			Product product = new Product();
			product.setIdProduct(rs.getInt("id_product"));
			product.setName(rs.getString("name"));
			product.setPrice(rs.getBigDecimal("price").setScale(2, BigDecimal.ROUND_FLOOR));
			product.setQuantity(rs.getString("quantity"));
			product.setCategory(rs.getString("category"));
			return product;
		}
	}

}