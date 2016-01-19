<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<elem:head/>
<mod:menu/>

<body>
<div class="center">

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-dismissible alert-danger">
            <button type="button" class="close" data-dismiss="alert">x</button>
                ${errorMessage}
        </div>
    </c:if>

    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">Routes</h3>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>Number</th>
                    <th>Price</th>
                    <th>Type</th>
                    <th>Bus count</th>
                    <th>Depot stop time</th>
                    <th>First bus time</th>
                    <th>Last bus time</th>
                    <th>From-To&nbsp;&nbsp;(timeToStation)</th>
                    <c:if test="${not empty logged_user}">
                        <th>Option</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${route}">
                    <tr>
                        <td>${r.routingNumber}</td>
                        <td>${r.price}</td>
                        <td>${r.type}</td>
                        <td>${r.busCount}</td>
                        <td><fmt:formatDate value="${r.depotStopTime}" pattern="HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${r.firstBusTime}" pattern="HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${r.lastBusTime}" pattern="HH:mm:ss"/></td>
                        <td class="left">
                            <ul>
                                <c:forEach var="a" items="${r.arcList}">
                                    <li>
                                            ${a.fromStation.name}&nbsp;&mdash;&nbsp;${a.toStation.name}&nbsp;(<fmt:formatDate
                                            value="${a.travelTime}"
                                            pattern="HH:mm:ss"/>)
                                    </li>
                                </c:forEach>
                            </ul>
                        </td>
                        <c:if test="${not empty logged_user}">
                            <td>
                                <a href="/route/delete?id_route=${r.id}&count=${r.busCount}">
                                    <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                                </a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- ADD ROUTE -->
            <c:if test="${not empty logged_user}">
                <a href="#" data-toggle="modal" data-target="#addRoute" class="btn btn-primary">Add route</a>
                <mod:routeModal/>
            </c:if>
        </div>
    </div>


    <hr/>

    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">Stations</h3>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Stop time</th>
                    <c:if test="${not empty logged_user}">
                        <th>Option</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${station}">
                    <tr>
                        <td>${s.name}</td>
                        <td><fmt:formatDate value="${s.stopTime}" pattern="HH:mm:ss"/></td>
                        <c:if test="${not empty logged_user}">
                            <td>
                                <a href="/station/delete?id_station=${s.id}">
                                    <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                                </a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- ADD STATION -->
            <c:if test="${not empty logged_user}">
                <a href="#" data-toggle="modal" data-target="#addStation" class="btn btn-primary">Add station</a>
                <mod:stationModal/>
            </c:if>
        </div>
    </div>

    <hr/>

    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">Buses</h3>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Routing number</th>
                    <th>Seats</th>
                    <c:if test="${not empty logged_user}">
                        <th>Option</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="b" items="${bus}">
                    <tr>
                        <td>${b.id}</td>
                        <td>${b.route.routingNumber}</td>
                        <td>${b.seat}</td>
                        <c:if test="${not empty logged_user}">
                            <td>
                                <a href="/bus/delete?id_bus=${b.id}">
                                    <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                                </a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- ADD BUSES -->
            <c:if test="${not empty logged_user}">
                <a href="#" data-toggle="modal" data-target="#addBus" class="btn btn-primary">Add bus</a>
                <mod:busModal/>
            </c:if>
        </div>
    </div>

    <hr/>

    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">Arcs</h3>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>Station from</th>
                    <th>Station to</th>
                    <th>Travel time</th>
                    <th>Option</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="a" items="${arc}">
                    <tr>
                        <td>${a.fromStation.name}</td>
                        <td>${a.toStation.name}</td>
                        <td>${a.travelTime}</td>
                        <td>
                            <a href="/arc/delete?id_arc=${a.id}">
                                <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- ADD BUSES -->
            <c:if test="${not empty logged_user}">
                <a href="#" data-toggle="modal" data-target="#addArc" class="btn btn-primary">Add arc</a>
                <mod:arcModal/>
            </c:if>
        </div>
    </div>

</div>
<c:remove var="errorMessage" scope="session"/>
</body>
</html>