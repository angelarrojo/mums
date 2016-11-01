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
			<c:forEach var="products" varStatus="status" items="${productForm}">
				<tr>
					<td>${products.name}</td>
					<td>${products.price} Eur</td>
					<td>${products.quantity}</td>
					<td>${products.category}</td>
					<td>${products.unit}</td>					
				</tr>
			</c:forEach>			
		</table>
<h1>El precio total es: ${msg}</h1>
</body>
</html>

