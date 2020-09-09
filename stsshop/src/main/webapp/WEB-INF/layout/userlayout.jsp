<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WebContent/layout/userlayout.jsp --%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />          
<!DOCTYPE html>
<html>
<title><sitemesh:write property='title' /></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
*  { margin: 0px; padding: 0px; }
html,body,h1,h2,h3,h4,h5 {font-family: "Raleway", sans-serif}

</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript" 
src="https://www.chartjs.org/dist/2.9.3/Chart.min.js"></script>
<script type="text/javascript" 
   src="http://cdn.ckeditor.com/4.5.7/standard/ckeditor.js"></script>
<decorator:head />
<link rel="stylesheet" href="${path}/css/main.css">  
<body class="w3-light-grey">

<!-- Top container -->
<div class="w3-bar w3-top w3-black w3-large" style="z-index:4">
  <button class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-light-grey" onclick="w3_open();"><i class="fa fa-bars"></i> &nbsp;Menu</button>
  <span class="w3-bar-item w3-right">
    <c:if test="${empty sessionScope.loginUser }">
       <a href="${path}/user/login.shop">로그인</a>
       <a href="${path}/user/userEntry.shop">회원가입</a>
    </c:if>
    <c:if test="${!empty sessionScope.loginUser }">
      ${sessionScope.loginUser.username}님이 로그인 하셨습니다.&nbsp;&nbsp;
       <a href="${path}/user/logout.shop">로그아웃</a>
    </c:if>
  </span>
</div>

<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
  <div class="w3-container w3-row">
    <div class="w3-col s4">
      <img src="${path}/img/3_over.jpg" class="w3-circle w3-margin-right" style="width:46px">
    </div>
    <div class="w3-col s8 w3-bar">
    <c:if test="${!empty sessionScope.loginUser }">
      <span>Welcome, <strong>${sessionScope.loginUser.username}</strong></span><br>
    </c:if>  
    <c:if test="${empty sessionScope.loginUser }">
      <span><strong>로그인 하세요</strong></span><br>
    </c:if>  
      <a href="#" class="w3-bar-item w3-button"><i class="fa fa-envelope"></i></a>
      <a href="#" class="w3-bar-item w3-button"><i class="fa fa-user"></i></a>
      <a href="#" class="w3-bar-item w3-button"><i class="fa fa-cog"></i></a>
    </div>
  </div>
  <hr>
  <div class="w3-container">
    <h5>프로그램연습</h5>
  </div>
  <div class="w3-bar-block">
    <a href="#" class="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black" onclick="w3_close()" title="close menu"><i class="fa fa-remove fa-fw"></i>&nbsp; Close Menu</a>
    <a href="#" class="w3-bar-item w3-button w3-padding " style="color:#D1B6E1;"><i class="fa fa-users fa-fw"></i>&nbsp; Overview</a>
    <a href="${path}/user/main.shop" class="w3-bar-item w3-button w3-padding"><i class="fa fa-eye fa-fw"></i>&nbsp; 회원관리</a>
    <a href="${path}/item/list.shop" class="w3-bar-item w3-button w3-padding"><i class="fa fa-eye fa-fw"></i>&nbsp; 상품관리</a>
    <a href="${path}/board/list.shop" class="w3-bar-item w3-button w3-padding"><i class="fa fa-users fa-fw"></i>&nbsp; 게시판</a>
    <a href="${path}/chat/chat.shop" class="w3-bar-item w3-button w3-padding"><i class="fa fa-users fa-fw"></i>&nbsp; 채팅</a>
  </div>
  <div class="w3-container">
   <%-- ajax을 통해 얻은 환율 정보 내용 출력 --%>
    <div id="exchange"></div>      
  </div><br>  
  <div class="w3-display-container">
  <%-- KEB하나은행 정보 출력하기 : 
      USD,JPY,EUR,CNY : 매매기준율, 현찰사실때,현찰파실때 --%>
    <div id="exchange2"></div>      
  </div>  
</nav>


<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px;margin-top:43px;">

  <!-- Header -->
  <header class="w3-container" style="padding-top:22px">
    <h5><b><i class="fa fa-dashboard"></i> 프로그램 연습</b></h5>
  </header>
  <div class="w3-row-padding w3-margin-bottom">
    <div class="w3-half">
      <div class="w3-container w3-padding-16">
       <div id="piecontainer" 
            style="width:80%; border:1px solid #ffffff">
		<canvas id="canvas1" style="width:100%;"></canvas>
	   </div>
      </div>
    </div>
    <div class="w3-half">
      <div class="w3-container w3-padding-16">
       <div id="barcontainer" 
            style="width:80%; border:1px solid #ffffff">
		<canvas id="canvas2" style="width: 100%;"></canvas>
	   </div>
    </div>
  </div>
  <div class="w3-container w3-light-grey w3-padding-32">
    <div class="w3-row">
      <div class="w3-container w3-col" style="width:95%;">
        <sitemesh:write property='body' />
      </div>
      <div class="w3-container w3-col" style="width:5%;">
        <p>&nbsp;</p>
      </div>
    </div>
  </div>

  <!-- Footer -->
  <footer class="w3-container w3-padding-16 w3-light-grey">
    <h4>구디 아카데미 Since 2016</h4>
    <p>Powered by <a href="http://www.goodee.co.kr" target="_blank">구디아카데미</a></p>
  </footer>

  <!-- End page content -->
</div>

<script>
// Get the Sidebar
var mySidebar = document.getElementById("mySidebar");
// Get the DIV with overlay effect
var overlayBg = document.getElementById("myOverlay");

// Toggle between showing and hiding the sidebar, and add overlay effect
function w3_open() {
  if (mySidebar.style.display === 'block') {
    mySidebar.style.display = 'none';
    overlayBg.style.display = "none";
  } else {
    mySidebar.style.display = 'block';
    overlayBg.style.display = "block";
  }
}
// Close the sidebar with the close button
function w3_close() {
  mySidebar.style.display = "none";
  overlayBg.style.display = "none";
}
</script>
<script>
  var randomColorFactor = function() {
	  return Math.round(Math.random() * 255);
  }
  var randomColor = function(opa) {
	  return "rgba("+ randomColorFactor() + ","
			  + randomColorFactor() + ","
			  + randomColorFactor() + ","
			  + (opa || '.3') + ")";
  }
  $(function(){
	  piegraph();
	  bargraph();
	  exchangeRate(); //환율 정보를 ajax을 통해 크롤링하기.
	  exchangeRate2();
  })
  function exchangeRate() {
	  $.ajax("${path}/ajax/exchange1.shop",{
		  success : function(data) {
			  $("#exchange").html(data);
		  },
		  error : function(e){
			  alert("환율 조회시 서버 오류:"+e.status);
		  }
	  })
  }
  function exchangeRate2() {
	  $.ajax("${path}/ajax/exchange2.shop",{
		  success : function(data) {
			  console.log(data)
			  $("#exchange2").html(data);
		  },
		  error : function(e){
			  alert("환율 조회시 서버 오류:"+e.status);
		  }
	  })
  }
  function piegraph() {
	  $.ajax("${path}/ajax/graph1.shop",{
		  success : function(data) { //data : json형태의 문자열로 수신
			  pieGraphPrint(data);
		  },
		  error : function(e) {
			  alert("서버 오류:" + e.status);
		  }
	  })
  }
  function bargraph() {
	  $.ajax("${path}/ajax/graph2.shop",{ //최근7일간의 게시물등록 건수
		  success : function(data) {
			  barGraphPrint(data);
		  },
		  error : function(e) {
			  alert("서버 오류:" + e.status);
		  }
	  })
  }
  function pieGraphPrint(data) {
//	console.log(data)
	var rows = JSON.parse(data);
	var names = []
	var datas = []
	var colors = []
	$.each(rows,function(index,item) {
		names[index] = item.name;
		datas[index] = item.cnt;
		colors[index] = randomColor(0.7);
	})
	var config = {
		  type : 'pie',
		  data : {
			  datasets : [{
				  data : datas,
				  backgroundColor:colors
			  }],
			  labels : names
		  },
	      options : {
	    	responsive : true,
	    	legend : {position : 'top'},
	    	title : {
	    		display : true,
	    		text : '글쓴이 별 게시판 등록 건수',
	    		position : 'bottom'
	    	}
	      }
	}
	var ctx = document.getElementById("canvas1").getContext("2d");
	new Chart(ctx,config);
  }
  function barGraphPrint(data) {
//		console.log(data)
      var rows = JSON.parse(data);
      var regdates = [];
      var datas = [];
      var colors = [];
	  $.each(rows,function(index,item) {
		regdates[index] = item.regdate;
		datas[index] = item.cnt;
		colors[index] = randomColor(1);
	  })
	  var chartData = {
			labels: regdates,
			datasets: [{
				type: 'line',
				borderWidth: 2,
				borderColor:colors,
				label: '건수',
				fill: false,
				data: datas,
			}, {
				type: 'bar',
				label: '건수',
				backgroundColor: colors,
				data: datas
			}]
	      }
	    var config = {
				type: 'bar',
				data: chartData,
				options: {			
					responsive: true,
					title: {display: true,
						    text: '최근 7일 게시판 등록 건수',
						    position : 'bottom'
					},
					legend : {display : false },
					scales: {
						xAxes: [{ display : true,   stacked : true }],			
						yAxes: [{ display : true,   stacked : true }]			
				    }
				}
	    }
		var ctx = document.getElementById('canvas2').getContext('2d');
		new Chart(ctx,config);
  }
</script>
</body>
</html>