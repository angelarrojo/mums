package mums.ticketea.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import org.junit.Assert;

public class ProductDaoTest {

    private EmbeddedDatabase db;

    ProductDao productDao;
    
    @Before
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
    	db = new EmbeddedDatabaseBuilder()
    		.setType(EmbeddedDatabaseType.H2)
    		.addScript("db/sql/create-db.sql")
    		.addScript("db/sql/insert-data.sql")
    		.build();
    }

    @Test
    public void testFindByname() {
    	NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
    	ProductDaoImpl productDao = new ProductDaoImpl();
    	productDao.setNamedParameterJdbcTemplate(template);
    	
    	List<Product> products = productDao.findAll();
    	
    	Assert.assertEquals(10, products.size());
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

}