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

function printTeaches(teaches) {
    let teachesDOM = document.createElement("dd")
    teachesDOM.append("Teaching subjects: ")
    for (let i = 0; i < teaches.length; i++) {
        teachesDOM.append(teaches[i].subjectId)
        if (i < teaches.length - 1)
            teachesDOM.append(", ")
    }
    return teachesDOM
}

function printTeachers(teacherList) {
    let descriptionList = document.createElement("dl");
    $("#teachers").append(descriptionList)
    for (let i = 0; i < teacherList.length; i++) {
        let teacherName = document.createElement("dt")
        let teacherInfo = printTeaches(teacherList[i].teaching.teachesList)
        teacherName.innerText = teacherList[i].id + " - " + teacherList[i].name
        descriptionList.append(teacherName)
        descriptionList.append(teacherInfo)
    }
}

function printRequirements(requirementsList) {
    let requirementsDOM = document.createElement("dd")
    requirementsDOM.innerText = "Learning: "
    for (let i = 0; i < requirementsList.length; i++) {
        requirementsDOM.append("Subject " + requirementsList[i].subjectId + " for " + requirementsList[i].hours + " hours")
        if (i < requirementsList.length - 1)
            requirementsDOM.append(", ")
        else
            requirementsDOM.append(".")
    }
    return requirementsDOM
}

function printClasses(schoolClassList) {
    let descriptionList = document.createElement("dl")
    $("#schoolClasses").append(descriptionList)
    for (let i = 0; i < schoolClassList.length; i++) {
        let schoolClassName = document.createElement("dt")
        schoolClassName.innerText = schoolClassList[i].id + " - " + schoolClassList[i].name
        let schoolClassInfo = printRequirements(schoolClassList[i].requirements.studyList)
        descriptionList.append(schoolClassName)
        descriptionList.append(schoolClassInfo)
    }
}

function printSubjects(subjectList) {
    let descriptionList = document.createElement("dl");
    $("#subjects").append(descriptionList)
    for (let i = 0; i < subjectList.length; i++) {
        let subjectName = document.createElement("dt")
        subjectName.innerText = subjectList[i].id + " - " + subjectList[i].name
        descriptionList.append(subjectName)
    }
}

function printWeek(timetable) {
    let days = timetable.days.value
    let hours = timetable.hours.value
    $(".week").append("Days: " + days, " Hours: " + hours)
}

function printRules(rules) {
    $(".weight").append("Hard rules weight: " + rules.hardRulesWeight)
    let descriptionList = document.createElement("dl")
    $("#rules").append(descriptionList)
    for (let i = 0; i < rules.ruleList.length; i++) {
        let ruleName = document.createElement("dt")
        ruleName.innerText = rules.ruleList[i].rule.charAt(0) + rules.ruleList[i].rule.slice(1).toLowerCase()
        descriptionList.append(ruleName)
        let ruleConfig = document.createElement("dd")
        ruleConfig.innerText = "Type: " + rules.ruleList[i].type.charAt(0) + rules.ruleList[i].type.slice(1).toLowerCase()
        if (rules.ruleList[i].configuration !== 0) {
            ruleConfig.append(", Total hours: " + rules.ruleList[i].configuration)
        }
        descriptionList.append(ruleConfig)
    }
}

function updateData(timetable) {
    let pTimeTable = JSON.parse(timetable)
    console.log(pTimeTable)
    printWeek(pTimeTable)
    printTeachers(pTimeTable.teachers.teacherList)
    printClasses(pTimeTable.schoolClasses.schoolClassList)
    printSubjects(pTimeTable.subjects.subjectList)
    printRules(pTimeTable.rules);

}

$(function () {
    var url = new URL(window.location.href);
    var index = url.searchParams.get("index");
    $.ajax({
        type: "GET",
        data: {index: index},
        url: "getTableJSON",
        success: function (timetable) {
            updateData(timetable)
        }
    })
})

$(".topPercent").on("change", function () {
    let topPercent = $(this)[0]
    if (parseInt(topPercent.value) > 100) {
        topPercent.value = 100;
    }
    if (parseInt(topPercent.value) < 1) {
        topPercent.value = 1
    }
})

$(".populationSize").on("change", function () {
    let popSize = $(this)[0]
    if (parseInt(popSize.value) < 100) {
        popSize.value = 100
    }
    $(".elitism").trigger("change")
})

$(".elitism").on("change", function () {
    let elitisms = $(".elitism")
    let popSize = parseInt($(".populationSize")[0].value)
    for (let i = 0; i < elitisms.length; i++) {
        if (parseInt(elitisms[i].value) > popSize)
            elitisms[i].value = popSize

        if (parseInt(elitisms[i].value) < 0)
            elitisms[i].value = 0
    }
})


$(".selectionIsActive").on('change', function () {//TODO make work
    let curr = $(this);
    let checkboxes = $(".selectionIsActive")
    console.log(curr)
    console.log(checkboxes[1])
    if (curr.prop("checked") === true) {
        for (let i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i] !== curr[0]) {
                checkboxes[i].prop("checked", false)
            }
        }
    } else {
        //curr.prop("checked", true)
    }
});

function createTruncationObject(form) {
    let Truncation = class {
        constructor(topPercent, isActive, elitism) {
            this.topPercent = topPercent
            this.elitism = elitism
            this.isActive = isActive
        }
    };
    let elements = form.elements
    let topPercent
    let isActive
    let elitism
    for (let i = 0; i < elements.length; i++) {
        switch (elements[i].name) {
            case "topPercent":
                topPercent = parseInt(elements[i].value)
                break
            case "isActive":
                isActive = elements[i].checked
                break
            case "elitism":
                elitism = parseInt(elements[i].value)
                break
        }
    }
    return new Truncation(topPercent, isActive, elitism)
}

$(".PTE").on("change", function () {
    let pte = $(this)[0]
    if (parseInt(pte.value) > 1) {
        pte.value = 1
    }
    if (parseInt(pte.value) < 0) {
        pte.value = 0
    }
})

function createRouletteWheelObject(RouletteWheelElement) {
    let RouletteWheel = class {
        constructor(isActive, elitism) {
            this.elitism = elitism
            this.isActive = isActive
        }
    };
    let elements = RouletteWheelElement.elements
    let isActive
    let elitism
    for (let i = 0; i < elements.length; i++) {
        switch (elements[i].name) {
            case "isActive":
                isActive = elements[i].checked
                break
            case "elitism":
                elitism = parseInt(elements[i].value)
                break
        }
    }
    return new RouletteWheel(isActive, elitism)
}

function createTournamentObject(TournamentElement) {
    let Tournament = class {
        constructor(pte, isActive, elitism) {
            this.pte = pte
            this.elitism = elitism
            this.isActive = isActive
        }
    };
    let elements = TournamentElement.elements
    let pte
    let isActive
    let elitism
    for (let i = 0; i < elements.length; i++) {
        switch (elements[i].name) {
            case "PTE":
                pte = parseFloat(elements[i].value)
                break
            case "isActive":
                isActive = elements[i].checked
                break
            case "elitism":
                elitism = parseInt(elements[i].value)
                break
        }
    }
    return new Tournament(pte, isActive, elitism)
}

function createSelectionObject() {
    class Selections {
        constructor(truncation, rouletteWheel, tournament) {
            this.truncation = truncation
            this.rouletteWheel = rouletteWheel
            this.tournament = tournament
        }
    }

    let truncation = createTruncationObject($(".Truncation")[0])
    let rouletteWheel = createRouletteWheelObject($(".RouletteWheel")[0])
    let tournament = createTournamentObject($(".Tournament")[0])
    return new Selections(truncation, rouletteWheel, tournament)
}

function createCrossoverObject() {
    return undefined;
}

$(function () {
    $("#startEngine").on("click", function () {
        let populationSize = $(".populationSize")[0].value
        let selections = createSelectionObject()
        let crossovers = createCrossoverObject()

        $.ajax({
            type: "GET",
            data: {selections: JSON.stringify(selections), popSize: JSON.stringify(populationSize)},
            url: "startEngine",
            success: function () {
                console.log("success")
            },
            error: function () {
                console.log("error")
            }
        })
    })
})

