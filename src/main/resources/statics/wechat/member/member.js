/**
 * Created by Administrator on 2017/9/12.
 */

var ACCOUNT = {};

var page_total = 0;
var pagesize = 0;
var total = 0;

// //查询会员信息
// function findMemberList(pageno) {
//     pageno = pageno || 1;
//     var pageSize = 15;
//
//     var param = {
//             "name":$("#name").val(),
//             "phone":$("#password").val(),
//             "pageNum":pageno,
//             "pageSize":pageSize
//         };
//     console.log("查询参数",param);
//     $.ajax({
//         type: 'post',
//         url: '/member/getMemberList',
//         data: param,
//         dataType: 'json',
//         success: function (data) {
//             console.log(data);
//
//             $("#memberList").empty();
//             page_total = data.pages;
//             pagesize = data.pageSize;
//             total = data.total;
//             console.log(data);
//             ACCOUNT.total=document.total;
//
//             var tmpl = document.getElementById('memberTemplate').innerHTML;
//             var doTtmpl = doT.template(tmpl);
//             $("#memberList").html(doTtmpl(data));
//             addpage(ACCOUNT.GetNum.ajaxGetList)
//         },
//         error: function () {
//             console.log("error ....");
//         }
//     });
// }



ACCOUNT.GetNum = {

    settings: {
        //modalID: '#modal-slider',
    },
    init: function () {
        this.ajaxGetList(1);
        this.even();
    },
    even: function () {
        $(document).keyup(function(event){
            if(event.keyCode ==13){
                ACCOUNT.GetNum.ajaxGetList();
            }
        });
        $("#search").on('click', function () {
            ACCOUNT.GetNum.ajaxGetList(1);
        });
    },
    ajaxGetList: function (pageno) {
        pageno = pageno || 1;
        var pageSize = 15;
        var param = {
            "name":$("#name").val(),
            "phone":$("#password").val(),
            "pageNum":pageno,
            "pageSize":pageSize
        };
        console.log(param);
        $.ajax({
            type: 'post',
            url: '/member/getMemberList',
            data: param,
            dataType: 'json',
            async:false,
            success: function (data) {
                // console.log(data);

                $("#memberList").empty();
                page_total = data.pages;
                pagesize = data.pageSize;
                total = data.total;
                ACCOUNT.total=document.total;

                // addpage(ACCOUNT.GetNum.ajaxGetList)
                var tmpl = document.getElementById('memberTemplate').innerHTML;
                var doTtmpl = doT.template(tmpl);
                $("#memberList").html(doTtmpl(data));
                pageInfo($('.pageinfo'), pageno, page_total, pagesize, total, ACCOUNT.GetNum.ajaxGetList);
            }
        });
    }
};


ACCOUNT.init = function () {
    ACCOUNT.GetNum.init();
};
$(function () {
    ACCOUNT.init();
});