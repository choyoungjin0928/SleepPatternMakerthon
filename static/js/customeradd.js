function order() {

    let name = $('#name').val();
    let age = $('#age').val();
    let phone = $('#phone').val();
    let id = $('#id').val();
    let password = $('#password').val();
    let image = $('#image').val();

    if (name == '') {
        alert('이름을 입력해주세요');
    } else if (age == '') {
        alert('나이를 입력해주세요');
    } else if (phone == '') {
        alert('전화번호를 입력해주세요');
    } else if (id == '') {
        alert('ID를 입력해주세요');
    } else if (password == '') {
        alert('비밀번호를 입력해주세요');
    } else if (image == '') {
        alert('이미지를 입력해주세요');
    } else {

        $.ajax({
            type: "POST",
            url: "/addcustomer",
            data: {
                name_give: name,
                age_give: age,
                phone_give: phone,
                id_give: id,
                password_give: password,
                image_give: image
            },
            success: function (response) {
                if (response['result'] == 'success') {
                    if (confirm("추가하시겠습니까?")) {
                        self.close();
                        window.opener.location.reload();
                    }
                }
            }
        })

    }

}