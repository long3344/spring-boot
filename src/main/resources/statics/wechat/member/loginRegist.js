/**
 * Created by Administrator on 2017/9/22.
 */


$(document).ready(function () {
    var flag =$("#password").val();
    if(flag==null||flag==''){
        $("input[name=remember-me]").attr("checked",false);
    }else{
        $("input[name=remember-me]").attr("checked",true);
    }

    $("#submit").on("click",function () {
        if(_submit()) {
            window.location.href = "/wechat/index";
            window.event.returnValue=false;
        }

    })
});
function _submit() {
    var flag =false;
    datas={};
    datas.username=$("#userName").val();
    datas.password=$("#password").val();
    datas.userId=$("#userId").val();
    datas.remember=$("input[name=remember-me]:checked").next().text();
    if(!datas.username) {
        alert("请输入用户名！");
        return false;
    }
    if(!datas.password){
        alert("请输入正确的密码！");
        return false;
    }
    if(!datas.userId){
    }
    $.ajax({
        type: 'post',
        url: "/wechat/login",
        async: false,
        data: datas,
        success: function (data) {
            console.log(data);
            if(data.status=="ok"){
                flag=true;
                window.location.href = "/wechat/index";
            }else {
                alert("没有该用户！")
                flag=false;
            }
        }
    });
    return flag;
}
//注册信息
function regMember(){

    datas={};
    datas.username=$("#regName").val();
    datas.password=$("#regPassword").val();
    datas.confirmPassword=$("#confirmPassword").val();
    datas.vcode=$("#vcode").val();
    if(!datas.username) {
        alert("请输入用户名！");
        return false;
    }
    if(!datas.password){
        alert("请输入正确的密码！");
        return false;
    }
    if (datas.password!=datas.confirmPassword){
        alert("两次输入的密码不一致，请核对！");
        return false;
    }
    if (!datas.vcode){
        alert("请输入验证码！");
        return false;
    }
    $.ajax({
        type: 'post',
        url: "/wechat/regist",
        async: false,
        data: datas,
        success: function (data) {
            console.log(data);
            if(data.code==000){
                alert("恭喜，注册成功！");
                window.location.href = "/wechat/index";
            }else {
                alert(data.message);
                $("#code_img").click();
            }
        }
    });

}