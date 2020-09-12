let guide = document.querySelector(".guide");
let mhzBtn = guide.querySelector(".mhz");
let colBtn = guide.querySelector(".col");
let myBtn = guide.querySelector(".my");
let serBtn = guide.querySelector(".ser")

let explore = document.querySelector(".explore");
let mhzPart = explore.querySelector(".mhz-part");
let colPart = explore.querySelector(".collection-part");
let myPart = explore.querySelector(".my-part");
let serPart = explore.querySelector(".search-part");


// const core = document.querySelector('.container');
// window.addEventListener('scroll', function() {
//     const scrollY = window.scrollY;
//     core.style.position='sticky';
//     core.style.top = 50px;
//     if (scrollY < 100) {
//         core.style.transform = 'translateY(0)';
//     } else {
//         core.style.height = 100px;
//         core.style.transform = 'translateY(-52px)';
//     }
// });


mhzBtn.addEventListener("click", function() {
    appear(this, mhzPart);
    disappear(colBtn, colPart);
    disappear(myBtn, myPart);
    disappear(serBtn, serPart);
    //colBtn.querySelector(".pic").style.background = "url(./images/col.png) no-repeat center / contain";
});

colBtn.addEventListener("click", function() {
    appear(this, colPart);
    disappear(mhzBtn, mhzPart);
    disappear(myBtn, myPart);
    disappear(serBtn, serPart);
});

myBtn.addEventListener("click", function() {
    appear(this, myPart);
    disappear(colBtn, colPart);
    disappear(mhzBtn, mhzPart);
    disappear(serBtn, serPart);
});

serBtn.addEventListener("click", function() {
    appear(this, serPart);
    disappear(colBtn, colPart);
    disappear(myBtn, myPart);
    disappear(mhzBtn, mhzPart);
});

function appear(dom1, dom2) {
    dom1.style.color = '#2F9842';
    dom2.style.display = 'block';
}

function disappear(dom1, dom2) {
    dom1.style.color = '#979797';
    dom2.style.display = 'none';
}

