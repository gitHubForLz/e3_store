<html>
<head>
</head>
<body>
<table border="1">
	<#list list as student>
		
	<#if student_index % 2 == 0>
	<tr bgcolor="red">
<#else>
<tr bgcolor="blue">
</#if>

	
		<td>姓名
		</td>
		<td>${student.name}
		</td>
	</tr>
	<tr>
		<td>年龄
		</td>
		<td>${student.age}
		</td>
	</tr>
	<tr>
		<td>学号
		</td>
		<td>${student.number}
		</td>
	</tr>
	</#list>
	
</table>
${hello}...
shijian ${date?string("yyyy/MM/dd HH:mm:ss")}  <br>
null ${nulls!"这是空null"}
</body>
</html>
