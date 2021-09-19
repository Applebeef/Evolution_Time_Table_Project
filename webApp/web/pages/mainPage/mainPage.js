const TABLE_UPDATE_SERVLET = "tableListUpdate"
const refreshRate = 2000
let totalTables

function refreshTableList(tables) {
    let table = $("#table")
    table.empty()
    table.append('<th>Uploader </th> <th>Days </th> <th>Hours </th> <th>Teachers </th> <th>Classes </th> <th>Subjects </th>' +
        '<th>Hard Rules </th><th>Soft Rules </th>')


    for (let i = 0; i < tables.length; i++) {
        let uploader = tables[i].uploader
        let days = tables[i].days.value
        let hours = tables[i].hours.value
        let teachers = tables[i].teachers.teacherList.length
        let subjects = tables[i].subjects.subjectList.length
        let classes = tables[i].schoolClasses.schoolClassList.length
        let softCount = 0
        let hardCount = 0
        for (let j = 0; j < tables[i].rules.ruleList.length; j++) {
            if (tables[i].rules.ruleList[j].type === "HARD") {
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
            console.log(tables)
            refreshTableList(tables);
        },
        error: function (xhr, status, error) {
            console.log(error)
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
            return false
        }
    })
    return false
})