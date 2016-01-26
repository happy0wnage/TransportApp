var timeRegex = /^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/;
var priceRegex = /^[\d]+(\.[\d]{1,2})?$/;
var nameRegex = /^[^?!,*&@%^|]+$/;

function getSpeedValue() {
    var value = document.getElementById("speed").value;
    document.getElementById("speedValue").value = value;
    document.getElementById("badge").innerHTML = value;
};

function stationValidator(form) {

    var timeValue = $(form).find("#stop_time");
    var name = $(form).find("#name");

    //name validation
    var nameValue = $(name).find("input").val();
    if (nameValue == "" || !nameRegex.test(nameValue)) {
        $(name).addClass("has-error");
        $(name).find("input").focus();
        return false;
    } else {
        $(name).removeClass("has-error");
    }

    //time validator
    var stopTimeValue = $(timeValue).find("input").val();
    if (stopTimeValue == "" || !timeRegex.test(stopTimeValue)) {
        $(timeValue).addClass("has-error");
        $(timeValue).find("input").focus();
        return false;
    } else {
        $(timeValue).removeClass("has-error");
    }
}

function arcValidator(form) {

    var time = $(form).find("#travel_time");
    var stationFrom = $(form).find("#stationFrom");
    var stationTo = $(form).find("#stationTo");

    var stationFromValue = $(form).find("#stationFrom").find("select option:selected").text();
    var stationToValue = $(form).find("#stationTo").find("select option:selected").text()

    if (stationFromValue === stationToValue) {
        $(stationFrom).addClass("has-error");
        $(stationTo).addClass("has-error");
        return false;
    } else {
        $(stationFrom).removeClass("has-error");
        $(stationTo).removeClass("has-error");
    }

    //time validator
    var timeValue = $(time).find("input").val();
    if (timeValue == "" || !timeRegex.test(timeValue)) {
        $(time).addClass("has-error");
        $(time).find("input").focus();
        return false;
    } else {
        $(time).removeClass("has-error");
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

function routeValidator(form) {
    var routing_number = $(form).find("#routing_number");
    var price = $(form).find("#price");
    var depot_stop_time = $(form).find("#depot_stop_time");
    var first_bus_time = $(form).find("#first_bus_time");
    var last_bus_time = $(form).find("#last_bus_time");

    //routing_number validator
    var routingNumberValue = $(routing_number).find("input").val();
    if (routingNumberValue == "") {
        $(routing_number).addClass("has-error");
        $(routing_number).find("input").focus();
        return false;
    } else {
        $(routing_number).removeClass("has-error");
    }

    //price validation
    var priceValue = $(price).find("input").val();
    if (priceValue == "" || !priceRegex.test(priceValue)) {
        $(price).addClass("has-error");
        $(price).find("input").focus();
        return false;
    } else {
        $(price).removeClass("has-error");
    }

    //time validation
    var depotStopTimeValue = $(depot_stop_time).find("input").val();
    if (depotStopTimeValue == "" || !timeRegex.test(depotStopTimeValue)) {
        $(depot_stop_time).addClass("has-error");
        $(depot_stop_time).find("input").focus();
        return false;
    } else {
        $(depot_stop_time).removeClass("has-error");
    }

    //time validation
    var firstBusTimeValue = $(first_bus_time).find("input").val();
    if (firstBusTimeValue == "" || !timeRegex.test(firstBusTimeValue)) {
        $(first_bus_time).addClass("has-error");
        $(first_bus_time).find("input").focus();
        return false;
    } else {
        $(first_bus_time).removeClass("has-error");
    }

    //time validation
    var lastBusTimeValue = $(last_bus_time).find("input").val();
    if (lastBusTimeValue == "" || !timeRegex.test(lastBusTimeValue)) {
        $(last_bus_time).addClass("has-error");
        $(last_bus_time).find("input").focus();
        return false;
    } else {
        $(last_bus_time).removeClass("has-error");
    }

    //stations validator
    var stationError = $(form).find("#arc_list_alert");
    var n = 0;
    $(form).find("select").each(function (i, elem) {
        if ($(elem).val() != 0) {
            n++;
        }
    });
    if (n < 2) {
        $(stationError).show();
        return false;
    } else {
        $(stationError).hide();
    }

}

function hideOther() {
    var selects = $("#selects").find("select");
    var flag = false;
    var temp = 0;
    for (var iter = 1; iter < selects.length + 1; iter++) {
        var select = selects[iter - 1];
        var option = $("#route" + iter + " :selected").text()
        if (option != "none") {
            flag = true;
            temp = iter;
        }
    }

    if (flag) {
        for (var iter = 1; iter < selects.length + 1; iter++) {
            if (temp != iter) {
                $("#route" + iter).prop("disabled", true);
            }
        }
    } else {
        for (var iter = 1; iter < selects.length + 1; iter++) {
            $("#route" + iter).prop("disabled", false);
        }
    }
}

function toHHMMSS(time) {
    return moment().startOf('day')
        .seconds(time)
        .format('H:mm:ss')
}

$(document).ready(function () {

    $("#arcList").css({"height": "400px"});

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
                $("#stateField").html("Time: " + toHHMMSS(result[0].travelTime + result[0].startTime));
                result.forEach(function (item) {
                    var tr = $("<tr></tr>");
                    var tdSpan = $("<td></td>");
                    var ulEl = $("<ul></ul>");

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
                        "<td>" + item.passengerList.length + "</td>",
                        tdSpan.html(span)
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
            var option = $("#route" + iter + " :selected");
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
                    success: function (e) {
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
                            $("#route" + iter).prop("disabled", false);
                        }
                    },
                    error: function (e) {
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

function hideArcs(name) {
    $(name).find("select").each(function (i, element) {
        if (i != 0) {
            console.log(i);
            $(element).parent().parent().hide();
        }
    })
}

function showSelect(name) {
    var flag = false;
    var hide = false;
    $(name).find("select").each(function (i, element) {
        if (flag) {
            $(element).parent().parent().show();
            flag = false;
        }
        if (hide) {
            $(element).parent().parent().hide();
            $(element).find("option[value=0]").attr('selected', 'selected');
            hide = false;
        }

        var option = $(element).val();
        if (option != 0) {
            flag = true;
        } else {
            hide = true;
        }
    });
}


