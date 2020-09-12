let main = document.querySelector(".main");
let findBtn = main.querySelector(".login-form .btn");
let nameInput = main.querySelector(".login-form .input-content div .name");
let answerInput = main.querySelector(".login-form .input-content div .answer");
let question = main.querySelector(".login-form .input-content .question");
let result = main.querySelector(".pwd-result .result-main");

let answer = showQuestion();

findBtn.addEventListener("click", function() {
    console.log(nameInput.value);
    if (nameInput.value === null || nameInput.value === "" ||
        answerInput.value === null || answerInput.value === "") {
        result.innerHTML = showResult(2);
        return;
    }
    fetch(
        `/findPassword/result?userName=${nameInput.value}`
    ).then(function (response) {
        return response.json();
    }).then(function (map) {
        result.innerHTML = ``;
        if (map['result'] === "null") {
            result.innerHTML = showResult(1);
            return;
        }
        if (answerInput.value === answer) {
            let pwd = map['result'];
            result.innerHTML = showResult(4);
            result.querySelector(".pwd").innerHTML = pwd;
        } else {
            result.innerHTML = showResult(3);
        }
    });
});

function showQuestion() {
    question.innerHTML = `川建国同志在美国卧底做什么？`;
    return "美国总统";
}

function showResult(typeNum) {
    let inner;
    switch (typeNum) {
        case 1: inner = `
            <div class="content">用户名不存在</div>
            <div class="btn"><a href="/findPassword">返回</a></div>
        `; break;
        case 2: inner = `
            <div class="content">填空不完整</div>
            <div class="btn"><a href="/findPassword">返回</a></div>
        `; break;
        case 3: inner = `
            <div class="content">问题回答错误</div>
            <div class="btn"><a href="/findPassword">返回</a></div>
        `; break;
        case 4: inner = `
            <div class="content">你的密码是
                <p class="pwd"></p>
            </div>
            <div class="btn"><a href="/login">返回登录</a></div>
        `; break;
    }
    main.querySelector(".pwd-result").style.display = "block";
    return inner;
}
