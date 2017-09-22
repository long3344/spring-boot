/**
 * Created by Administrator on 2017/9/12.
 */

//查询会员信息
function findMemberList() {
    
    var param = {
            "name":$("#name").val(),
            "phone":$("#password").val(),
            "pageNum":1,
            "pageSize":1
        };
    console.log("查询参数",param);
    $.ajax({
        type: 'post',
        url: '/member/getMemberList',
        data: param,
        dataType: 'json',
        success: function (data) {
            console.log(data);

            var tmpl = document.getElementById('memberTemplate').innerHTML;
            var doTtmpl = doT.template(tmpl);
            $("#memberList").html(doTtmpl(data.list));
        },
        error: function () {
            console.log("error ....");
        }
    });
}