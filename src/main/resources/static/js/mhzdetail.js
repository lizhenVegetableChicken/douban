let likeBtn = document.querySelector(".mhz-main .subject .like-btn");
let likeNum = likeBtn.querySelector(".num");
let like = likeBtn.querySelector(".like");
let num = parseInt(likeNum.firstChild.nodeValue);
let chosen = -1;
let moreBtn = document.querySelector(".mhz-main .songs .more");
let songs = document.querySelectorAll(".mhz-main .songs .song");
let show = -1;

hideSongs();

likeBtn.addEventListener("click", function() {
    chosen *= -1;
    // console.log("click!");
    if (chosen === 1) {
        num ++;
        likeit();
    } else {
        num --;
        cancel();
    }
})

moreBtn.addEventListener("click", function () {
    show *= -1;
    if (show === 1) {
        showMoreSongs();
    } else {
        hideSongs();
    }
});

function likeit() {
    likeBtn.style.color="#FF4874";
    likeBtn.style.border="1px solid #FF4874";
    likeNum.innerHTML = num+'';
    like.innerHTML = `已喜欢`;
}

// 不喜欢
function cancel() {
    likeBtn.style.color="#C5C5C5";
    likeBtn.style.border="1px solid #C5C5C5";
    likeNum.innerHTML = num+'';
    like.innerHTML = `喜欢`;
}

function showMoreSongs() {
    for (let i=0; i<songs.length; i++) {
        songs[i].style.display = "flex";
    }
    moreBtn.innerHTML = `点击收回`;
}
function hideSongs() {
    for (let i=4; i<songs.length; i++) {
        songs[i].style.display = "none";
    }
    moreBtn.innerHTML = `查看全部精选歌曲`;
}