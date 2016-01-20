/**
 * Created by Владислав on 10.01.2015.
 */

var timeRegex = /^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/;
var priceRegex = /^[\d]+(.[\d]+)?$/;
var nameRegex = /^[A-Z][a-z]+$/;

function getSpeedValue() {
    var value = document.getElementById("speed").value;
    document.getElementById("speedValue").value = value;
    document.getElementById("badge").innerHTML = value;
};

function stationValidator() {

    var timeValue = document.stationForm.stop_time.value;
    var name = document.stationForm.name.value;

    //name validation
    if (name == "" || !nameRegex.test(name)) {
        document.getElementById("name").className = "form-group has-error";
        document.stationForm.name.focus();
        return false;
    } else {
        document.getElementById("name").className = "form-group";
    }

    //time validator
    if (timeValue == "" || !timeRegex.test(timeValue)) {
        document.getElementById("stop_time").className = "form-group has-error";
        document.stationForm.stop_time.focus();
        return false;
    } else {
        document.getElementById("stop_time").className = "form-group";
    }
}

function arcValidator() {

    var timeValue = document.arcForm.travel_time.value;

    if (timeValue == "" || !timeRegex.test(timeValue)) {
        document.getElementById("travel_time").className = "form-group has-error";
        document.arcForm.travel_time.focus();
        return false;
    } else {
        document.getElementById("travel_time").className = "form-group";
    }
}

function userValidator() {

    var loginDiv = $("#loginDiv");
    var passwordDiv = $("#passwordDiv");

    var login = $("#inputLogin");
    var password = $("#inputPassword");

    if (login.val() == "") {
        loginDiv.addClass('has-error');
        loginDiv.focus();
        return false;
    } else {
        loginDiv.removeClass('has-error');
    }

    if (password.val() == "") {
        passwordDiv.addClass('has-error');
        passwordDiv.focus();
        return false;
    } else {
        passwordDiv.removeClass('has-error');
    }
}

function routeValidator() {
    var routing_number = document.routeForm.routing_number.value;
    var price = document.routeForm.price.value;
    var first_bus_time = document.routeForm.first_bus_time.value;
    var last_bus_time = document.routeForm.last_bus_time.value;
    var last_bus_time = document.routeForm.last_bus_time.value;
    var selectedCount = $("#arcList option:selected").length;

    //routing_number validator
    if (routing_number == "") {
        document.getElementById("routing_number").className = "form-group has-error";
        document.routeForm.routing_number.focus();
        return false;
    } else {
        document.getElementById("routing_number").className = "form-group";
    }

    //price validation
    if (price == "" || !priceRegex.test(price)) {
        document.getElementById("price").className = "form-group has-error";
        document.routeForm.price.focus();
        return false;
    } else {
        document.getElementById("price").className = "form-group";
    }

    //time validation
    if (first_bus_time == "" || !timeRegex.test(first_bus_time)) {
        document.getElementById("first_bus_time").className = "form-group has-error";
        document.routeForm.first_bus_time.focus();
        return false;
    } else {
        document.getElementById("first_bus_time").className = "form-group";
    }

    //time validation
    if (last_bus_time == "" || !timeRegex.test(last_bus_time)) {
        document.getElementById("last_bus_time").className = "form-group has-error";
        document.routeForm.last_bus_time.focus();
        return false;
    } else {
        document.getElementById("last_bus_time").className = "form-group";
    }

    if (selectedCount < 1) {
        $("#arc_list_alert").show();
        return false;
    }

}

function hideOther() {
    var selects = $("#selects").find("select");
    var flag = false;
    var temp = 0;
    for (var iter = 1; iter < selects.length + 1; iter++) {
        var select = selects[iter - 1];
        var option = $("#" + iter + " :selected").text()
        if (option != "none") {
            flag = true;
            temp = iter;
        }
    }

    if (flag) {
        for (var iter = 1; iter < selects.length + 1; iter++) {
            if (temp != iter) {
                $("#" + iter).prop("disabled", true);
            }
        }
    } else {
        for (var iter = 1; iter < selects.length + 1; iter++) {
            $("#" + iter).prop("disabled", false);
        }
    }
}

function toHHMMSS(time) {
    return moment().startOf('day')
        .seconds(time)
        .format('H:mm:ss')
}

$(document).ready(function () {

    $("#startButton").click(function () {
        $("#pauseButton").show();
        $("#startButton").hide();
        $("#loading").show();
        $("#optimization").hide();
        $("#add_bus_process").hide();
        $("#downloadButton").hide();
    });

    $("#pauseButton").click(function () {
        $("#continueButton").show();
        $("#pauseButton").hide();
        $("#startButton").hide();
        $("#loading").hide();
        $("#add_bus_process").show();
        $("#current_position").show();

        $.ajax({
            type: "GET",
            url: "/simulation/pause",
            success: function (result) {
                $("#current_position").find("tbody").empty();
                $("#stateField").html("Time: " + toHHMMSS(result[0].travelTime + result[0].route.firstBusTime));
                result.forEach(function (item) {
                    var tr = $("<tr></tr>");
                    var td = $("<td></td>");

                    var span = $("<span></span>");
                    $(span).attr("aria-hidden", "true");

                    if (item.direction == "STRAIGHT") {
                        span.addClass("glyphicon glyphicon-arrow-right");
                    } else {
                        span.addClass("glyphicon glyphicon-arrow-left");
                    }

                    $(tr).append(
                        "<td>" + item.id + "</td>",
                        "<td>" + item.route.routingNumber + "</td>",
                        "<td>" + item.currentStation.name + "</td>",
                        "<td>" + toHHMMSS(item.timeToStation) + "</td>",
                        "<td>" + toHHMMSS(item.travelTime) + "</td>",
                        "<td>" + item.passengerList.length + "</td>",
                        td.html(span)
                    )
                    ;
                    $("#current_position").find("tbody").append(tr);
                });
            }
        });
    });

    $("#continueButton").click(function () {
        $("#pauseButton").show();
        $("#startButton").hide();
        $("#continueButton").hide();
        $("#loading").show();
        $("#add_bus_process").hide();
        $("#current_position").hide();
        $.ajax({
            type: "GET",
            url: "/simulation/pause"
        });
    });

    $("#addBusButton").click(function (e) {
        e.preventDefault();
        var idRoute;
        var idStation;
        var seats = $("#idSeat :selected").text();

        var selects = $("#selects").find("select");
        var flag = true;
        for (var iter = 1; iter < selects.length + 1; iter++) {
            var select = selects[iter - 1];
            var option = $("#" + iter + " :selected");
            if (option.text() != "none") {
                flag = false;
                idRoute = select.name;
                idStation = option.val();
                $(select).removeClass('has-error')
            }
        }

        if (!flag) {
            $.ajax(
                {
                    type: "POST",
                    url: "/simulation/addBus",
                    data: {id_route: idRoute, id_station: idStation, seat: seats},
                    success: function(e) {
                        $("#pauseButton").show();
                        $("#startButton").hide();
                        $("#loading").show();
                        $("#optimization").hide();
                        $("#add_bus_process").hide();
                        $("#continueButton").hide();
                        $("#current_position").hide();
                        for (var iter = 1; iter < selects.length + 1; iter++) {
                            var select = selects[iter - 1];
                            $(select).removeClass("error");
                            $(select).val("none")
                            $("#" + iter).prop("disabled", false);
                        }
                    },
                    error: function(e){
                        alert(e.responseText);
                    }

                }
            );
        } else {
            for (var iter = 1; iter < selects.length + 1; iter++) {
                var select = selects[iter - 1];
                $(select).addClass("error");
            }
        }
    });
});


