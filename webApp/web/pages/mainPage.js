const TABLE_UPDATE_SERVLET = "/tableListUpdate"
const refreshRate = 2000

function refreshTableList(tables) {
    $("#table").empty();

    $.each(tables || []), function (timetable) {
        console.log(timetable);
    }
}

function ajaxTableUpdate() {
    $.ajax({
        url: TABLE_UPDATE_SERVLET,
        dataType: 'json',
        success: function (tables) {
            refreshTableList(JSON.parse(tables));
        }
    });
}

$(function () {
    setInterval(ajaxTableUpdate, refreshRate);
})