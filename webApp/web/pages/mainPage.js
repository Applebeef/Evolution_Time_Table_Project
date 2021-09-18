const TABLE_UPDATE_SERVLET = "tableListUpdate"
const refreshRate = 2000

function refreshTableList(tables) {
    let table = $("#table")
    table.empty()
    table.append('<th>Uploader </th> <th>Days </th> <th>Hours </th> <th>Teachers </th> <th>Classes </th> <th>Subjects </th>')

    for (let tablesKey of tables) {
        let uploader = tablesKey.uploader
        let days = tablesKey.days.value
        let hours = tablesKey.hours.value
        let teachers = tablesKey.teachers.teacherList.length
        let subjects = tablesKey.subjects.subjectList.length
        let classes = tablesKey.schoolClasses.schoolClassList.length
        document.getElementById("table").insertRow(-1).innerHTML = '<td>' + uploader + '</td>'
            + '<td>' + days + '</td>'
            + '<td>' + hours + '</td>'
            + '<td>' + teachers + '</td>'
            + '<td>' + subjects + '</td>'
            + '<td>' + classes + '</td>'
    }

}

function ajaxTableUpdate() {
    $.ajax({
        url: TABLE_UPDATE_SERVLET,
        dataType: 'json',
        success: function (tables) {
            console.log(tables)
            refreshTableList(tables);
        }
    });
}

$(function () {
    setInterval(ajaxTableUpdate, refreshRate);
})


$("#submitBtn").on("click", function(event) {

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
        error: function() {
            console.error("Failed to submit")
        },
        success: function(r) {
            return false
        }
    })
    return false
})