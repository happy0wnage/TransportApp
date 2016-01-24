<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<elem:head/>
<modal:menu/>

<body>
<div class="center">

    <c:if test="${not empty logged_user}">
        <div class="round">
            <button id="pauseButton" type="button" class="btn btn-warning btn-circle btn-xl block-hide"><i
                    class="glyphicon glyphicon-pause"></i></button>
            <button id="continueButton" type="button" class="btn btn-info btn-circle btn-xl block-hide"><i
                    class="glyphicon glyphicon-play"></i></button>
        </div>
        <div class="middle block-hide" id="loading">
            <img src="/resources/image/365.gif"/>
        </div>

        <div class="panel panel-primary block-hide" id="current_position">
            <div class="panel-heading">
                <h2 class="panel-title middle" id="stateField"></h2>
            </div>
            <div class="panel-body">
                <table class="table table-striped table-hover ">
                    <thead>
                    <tr class="active">
                        <th>ID Bus</th>
                        <th>Routing number</th>
                        <th>Move from station</th>
                        <th>Time to station</th>
                        <th>Travel time</th>
                        <th>Passengers</th>
                        <th>Direction</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
        <div class="panel panel-primary block-hide" id="add_bus_process">
            <div class="panel-body">
                <fieldset>
                    <table class="table table-striped table-hover" id="selects">
                        <c:set var="idRoute" value="1"/>
                        <c:forEach var="r" items="${route}">
                            <tr>
                                <div class="form-group">
                                    <td>
                                        <label class="col-lg-2 control-label">${r.routingNumber}</label>
                                    </td>
                                    <td>
                                        <div class="col-lg-10">
                                            <select class="form-control" name="${r.id}" id="route${idRoute}"
                                                    onchange="return hideOther()">
                                                <option value="none">none</option>
                                                <c:forEach var="s" items="${r.stations}">
                                                    <option value="${s.id}">${s.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </div>
                            </tr>
                            <c:set var="idRoute" value="${idRoute + 1}"/>
                        </c:forEach>
                    </table>

                    <div class="form-group">
                        <label class="col-lg-2 control-label">Seats passengersCount</label>

                        <div class="col-lg-10">
                            <select class="form-control" name="seat" id="idSeat">
                                <c:forEach var="i" begin="10" end="35" step="1">
                                    <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </fieldset>
            </div>
            <div class="panel-footer">
                <input id="addBusButton" type="submit" class="btn btn-primary btn-block" value="Add bus"/>
            </div>
        </div>
        <form action="/simulation/start" class="horizontal-form">
            <input type="hidden" name="speedValue" id="speedValue" value="0"/>

            <div class="round low">
                <button id="startButton" type="submit" class="btn btn-success btn-circle btn-xl"><i
                        class="glyphicon glyphicon-play"></i></button>
            </div>

            <div id="optimization">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Simulation speed&nbsp;&nbsp;<span class="badge" id="badge">0</span></h3>
                    </div>
                    <div class="panel-body">
                        <input type="range" min="0" max="100" step="1" value="0" id="speed" onclick="getSpeedValue()"/>
                    </div>
                </div>

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Passenger Count</h3>
                    </div>
                    <div class="panel-body">
                        <table class="range">
                            <tr>
                                <c:forEach var="time" items="${daily_flow}">
                                    <th>${time.timeFrom} - ${time.timeTo}</th>
                                </c:forEach>
                            </tr>
                            <tr>
                                <c:forEach var="value" items="${daily_flow}">
                                    <td><input name="${value.id}" orient="vertical" type="range" min="1" max="50"
                                               step="5"
                                               value="${value.passengersCount}"></td>
                                </c:forEach>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <br/>
        </form>
    </c:if>

    <c:if test="${not empty results}">

        <a id="downloadButton" href="/download/results" class="btn btn-default btn-block">Download results&nbsp;&nbsp;&nbsp;<span
                class="glyphicon glyphicon-save" aria-hidden="true"></span></a>
        <br/>

        <table class="table table-striped table-hover ">
            <thead>
            <tr class="active">
                <th>Start date</th>
                <th>End date</th>
                <th>Route</th>
                <th>Loading buses</th>
                <th>Passengers</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="r" items="${results}">
                <tr>
                    <td>${r.startDate}</td>
                    <td>${r.endDate}</td>
                    <td>${r.routes}</td>
                    <td>
                        <div class="progress">
                            <div class="progress-bar progress-bar-success" style="width: ${r.loadingPercent}%"></div>
                        </div>
                    </td>
                    <td>
                        <c:set var="percent"
                               value="${(r.satisfiedCount * 100)/(r.satisfiedCount + r.dissatisfiedCount)}"/>
                        <fmt:formatNumber var="p" value="${percent}" maxFractionDigits="0"/>
                        <strong>Satisfied:&nbsp;</strong>${r.satisfiedCount} <strong><abbr
                            title="Dissatisfied: ${r.dissatisfiedCount}">(${p} %)</abbr></strong>
                    </td>
                </tr>

            </c:forEach>

            </tbody>
        </table>
    </c:if>

</div>

</body>
</html>