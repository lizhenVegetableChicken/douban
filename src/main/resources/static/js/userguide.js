// 获取歌手 和 头像栏单位
let singers = document.querySelectorAll(".userguide-main .singers .singer");
let avatars = document.querySelectorAll(".userguide-main .selected .p");
// 记录歌手是否被选,和被选中数目
let chooses = new Array(singers.length);
let chosenNum = 0;
// 记录头像栏位置和空位，每个位置存储该歌手的顺序编号
let positions = [-1,-1,-1,-1,-1];
let firstBlank = 0;

// 页面加载完成时，先初始化十个歌手
getTen();

// 换一批
document.querySelector(".userguide-main .options .change").addEventListener("click", function() {
    // 先全部清除
    for (let i=0; i<singers.length; i++) {
        singers[i].style.display="none";
    }
    // 换十个上去
    getTen();
    console.log("换一批!");
});

// 点击“喜欢”按钮的渲染。给每一喜欢按钮增加事件监听
for (let i=0; i<singers.length; i++) {
    // -1 为不喜欢， 1 为喜欢
    chooses[i] = -1;
    singers[i].querySelector(".like-btn").addEventListener("click", function() {
        // 点击时为喜欢，接下来就会进行取消喜欢的操作
        if (chooses[i] === 1 ) {
            cancel(singers[i]);
            chooses[i] *= -1;
            chosenNum --;
            // 删除掉头像栏上该歌手的对应位置的标记
            deleteAvatar(i);
        // 点击时为不喜欢且总数不超过5，则进行喜欢操作
        } else if (chosenNum < 5) {
            like(singers[i]);
            chooses[i] *= -1;
            chosenNum++;
            // 将第一个空位填上这个歌手的位置顺序编号
            positions[firstBlank] = i;
        }
        // 判断此时的最先空位
        judgeBlank();
        // 头像栏头像填充
        insertAvatars();
    });
}

// 点击头像栏实现取消喜欢
for(let i=0; i<5; i ++) {
    avatars[i].addEventListener("click", function() {
        let num = positions[i];
        if (positions[i] !== -1) {
            cancel(singers[num]);
            deleteAvatar(num);
            chooses[num] *= -1;
            chosenNum --;
        }
        judgeBlank();
        insertAvatars();
    });
}

// 渲染十个歌手
function getTen() {
    // 标记法
    let count = 0;
    let flex = new Array(singers.length);
    while (count < 10) {
        let random =  parseInt(Math.random()*(singers.length));
        if (flex[random] === 1) {
            continue;
        }
        singers[random].style.display="flex";
        flex[random] = 1;
        count ++;
    }
    // 交换法
    // for (let i=0; i<10; i++) {
    //     let random =  parseInt(Math.random()*(singers.length-i));
    //     singers[random].style.display="flex";
    //     let t = singers[random];
    //     singers[random] = singers[singers.length-1-i];
    //     singers[singers.length-1-i] = t;
    // }
}

// 喜欢
function like(singer) {
    singer.querySelector(".red").style.display="block";
    singer.querySelector(".like-btn").innerHTML="取消";
    singer.querySelector(".like-btn").style.color="white";
    singer.querySelector(".like-btn").style.background="#FF4874";
}

// 不喜欢
function cancel(singer) {
    singer.querySelector(".red").style.display="none";
    singer.querySelector(".like-btn").innerHTML="喜欢";
    singer.querySelector(".like-btn").style.color="#FF4874";
    singer.querySelector(".like-btn").style.background="white";
}

// 判断首个空位
function judgeBlank() {
    for (let i=0; i<5; i++) {
        if (positions[i]===-1){
            firstBlank = i;
            break;
        }
    }
}

// 删去对应头像栏的图片的位置标记
function deleteAvatar(i) {
    for (let j=0; j<5; j++){
        if(positions[j]===i){
            positions[j] = -1;
        }
    }
}

// 填充头像栏
function insertAvatars() {
    for (let i=0; i<5; i++) {
        if (positions[i] === -1) {
            // 没有歌手的头像栏填充默认图片
            avatars[i].style.background="url(./images/avatar.png) no-repeat center / contain";
            avatars[i].style.cursor="not-allowed";
            avatars[i].querySelector(".red").style.display = "none";
        } else {
            //avatars[i].querySelector(".red").style.display="block";
            let img = singers[positions[i]].querySelector("img");
            let imgSrc = img.getAttribute("src");
            avatars[i].style.background=`url(${imgSrc}) no-repeat center / contain`;
            avatars[i].style.cursor="pointer";
            avatars[i].querySelector(".red").style.display = "block";
        }
    }
}