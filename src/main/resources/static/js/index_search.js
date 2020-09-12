let main = document.querySelector(".search-main");
let searchInput = main.querySelector(".form .search .search-input");
let results = main.querySelector(".results");
let searchGuide = main.querySelector(".guide");
let resultMap = null;

searchInput.addEventListener("input", function() {
    if (searchInput.value !== "") {
        fetch(
            `/index/searchContent?keyword=${searchInput.value}`
        ).then(function (response) {
            return response.json();
        }).then(function (map) {
            resultMap = map;
        });
        console.log(searchInput.value);
    } else {
        results.innerHTML = ``;
        resultMap = null;
    }
});

for (let i=0; i<searchGuide.querySelectorAll(".g").length;i++) {
    searchGuide.querySelectorAll(".g")[i].addEventListener("click", function () {
        clickSearch(i);
    });
}

function clickSearch(type) {
    console.log('click the '+ type);
    if (resultMap === null) return;
    results.innerHTML = ``;
    let aim;
    switch (type) {
        case 0: aim='singers';break;
        case 1: aim='songs';break;
        case 2: aim='mhzs';break;
        case 3: aim='collections';break;
    }
    let data = resultMap[aim];
    if (data.length === 0) {
        results.innerHTML = `<span>未找到</span>`;
        return;
    }
    for (let s of data) {
        results.appendChild(createSingle(s,type));
    }
}

function createSingle(single, type) {
    let li = document.createElement("div");
    li.setAttribute("class", "single");
    switch(type) {
        case 1:
        case 2:
            li.innerHTML = `
                  <img referrer="no-referrer|origin|unsafe-url" src="${single['cover']}" alt="#">
                  <div class="name">${single['name']}</div>
            `;break;
        case 0:
            li.innerHTML = `
                  <img referrer="no-referrer|origin|unsafe-url" src="${single['avatar']}" alt="#">
                  <div class="name"><a href="/artist?artistId=${single['id']}">${single['name']}</a></div>
            `;break;
        case 3:
            li.innerHTML = `
                  <img referrer="no-referrer|origin|unsafe-url" src="${single['cover']}" alt="#">
                  <div class="name"><a href="/collectiondetail?subjectId=${single['id']}">${single['name']}</a></div>
            `;break;
    }
    return li;
}