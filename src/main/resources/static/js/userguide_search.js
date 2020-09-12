let main = document.querySelector(".search-main");
let searchInput = main.querySelector(".form .search .search-input");
let results = main.querySelector(".results");
let clickmore = main.querySelector(".clickmore");
let show = -1;

searchInput.addEventListener("input", function() {
    console.log(searchInput.value);
    if (searchInput.value !== "") {
        fetch(
            `/userguide/searchContent?keyword=${searchInput.value}`
        ).then(function (response) {
            return response.json();
        }).then(function (map) {
            results.innerHTML = ``;
            let i;
            for (i = 0; ; i++) {
                let key = "s" + i;
                let singer = map[key];
                if (typeof singer == "undefined") {
                    if (i === 0){
                        results.innerHTML = `<span>未找到</span>`;
                        clickmore.style.display = "none";
                    } else if (i>6) {
                        clickmore.style.display = "block";
                    }
                    return;
                }
                let singerDom = createDiv(singer);
                if (i>5) {
                    singerDom.style.display = "none";
                }
                results.appendChild(singerDom);
                //results.appendChild((i>4)?createDiv(singer).style.display="none":createDiv(singer))
            }
        });
    } else {
        results.innerHTML = ``;
        clickmore.style.display = "none";
    }
});

function createDiv(singer) {
    let li = document.createElement("div");
    li.setAttribute("class", "singer");
    li.innerHTML = `
          <img referrer="no-referrer|origin|unsafe-url" src="${singer['avatar']}" alt="#">
          <div class="name"><a href="/artist?artistId=${singer['id']}">${singer['name']}</a></div>
    `;
    return li;
}

clickmore.addEventListener("click", function () {
    show *= -1;
    let singers = results.querySelectorAll(".singer");
    if (show === 1) {
        for (let singer of singers) {
            singer.style.display = "block";
        }
        this.innerText = "点击收回";
    } else {
        for (let i=6; i<singers.length; i++) {
            singers[i].style.display = "none";
        }
        this.innerText = "查看所有";
    }
});