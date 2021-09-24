let index
let days
let hours
let subjects
let teachersAmount
let teachers
let schoolClassesAmount
let schoolClasses
const refreshRate = 2000

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

function disableOrEnableSettings(isDisabled) {
    $(".Truncation :input").prop("disabled", isDisabled)
    $(".Tournament :input").prop("disabled", isDisabled)
    $(".RouletteWheel :input").prop("disabled", isDisabled)
    $("#initPopulation :input").prop("disabled", isDisabled)
    $("#frequency :input").prop("disabled", isDisabled)
    $(".dayTimeOriented :input").prop("disabled", isDisabled)
    $(".aspectOriented :input").prop("disabled", isDisabled)
    $(".flipping :input").prop("disabled", isDisabled)
    $(".sizer :input").prop("disabled", isDisabled)
    $(".fitness :input").prop("disabled", isDisabled)
    $(".generations :input").prop("disabled", isDisabled)
    $(".time :input").prop("disabled", isDisabled)
}

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
    teachersAmount = teacherList.length
    teachers = teacherList
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
    schoolClassesAmount = schoolClassList.length
    schoolClasses = schoolClassList
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
    subjects = subjectList.length
    for (let i = 0; i < subjectList.length; i++) {
        let subjectName = document.createElement("dt")
        subjectName.innerText = subjectList[i].id + " - " + subjectList[i].name
        descriptionList.append(subjectName)
    }
}

function printWeek(timetable) {
    days = timetable.days.value
    hours = timetable.hours.value
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
        if (rules.ruleList[i].configuration !== undefined) {
            ruleConfig.append(", Total hours: " + rules.ruleList[i].configuration)
        }
        descriptionList.append(ruleConfig)
    }
}

function sendStartRequest() {
    let populationSize = parseInt($("#populationSizeInput")[0].value)
    let frequency = parseInt($("#frequencyInput")[0].value)
    let selections = createSelectionObject()
    let crossovers = createCrossoverObject()
    let mutations = createMutationObject()
    let endingConditions = createEndingConditions()

    $.ajax({
        type: "GET",
        data: {
            selections: JSON.stringify(selections),
            popSize: populationSize,
            frequency: frequency,
            crossovers: JSON.stringify(crossovers),
            mutations: JSON.stringify(mutations),
            endingConditions: JSON.stringify(endingConditions),
            index: index
        },
        url: "startEngine",
        success: function () {
            console.log("start engine - success")
            engineStarted(false)

        },
        error: function () {
            console.log("start engine - error")
        }
    })
}

function createStartButton() {
    let engineControls = $(".engineControls")
    engineControls.empty()
    let startButton = document.createElement("button")
    startButton.id = "startEngine"
    startButton.innerText = "Start engine"
    startButton.onclick = function () {
        sendStartRequest()
    }
    engineControls.append(startButton)
}

function updateTimeTableData(timetable) {
    printWeek(timetable)
    printTeachers(timetable.teachers.teacherList)
    printClasses(timetable.schoolClasses.schoolClassList)
    printSubjects(timetable.subjects.subjectList)
    printRules(timetable.rules);
}

function printCrossovers(crossovers) {
    let aspectOrientedElements = $(".aspectOriented")[0].elements
    let dayTimeElements = $(".dayTimeOriented")[0].elements
    for (let i = 0; i < aspectOrientedElements.length; i++) {
        switch (aspectOrientedElements[i].name) {
            case "isActive":
                aspectOrientedElements[i].checked = crossovers.aspectOriented.isActive
                break
            case "cuttingPoints":
                aspectOrientedElements[i].value = crossovers.aspectOriented.cuttingPoints
                break
            case "aspect":
                aspectOrientedElements[i].value = crossovers.aspectOriented.aspect
                break
        }
    }
    for (let i = 0; i < dayTimeElements.length; i++) {
        switch (dayTimeElements[i].name) {
            case "isActive":
                dayTimeElements[i].checked = crossovers.dayTimeOriented.isActive
                break
            case "cuttingPoints":
                dayTimeElements[i].value = crossovers.dayTimeOriented.cuttingPoints
        }
    }

}

function printMutations(mutations) {
    let flippingElements = $(".flipping")[0].elements
    let sizerElements = $(".sizer")[0].elements
    for (let i = 0; i < flippingElements.length; i++) {
        switch (flippingElements[i].name) {
            case "probability":
                flippingElements[i].value = mutations.flipping.probability
                break
            case "tupples":
                flippingElements[i].value = mutations.flipping.tupples
                break
            case "component":
                flippingElements[i].value = mutations.flipping.component
                break
        }
    }
    for (let i = 0; i < sizerElements.length; i++) {
        switch (sizerElements[i].name) {
            case "probability":
                sizerElements[i].value = mutations.sizer.probability
                break
            case "tupples":
                sizerElements[i].value = mutations.sizer.tupples
                break
        }
    }
}

function printSelections(selections) {
    let truncationElements = $(".Truncation")[0].elements
    let rouletteElements = $(".RouletteWheel")[0].elements
    let tournamentElements = $(".Tournament")[0].elements
    for (let i = 0; i < truncationElements.length; i++) {
        switch (truncationElements[i].name) {
            case "isActive":
                truncationElements[i].checked = selections.truncation.isActive
                break
            case "topPercent":
                truncationElements[i].value = selections.truncation.topPercent
                break
            case "elitism":
                truncationElements[i].value = selections.truncation.elitism
                break
        }
    }
    for (let i = 0; i < rouletteElements.length; i++) {
        switch (rouletteElements[i].name) {
            case "isActive":
                rouletteElements[i].checked = selections.rouletteWheel.isActive
                break
            case "elitism":
                rouletteElements[i].value = selections.rouletteWheel.elitism
                break
        }
    }
    for (let i = 0; i < tournamentElements.length; i++) {
        switch (tournamentElements[i].name) {
            case "isActive":
                tournamentElements[i].checked = selections.tournament.isActive
                break
            case "PTE":
                tournamentElements[i].value = selections.tournament.pte
                break
            case "elitism":
                tournamentElements[i].value = selections.tournament.elitism
                break
        }
    }
}

function printEndingConditions(endingConditions) {
    $("#generationsEC").prop("value", endingConditions.generations)
    $("#generations").prop("max", endingConditions.generations)
    $("#fitnessEC").prop("value", endingConditions.fitness)
    $("#fitness").prop("max", endingConditions.fitness)
    $("#timeEC").prop("value", endingConditions.time)
    $("#time").prop("max", endingConditions.time)
}

function updateEngineData(crossovers, mutations, selections, endingConditions, popSize, frequency) {
    printCrossovers(crossovers)
    printMutations(mutations)
    printSelections(selections)
    printEndingConditions(endingConditions)
    $("#populationSizeInput").prop("value", popSize)
    $("#frequencyInput").prop("value", frequency)
}

function updateLoadData(data) {
    let pData = JSON.parse(data)
    console.log(pData)
    updateTimeTableData(pData.timeTable)
    if (pData.crossoversJSON !== undefined && pData.mutationsJSON !== undefined && pData.selectionsJSON !== undefined && pData.endingConditionsJSON !== undefined) {
        updateEngineData(pData.crossoversJSON, pData.mutationsJSON, pData.selectionsJSON, pData.endingConditionsJSON, pData.popSize, pData.frequency)
    }
    if (pData.isAlive === undefined || pData.isAlive === false) {
        createStartButton()
    } else
        engineStarted(pData.isPaused)
    $("#classOrTeacherDisplay").trigger("change")
    getGenAndBestFitness()
    setInterval(getGenAndBestFitness, refreshRate)
}

function getResult() {
    let value = $("#chooseClassOrTeacher").prop("value")
    $.ajax({
        type: 'POST',
        url: "result",
        data: {index: index, id: value},
        error: function () {
            console.error("error")
        },
        success: function (result) {
            console.log(result)
            return false
        }
    })
    return false
}

function updateGenAndFitness(genAndFitness) {
    let currentGen = $("#currentGeneration")
    let bestFitness = $("#bestFitness");
    if (genAndFitness !== null) {
        currentGen.empty()
        currentGen.append("Current generation: " + genAndFitness.generation)
        bestFitness.empty()
        bestFitness.append("Best fitness: " + genAndFitness.fitness.toFixed(2))
        if (genAndFitness.isAlive) {
            engineStarted()
        } else {
            createStartButton()
        }
    }
}

function getGenAndBestFitness() {
    $.ajax({
        type: "GET",
        data: {index: index},
        url: "genAndFitness",
        success: function (genAndFitness) {
            updateGenAndFitness(genAndFitness)
        }
    })
}

$(function () {
    var url = new URL(window.location.href);
    index = url.searchParams.get("index");
    $.ajax({
        type: "GET",
        data: {index: index},
        url: "getTableJSON",
        success: function (data) {
            updateLoadData(data)
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

$("#populationSizeInput").on("change", function () {
    let popSize = $(this)[0]
    if (parseInt(popSize.value) < 100) {
        popSize.value = 100
    }
    $(".elitism").trigger("change")
    console.log("stuff")
    $(".tupples").trigger("change")
})

$(".elitism").on("change", function () {
    let elitisms = $(".elitism")
    let popSize = parseInt($("#populationSizeInput")[0].value)
    for (let i = 0; i < elitisms.length; i++) {
        if (parseInt(elitisms[i].value) > popSize)
            elitisms[i].value = popSize

        if (parseInt(elitisms[i].value) < 0)
            elitisms[i].value = 0
    }
})

$('.selectionIsActive').on('change', function () {
    let selections = $('.selectionIsActive')
    selections.not(this).prop('checked', false);

    let allFalse = true
    for (let i = 0; i < selections.not(this).length; i++) {
        if (selections.not(this).prop('checked') === true)
            allFalse = false
    }
    if (allFalse === true) {
        $(this).prop('checked', true)
    }
});

$('.crossoverIsActive').on('change', function () {
    let crossovers = $('.crossoverIsActive')
    crossovers.not(this).prop('checked', false);

    let allFalse = true
    for (let i = 0; i < crossovers.not(this).length; i++) {
        if (crossovers.not(this).prop('checked') === true)
            allFalse = false
    }
    if (allFalse === true) {
        $(this).prop('checked', true)
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

function createDayTimeOrientedObject(dayTimeOrientedElement) {
    class dayTimeOriented {
        constructor(isActive, cuttingPoints) {
            this.isActive = isActive
            this.cuttingPoints = cuttingPoints
        }
    }

    let elements = dayTimeOrientedElement.elements
    let cuttingPoints
    let isActive
    for (let i = 0; i < elements.length; i++) {
        switch (elements[i].name) {
            case "isActive":
                isActive = elements[i].checked
                break
            case "cuttingPoints":
                cuttingPoints = parseInt(elements[i].value)
                break
        }
    }
    return new dayTimeOriented(isActive, cuttingPoints)
}

function createAspectOrientedObject(aspectOrientedElement) {
    class aspectOriented {
        constructor(isActive, cuttingPoints, aspect) {
            this.isActive = isActive
            this.cuttingPoints = cuttingPoints
            this.aspect = aspect
        }
    }

    let elements = aspectOrientedElement.elements
    let isActive
    let cuttingPoints
    let aspect
    for (let i = 0; i < elements.length; i++) {
        switch (elements[i].name) {
            case "isActive":
                isActive = elements[i].checked
                break
            case "cuttingPoints":
                cuttingPoints = parseInt(elements[i].value)
                break
            case "aspect":
                aspect = elements[i].value
        }
    }
    return new aspectOriented(isActive, cuttingPoints, aspect)
}

function createCrossoverObject() {
    class Crossovers {
        constructor(dayTimeOriented, aspectOriented) {
            this.dayTimeOriented = dayTimeOriented
            this.aspectOriented = aspectOriented
        }
    }

    let dayTimeOriented = createDayTimeOrientedObject($(".dayTimeOriented")[0])
    let aspectOriented = createAspectOrientedObject($(".aspectOriented")[0])
    return new Crossovers(dayTimeOriented, aspectOriented)
}

function createFlippingObject(flippingElement) {
    class Flipping {
        constructor(probability, tupples, component) {
            this.probability = probability
            this.tupples = tupples
            this.component = component
        }
    }

    let elements = flippingElement.elements
    let probability
    let tupples
    let component
    for (let i = 0; i < elements.length; i++) {
        switch (elements[i].name) {
            case "probability":
                probability = parseFloat(elements[i].value)
                break
            case "tupples":
                tupples = parseInt(elements[i].value)
                break
            case "component":
                component = elements[i].value
                break
        }
    }
    return new Flipping(probability, tupples, component)
}

function createSizerObjects(sizerElement) {
    class Sizer {
        constructor(probability, tupples) {
            this.probability = probability
            this.tupples = tupples
        }
    }

    let elements = sizerElement.elements
    let probability
    let tupples
    for (let i = 0; i < elements.length; i++) {
        switch (elements[i].name) {
            case "probability":
                probability = parseFloat(elements[i].value)
                break
            case "tupples":
                tupples = parseInt(elements[i].value)
                break
        }
    }
    return new Sizer(probability, tupples)
}

function createMutationObject() {
    class Mutations {
        constructor(flipping, sizer) {
            this.flipping = flipping
            this.sizer = sizer
        }
    }

    let flipping = createFlippingObject($(".flipping")[0])
    let sizer = createSizerObjects($(".sizer")[0])
    return new Mutations(flipping, sizer)
}

function createEndingConditions() {
    class EndingConditions {
        constructor(generations, fitness, time) {
            this.generations = generations
            this.fitness = fitness
            this.time = time
        }
    }

    let generations
    let fitness
    let time
    generations = parseInt($(".generations")[0].elements[0].value)
    fitness = parseInt($(".fitness")[0].elements[0].value)
    time = parseInt($(".time")[0].elements[0].value)
    return new EndingConditions(generations, fitness, time)
}

function stopEngine() {
    $(".engineControls").empty()
    $.ajax({
        type: "GET",
        data: {index: index},
        url: "stopEngine",
        success: function () {
            console.log("stop engine - success")
        },
        error: function () {
            console.log("stop engine - error")
        }
    })
    createStartButton()
    disableOrEnableSettings(false)
}

function engineStarted(isPaused) {
    disableOrEnableSettings(true)
    let engineControls = $(".engineControls")
    engineControls.empty()
    let pauseButton = document.createElement("button")
    let stopButton = document.createElement("button")
    let pauseText

    if (isPaused)
        pauseText = "Resume"
    else
        pauseText = "Pause"

    pauseButton.id = "pauseButton"
    pauseButton.innerText = pauseText
    pauseButton.onclick = function () {
        $.ajax({
            type: "GET",
            data: {index: index},
            url: "pause",
            success: function () {
                console.log(pauseButton.innerText + " success")
            },
            error: function () {
                console.log(pauseButton.innerText + " error")
            }
        })

        if (pauseButton.innerText === "Resume") {
            pauseButton.innerText = "Pause"
            disableOrEnableSettings(true)
        } else {
            pauseButton.innerText = "Resume"
            disableOrEnableSettings(false)
        }
    }
    stopButton.id = "stopButton"
    stopButton.innerText = "Stop"
    stopButton.onclick = function () {
        stopEngine()
    }
    engineControls.append(stopButton)
    engineControls.append(pauseButton)
}

$("#frequencyInput").on("change", function () {
    let freq = $(this)[0]
    if (parseInt(freq.value) < 1) {
        freq.value = 1
    }
})

$(".cuttingPoints").on("change", function () {
    let cuttingPoint = $(this)[0]
    if (parseInt(cuttingPoint.value) < 1)
        cuttingPoint.value = 1
    if (parseInt(cuttingPoint.value) > days * hours * schoolClassesAmount * teachersAmount * subjects) {
        cuttingPoint.value = days * hours * schoolClassesAmount * teachersAmount * subjects
    }
})

$(".tupples").on("change", function () {
    let tupple = $(".tupples")
    let initialPop = parseInt($("#populationSizeInput")[0].value)
    for (let i = 0; i < tupple.length; i++) {
        if (Math.abs(parseInt(tupple[i].value)) > initialPop) {
            tupple[i].value = initialPop * Math.sign(tupple[i].value)
        }
    }
})

$("#maxTupples").on("change", function () {
    let tupple = $(this)[0]
    if (parseInt(tupple.value) < 1)
        tupple.value = 1
})

$(".probability").on("change", function () {
    let prob = $(this)[0]
    if (parseInt(prob.value) < 0)
        prob.value = 0
    if (parseInt(prob.value) > 1)
        prob.value = 1
})

$(".endingCondition").on("change", function () {
    let ec = $(this)[0]
    if (parseInt(ec.value) < 0)
        ec.value = 0

    if (ec.id === "fitness") {
        if (parseInt(ec.value) > 100)
            ec.value = 100
    }

    let allEC = $(".endingCondition")
    console.log(allEC)
    let allZero = true
    for (let i = 0; i < allEC.length; i++) {
        allZero = (parseInt(allEC[i].value) === 0) && allZero
    }
    $("#startEngine").prop("disabled", allZero)
})

function appendListToSelect(list) {
    let ct = $("#chooseClassOrTeacher")
    ct.empty()
    for (let i = 0; i < list.length; i++) {
        let option = document.createElement("option")
        option.value = list[i].id
        option.innerText = list[i].id + " - " + list[i].name
        ct.append(option)
    }
}

$("#classOrTeacherDisplay").on("change", function () {
    let ct = $("#chooseClassOrTeacher")
    switch ($(this)[0].value) {
        case "Teacher":
            appendListToSelect(teachers)
            break
        case "Class":
            appendListToSelect(schoolClasses)
            break
    }
})

$("#bestSolutionDisplayButton").on("click", function () {

    getResult()
    return false
})