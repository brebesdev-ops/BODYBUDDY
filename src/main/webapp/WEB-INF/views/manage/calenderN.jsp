<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset='utf-8' />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/packages/core/main.css" rel='stylesheet' />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/packages/daygrid/main.css" rel='stylesheet' />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/packages/timegrid/main.css" rel='stylesheet' />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/packages/list/main.css" rel='stylesheet' />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/packages/core/main.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/packages/interaction/main.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/packages/daygrid/main.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/packages/timegrid/main.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/packages/list/main.js"></script>
	<script src="https://code.jquery.com/jquery-3.0.0.min.js"></script>

<style>

  body {
    margin: 40px 10px;
    padding: 0;
    font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
    font-size: 14px;
  }

  #calendar {
    max-width: 900px;
    margin: 0 auto;
  }

</style>
</head>
<body>

  <div id='calendar'></div>

<input type="hidden" id="mid" value="${m_id}"/>
<input type="hidden" id="pscode" value="${ps_code}"/>
</body>
<script>

  document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
      plugins: [ 'interaction', 'dayGrid', 'timeGrid', 'list' ],
      header: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
      },
      defaultDate: '2019-06-12',
      navLinks: true, // can click day/week names to navigate views

      weekNumbers: true,
      weekNumbersWithinDays: true,
      weekNumberCalculation: 'ISO',

      editable: true,
      eventLimit: true, // allow "more" link when too many events
      events: [      ]
    });

    calendar.render();
  }); //END

  var mid = $('#mid').val();
  var pscode = $('#pscode').val();
  var date = new Date();
  var currentYear = date.getFullYear();
  var currentMonth = date.getMonth()+1;
  var currentDay = date.getDate();
  $(document).ready(function(){
	  console.log("상키ㅁㅁㅁㅁㅁㅁ")
	  $.ajax({
			type:'post', 
			url:'dailyCheck',
			data:{"m_id":$("#mid").val(),"ps_code":$("#pscode").val()},
			dataType:'json',
			success:function(data){
				console.log(data)
				console.log(currentYear)
				console.log(currentMonth)
				console.log(currentDay)
			
			},
			error:function(error){
				console.log(error);
			}
		
		});   
  });

</script>
	
</html>