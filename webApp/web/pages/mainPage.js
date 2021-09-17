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

$(function () { // onload...do
    //add a function to the submit event
    $("#upload").submit(function () {
        $.ajax({
            url: this.action,
            timeout: 2000,
            error: function () {
                console.error("Failed to submit");
            },
            success: function (r) {

            }
        });
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});