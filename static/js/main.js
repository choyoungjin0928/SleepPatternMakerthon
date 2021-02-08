// 로딩 후 바로 실행
$(document).ready(function () {
    if ($.cookie('mytoken') == undefined) {
        // mytoken이라는 값으로 쿠키가 없으면, 로그인 창으로 이동
        alert('먼저 로그인을 해주세요')
        window.location.href = '/login'
    } else {
        // 쿠키가 있으면, 유저 정보를 불러옴
        load_user_info()

    }
});

function load_user_info() {
    $.ajax({
        type: "GET",
        url: "/api/nick",
        headers: {
            'token_give': $.cookie('mytoken')
        },
        data: {},
        success: function (response) {
            if (response['result'] == 'success') {
                // 올바른 결과값을 받으면 nickname을 입력
                $('#nickname').text(response['nickname'])
            } else {
                // 에러가 나면 메시지를 띄우고 로그인 창으로 이동
                alert(response['msg'])
                window.location.href = '/login'
            }
        }
    })
}


function logout() {
    $.removeCookie('mytoken');
    alert('로그아웃!')
    window.location.href = '/login'
}