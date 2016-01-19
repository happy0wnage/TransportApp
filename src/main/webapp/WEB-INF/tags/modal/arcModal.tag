<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<div id="addArc" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <form class="form-horizontal" name="arcForm" method="post" action="/arc/add"
                  onsubmit="return arcValidator();">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">ADD ARC</h4>
                </div>
                <div class="modal-body">
                    <fieldset>
                        <div class="form-group">
                            <label class="col-lg-2 control-label">Station from</label>

                            <div class="col-lg-10">
                                <select class="form-control" name="id_station_from">
                                    <c:forEach var="s" items="${station}">
                                        <option value="${s.id}">${s.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-lg-2 control-label">Station to</label>

                            <div class="col-lg-10">
                                <select class="form-control" name="id_station_to">
                                    <c:forEach var="s" items="${station}">
                                        <option value="${s.id}">${s.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group" id="travel_time">
                            <label class="col-lg-2 control-label">Travel time</label>

                            <div class="col-lg-10">
                                <input name="travel_time" type="text" class="form-control" placeholder="HH:mm:ss"
                                       autocomplete="off">
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