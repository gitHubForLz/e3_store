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

	
		<td>����
		</td>
		<td>${student.name}
		</td>
	</tr>
	<tr>
		<td>����
		</td>
		<td>${student.age}
		</td>
	</tr>
	<tr>
		<td>ѧ��
		</td>
		<td>${student.number}
		</td>
	</tr>
	</#list>
	
</table>
${hello}...
shijian ${date?string("yyyy/MM/dd HH:mm:ss")}  <br>
null ${nulls!"���ǿ�null"}
</body>
</html>
