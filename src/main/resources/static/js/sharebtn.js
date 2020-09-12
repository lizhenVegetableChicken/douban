let sharePage = document.querySelector(".share-page");
let shareBtn = document.querySelector(".share-btn");
let exist = -1;

shareBtn.addEventListener("click", function() {

    exist *= -1;
    if (exist === 1) {
        sharePage.style.display = "block";
    } else {
        sharePage.style.display = "none";
    }
});

// 设置参数方式
var qrcode = new QRCode('qrcode', {
    text: 'ych',
    width: 75,
    height: 75,
    colorDark : 'black',
    colorLight : '#ffffff',
    correctLevel : QRCode.CorrectLevel.H
});

// 使用 API
qrcode.clear();
qrcode.makeCode('http://localhost:8080/index');