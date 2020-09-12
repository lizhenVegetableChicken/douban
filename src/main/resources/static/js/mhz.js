let artists = document.querySelectorAll(".artistData");
let moreBtn = document.querySelector(".mhz-part .main .more");
let show = -1;
let artistAvatars = document.querySelectorAll(".artistData .cover .cb");

hide();

moreBtn.addEventListener("click", function() {
    show *= -1;
    if (show === 1) {
        showMore();
    } else {
        hide();
    }
});

function showMore() {
    for (let i=0; i<artists.length; i++) {
        artists[i].style.display = "block";
        moreBtn.innerHTML = `点击收回`;
    }
}
function hide() {
    for (let i=10; i<artists.length; i++) {
        artists[i].style.display = "none";
        moreBtn.innerHTML = `发现更多兆赫`;
    }
}
let chosen = new Array(artistAvatars.length);
for (let i=0; i<artistAvatars.length; i++) {
    chosen[i] = -1;
    artistAvatars[i].addEventListener("click", function () {
        recover();
        if (chosen[i] === -1) {
            chosen[i] = 1;
            this.style.display = "block";
            this.querySelector(".pic").style.background = `url("../images/Union2.png") no-repeat center / contain`;
        } else {
            recover();
            chosen[i] = -1;
        }

    });
}
function recover() {
    for (let i=0; i<artistAvatars.length; i++) {
        artistAvatars[i].style.display = "none";
        artistAvatars[i].querySelector(".pic").style.background = `url("../images/Polygon2.png") no-repeat center / contain`;
    }
}