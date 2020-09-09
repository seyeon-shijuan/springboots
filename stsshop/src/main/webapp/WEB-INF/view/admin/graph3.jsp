<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- /WebContent/WEB-INF/jsp/admin/graph2.jsp -->    
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>     
<html>
<head>
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
<%--    <script src="../js/Chart.min.js"></script> --%>
 <script type="text/javascript"  src=
"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.3.0/Chart.bundle.js"></script>
</head>
<body>
	<div style="width: 60%">
<canvas id="canvas" height="450" width="600"></canvas>
	</div>
	<script>
		var names = [<c:forEach items="${map}" var="m">"${m.key}" ,</c:forEach>];
		var barChart = null;
		var barChartData = {
			labels : names,
			datasets : [{
			  fillColor : "rgba(34,116,30,1)",
			  data : [<c:forEach items="${map}" var="m">"${m.value}",</c:forEach>]}]
		};
		$(function() {
		var ctx = document.getElementById("canvas").getContext("2d");
		barChart = new Chart(ctx).Bar(barChartData, {
			scaleBeginAtZero : true,
			scaleShowGridLines : true,
			scaleGridLineColor : "rgba(0,0,0,0.5)",
			scaleGridLineWidth : 1,
			barShowStroke : false,
			barStrokeWidth : 2,
			barValueSpacing : 5,
			barDatasetSpacing : 1
			});
		});
	</script>
</body>
</html>