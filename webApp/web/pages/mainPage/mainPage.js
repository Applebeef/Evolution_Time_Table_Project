const TABLE_UPDATE_SERVLET = "tableListUpdate"
const refreshRate = 2000
let totalTables

function refreshTableList(tables) {
    let table = $("#table")
    table.empty()
    table.append('<th>Uploader </th> <th>Days </th> <th>Hours </th> <th>Teachers </th> <th>Classes </th> <th>Subjects </th>' +
        '<th>Hard Rules </th><th>Soft Rules </th><th>Users</th><th>Best Fitness</th>')


    for (let i = 0; i < tables.length; i++) {
        let timetable = tables[i].v1
        let uploader = timetable.uploader
        let days = timetable.days.value
        let hours = timetable.hours.value
        let teachers = timetable.teachers.teacherList.length
        let subjects = timetable.subjects.subjectList.length
        let classes = timetable.schoolClasses.schoolClassList.length
        let softCount = 0
        let hardCount = 0
        let totalUsers = tables[i].v2
        let bestFitness = tables[i].v3.toFixed(2)
        for (let j = 0; j < timetable.rules.ruleList.length; j++) {
            if (timetable.rules.ruleList[j].type === "HARD") {
                hardCount++
            } else {
                softCount++
            }
        }

        document.getElementById("table").insertRow(-1).innerHTML = '<td>' + uploader + '</td>'
            + '<td>' + days + '</td>'
            + '<td>' + hours + '</td>'
            + '<td>' + teachers + '</td>'
            + '<td>' + subjects + '</td>'
            + '<td>' + classes + '</td>'
            + '<td>' + hardCount + '</td>'
            + '<td>' + softCount + '</td>'
            + '<td>' + totalUsers + '</td>'
            + '<td>' + bestFitness + '</td>'
            + '<td>' +
            '<form method="GET" action="enterTable">' +
            '<input type="hidden" name="index" value=' + i + '>' +
            '<input type="submit" value="Choose"/></form>'
            + '</td >'

    }

}

function ajaxTableUpdate() {
    $.ajax({
        url: TABLE_UPDATE_SERVLET,
        dataType: 'json',
        success: function (tables) {
            totalTables = tables.length
            refreshTableList(tables);
        },
        error: function (xhr, status, error) {
            console.log(error)
            console.log(status)
            console.log(xhr)
        }
    });
}

$(function () {
    ajaxTableUpdate()

    setInterval(ajaxTableUpdate, refreshRate)
})


$("#submitBtn").on("click", function () {

    const fd = new FormData();

    const files = $('#timeTableXML').prop('files');
    fd.append('file', files[0]);
    $.ajax({
        type: 'POST',
        processData: false,
        contentType: false,
        url: "uploadTimeTable",
        data: fd,
        timeout: 2000,
        error: function () {
            console.error("Failed to submit")
        },
        success: function (r) {
            alert(r)
            return false
        }
    })
    return false
})