<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<elem:head/>
<modal:notLoggedmenu/>

<body>
<div class="center">

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-dismissible alert-danger">
            <button type="button" class="close" data-dismiss="alert">x</button>
                ${errorMessage}
        </div>
    </c:if>
    <c:if test="${not empty validationErrors}">
        <div class="alert alert-dismissible alert-danger">
            <button type="button" class="close" data-dismiss="alert">x</button>
            <c:forEach var="err" items="${validationErrors}">
                ${err}
                <br/>
            </c:forEach>
        </div>
    </c:if>

    <form class="form-horizontal" action="/user/add" method="post" onsubmit="return userValidator()">
        <fieldset>
            <legend>Register</legend>
            <div class="form-group" id="loginDiv">
                <label class="col-lg-2 control-label">Login</label>

                <div class="col-lg-10">
                    <input name="inputLogin" id="inputLogin" type="text" class="form-control" placeholder="Login"
                           autocomplete="off">
                </div>
            </div>
            <div class="form-group" id="passwordDiv">
                <label class="col-lg-2 control-label">Password</label>

                <div class="col-lg-10">
                    <input name="inputPassword" id="inputPassword" type="password" class="form-control"
                           placeholder="Password"
                           autocomplete="off">
                </div>
            </div>

            <div class="form-group">
                <div class="col-lg-10 col-lg-offset-2">
                    <input type="submit" class="btn btn-primary large" value="Submit"/>
                </div>
            </div>
        </fieldset>
    </form>
</div>

<c:remove var="errorMessage" scope="session"/>
<c:remove var="validationErrors" scope="session"/>
</body>
</html>