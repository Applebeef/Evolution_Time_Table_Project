$(function () {
    var acc = document.getElementsByClassName("accordion");
    var i;

    for (i = 0; i < acc.length; i++) {
        acc[i].addEventListener("click", function () {
            this.classList.toggle("active");
            var panel = this.nextElementSibling;
            if (panel.style.display === "block") {
                panel.style.display = "none";
            } else {
                panel.style.display = "block";
            }
        });
    }
})

function updateData(timetable) {

    let teachers = timetable.teachers.teacherList
    let teacherList = document.getElementById("teachers").ap
    for (let i = 0; i < teachers.length; i++) {
        document.createElement("dl");
        $("#teachers").append()
    }
}

$(function () {
    var url = new URL(window.location.href);
    var index = url.searchParams.get("index");
    $.ajax({
        type: "GET",
        data: index,
        url: "getTableJSON",
        success: function (timetable) {
            updateData(timetable)
        }
    })
})


