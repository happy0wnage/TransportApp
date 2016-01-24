<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<div id="addStation" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <form class="form-horizontal" name="stationForm" method="post" action="/station/add"
                  onsubmit="return stationValidator();">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">ADD STATION</h4>
                </div>
                <div class="modal-body">
                    <fieldset>
                        <div class="form-group" id="name">
                            <label class="col-lg-2 control-label">Name</label>

                            <div class="col-lg-10">
                                <input name="name" type="text" class="form-control" placeholder="Station name"
                                       autocomplete="off">
                            </div>
                        </div>
                        <div class="form-group" id="stop_time">
                            <label class="col-lg-2 control-label">Stop time</label>

                            <div class="col-lg-10">
                                <input name="stop_time" type="text" class="form-control" placeholder="HH:mm:ss"
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