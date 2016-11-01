<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
 
<h1>Mum's</h1>
<form:form method="post" action="result" modelAttribute="productForm">
		<table >
			<thead>
				<tr>
					<th>NAME</th>
					<th>PRICE</th>
					<th>QUANTITY</th>
					<th>TYPE</th>
					<th>UNITS</th>					
				</tr>
			</thead>
			<c:forEach var="products" varStatus="status" items="${productForm.products}">
				<tr>
					<td>${products.name} <input type="hidden" name="products[${status.index}].name" value="${products.name}"/></td>
					<td>${products.price} Eur <input type="hidden" name="products[${status.index}].price" value="${products.price}"/></td>
					<td>${products.quantity} <input type="hidden" name="products[${status.index}].quantity" value="${products.quantity}"/></td>
					<td>${products.category} <input type="hidden" name="products[${status.index}].category" value="${products.category}"/></td>
					<td><input name="products[${status.index}].unit" value="${products.unit}" size="2" maxlength="2"/></td>					
				</tr>
			</c:forEach>			
			<tr>
			<td><input type="submit" value="Bill" /></td>
			</tr>
		</table>
</form:form>
</body>
</html>

