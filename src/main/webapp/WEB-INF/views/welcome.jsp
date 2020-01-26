<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Welcome</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Welcome ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a></h2>

    </c:if>

</div>
<canvas id = "game-canvas" width="800" height="800"></canvas>
<script>
    var canvas = document.getElementById("game-canvas");
    var context = canvas.getContext("2d");
    var  username = "${pageContext.request.userPrincipal.name}";
    var gameID = "${message}"
    var stones= new Array();
    var player = "${c}";
    var boardSize = "${boardSize}";
    var border = canvas.width / 10;
    var boardWidth = canvas.width - (border * 2);
    var boardHeight = canvas.height - (border * 2);
    var cellWidth = boardWidth / (boardSize - 1);
    var cellHeight = boardHeight / (boardSize - 1);
    var currPlayer = 0;
    for(i = 0; i<boardSize; i++){
        for(j = 0; j<boardSize; j++)
        {
            stones[i*boardSize+j]="N";
        }
    }
   // alert(stones);
    var lastX;
    var lastY;

    drawGrid();

    function drawGrid()
    {
        var backgroundColor = '#F5DEB3';
        var gridColor = '#8B4513';

        context.fillStyle = backgroundColor;
        context.fillRect(0, 0, canvas.width, canvas.height);
        context.fillStyle = gridColor;

        for (var i = 0; i < boardSize - 1; i++)
        {
            for (var j = 0; j < boardSize - 1; j++)
            {
                context.fillRect(i * cellWidth + border, j * cellHeight + border, cellWidth - 1, cellHeight - 1);
            }
        }
        for(i = 0; i<boardSize; i++){
            for(j = 0; j<boardSize; j++)
            {
                if(stones[i*boardSize+j]=="W")placeStone(i*(cellWidth) + border, j*(cellWidth) + border, '#f8fffb');
                if(stones[i*boardSize+j]=="B")placeStone(i*(cellWidth) + border, j*(cellWidth) + border, '#000000');
            }
        }

        var quater = Math.floor((boardSize - 1) / 4);
        var markerSize = 8;
        var markerMargin = (markerSize / 2) + 0.5;

        context.fillStyle = backgroundColor;

        if (!!(boardSize % 2))
        {
            context.fillRect((((boardSize - 1) / 2) * cellWidth) + border - markerMargin, (((boardSize - 1) / 2) * cellWidth) + border - markerMargin, markerSize, markerSize);
        }

        context.fillRect((quater * cellWidth) + border - markerMargin, (quater * cellWidth) + border - markerMargin, markerSize, markerSize);
        context.fillRect((((boardSize - 1) - quater) * cellWidth) + border - markerMargin, (quater * cellWidth) + border - markerMargin, markerSize, markerSize);

        context.fillRect((((boardSize - 1) - quater) * cellWidth) + border - markerMargin, (((boardSize - 1) - quater) * cellWidth) + border - markerMargin, markerSize, markerSize);
        context.fillRect((quater * cellWidth) + border - markerMargin, (((boardSize - 1) - quater) * cellWidth) + border - markerMargin, markerSize, markerSize);

        var size = canvas.width / 40;
        var textSpacing = 10;
        context.fillStyle = '#000000';
        context.font = size + "px Arial";

        for (i = 0; i < boardSize; i++)
        {
            context.fillText((i + 1), textSpacing, ((canvas.height - border) - (i * cellHeight)) + (size / 3));
            context.fillText((i + 1), canvas.width - (size + textSpacing), ((canvas.height - border) - (i * cellHeight)) + (size / 3));

            context.fillText((String.fromCharCode(97 + i)), (border + (i * cellHeight) + (size / 3)) - (size / 1.5), textSpacing + size);
            context.fillText((String.fromCharCode(97 + i)), (border + (i * cellHeight) + (size / 3)) - (size / 1.5), canvas.height - (textSpacing * 2));
        }
    }
    setInterval(function() {
        $.ajax({
            type: "GET",
            url: "submit",
            data: {ID: gameID},
            success: function (result) {
                var help = result.split("#");
                currPlayer = help[0];
                stones = help[1].split("");
              //  alert(stones);
                drawGrid();
              //  alert(stones);
            }
    })},1000);
    canvas.addEventListener('mousemove', function(evt)
    {
        var position = getGridPoint(evt);

        if ((position.x != lastX) || (position.y != lastY))
        {
            drawGrid();

            if (((position.x >=0) && (position.x < boardSize)) && ((position.y >=0) && (position.y < boardSize)))
            {
                placeStone((position.x * cellWidth) + border, (position.y * cellWidth) + border, 'rgba(0, 0, 0, 0.2)');
            }
        }

        lastX = position.x;
        lastY = position.y;
    });
    canvas.addEventListener('click', function(evt)
    {

        var position = getGridPoint(evt);
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        if(currPlayer != player)alert("NIE TWÓJ RUCH KURWO JEBANA W DUPE");
        else {
            $.ajax({
                type: "POST",
                url: "submit",
                data: { x:  position.x, y: position.y, user: player, gameID: gameID}, // parameters
                beforeSend: function (xhr) { xhr.setRequestHeader(header,token);
                },
                success: function (result) {

                }
            });
        }

        if ((position.x != lastX) || (position.y != lastY))
        {
            drawGrid();

            if (((position.x >=0) && (position.x < boardSize)) && ((position.y >=0) && (position.y < boardSize)))
            {
                placeStone((position.x * cellWidth) + border, (position.y * cellWidth) + border, '#FF0000');
            }
        }

        lastX = position.x;
        lastY = position.y;
    });
    function placeStone(x, y, color)
    {
        var radius = cellWidth / 2;

        context.beginPath();
        context.arc(x, y, radius, 0, 2 * Math.PI, false);
        context.fillStyle = color;
        context.fill();
        context.lineWidth = 5;
    }

    function getGridPoint(evt)
    {
        var rect = canvas.getBoundingClientRect();

        var x = Math.round((evt.clientX-border-rect.left)/(rect.right-2*border-rect.left)* boardWidth);
        var y = Math.round((evt.clientY-border-rect.top)/(rect.bottom-2*border-rect.top)* boardHeight);

        var roundX = Math.round(x / cellWidth);
        var roundY = Math.round(y / cellHeight);

        return {
            x: roundX,
            y: roundY
        };
    }

</script>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
