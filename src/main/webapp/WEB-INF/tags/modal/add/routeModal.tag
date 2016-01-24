<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<div id="addRoute" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <form class="form-horizontal" name="routeForm" method="post" action="/route/add"
                  onsubmit="return routeValidator();" id="routeAddForm">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">ADD ROUTE</h4>
                </div>
                <div class="modal-body">
                    <fieldset>
                        <div class="form-group" id="routing_number">
                            <label class="col-lg-2 control-label">Number</label>

                            <div class="col-lg-10">
                                <input name="routing_number" type="text" class="form-control" placeholder="Number"
                                       autocomplete="off">
                            </div>
                        </div>
                        <div class="form-group" id="price">
                            <label class="col-lg-2 control-label">Price</label>

                            <div class="col-lg-10">
                                <input name="price" type="text" class="form-control" placeholder="Example:  5.5"
                                       autocomplete="off">
                            </div>
                        </div>

                        <div class="form-group" id="depot_stop_time">
                            <label class="col-lg-2 control-label">Depot stop time</label>

                            <div class="col-lg-10">
                                <input name="depot_stop_time" type="text" class="form-control" placeholder="HH:mm:ss"
                                       autocomplete="off">
                            </div>
                        </div>

                        <div class="form-group" id="first_bus_time">
                            <label class="col-lg-2 control-label">First bus time</label>

                            <div class="col-lg-10">
                                <input name="first_bus_time" type="text" class="form-control" placeholder="HH:mm:ss"
                                       autocomplete="off">
                            </div>
                        </div>

                        <div class="form-group" id="last_bus_time">
                            <label class="col-lg-2 control-label">Last bus time</label>

                            <div class="col-lg-10">
                                <input name="last_bus_time" type="text" class="form-control" placeholder="HH:mm:ss"
                                       autocomplete="off">
                            </div>
                        </div>
                        <hr/>
                        <c:set var="idStation" value="1"/>
                        <c:forEach var="key" items="${station}">
                            <div class="form-group">
                                <label class="col-lg-2 control-label">Station&nbsp;#${idStation}</label>

                                <div class="col-lg-10">
                                    <select class="form-control" name="idst${idStation}" onchange="showSelect('#routeAddForm')">
                                        <option value="0">None</option>
                                        <c:forEach var="s" items="${station}">
                                            <option value="${s.id}">${s.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <c:set var="idStation" value="${idStation + 1}"/>
                        </c:forEach>

                    </fieldset>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-success">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>