<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<div id="addBus" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <form class="form-horizontal" method="post" action="/bus/add">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">ADD BUSr</h4>
                </div>
                <div class="modal-body">
                    <fieldset>
                        <div class="form-group">
                            <label class="col-lg-2 control-label">Route</label>

                            <div class="col-lg-10">
                                <select class="form-control" name="id_route">
                                    <c:forEach var="r" items="${route}">
                                        <option value="${r.id}">${r.routingNumber}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 control-label">Seats</label>

                            <div class="col-lg-10">
                                <select class="form-control" name="seat">
                                    <c:forEach var="i" begin="10" end="35" step="1">
                                        <option value="${i}">${i}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
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