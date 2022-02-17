


/*쿠키값 받아오기*/
const getCookie = function (name) { var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value ? decodeURIComponent(value[2]) : null; };
var price=getCookie('totalsum3');
console.log(price);

/*쿠키값 받아오기*/
const getCookie2 = function (name) { var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value ? decodeURIComponent(value[2]) : null; };
var payUserID=getCookie2('userID');
console.log(payUserID);

$(function(){
    var IMP = window.IMP; // 생략가능
    IMP.init('imp14413508'); // 'iamport' 대신 부여받은 "가맹점 식별코드"를 사용
    var msg;

    IMP.request_pay({
        pg : 'kakaopay',
        pay_method : 'card',
        merchant_uid : 'merchant_' + new Date().getTime(),
        name : '오서김 결제',
        amount : price, /* ${sessionScope.totalPrice}, */
        buyer_email : 'khy@naver.com' ,/*'<%=email%>',*/
        buyer_name : '지율',/* '<%=name%>',*/
        buyer_tel : '010333333',/*'<%=phone%>',*/
        buyer_addr : '수원시',/*'<%=address%>',*/
        buyer_postcode : '123-456',
        //m_redirect_url : 'http://www.naver.com'
    }, function(rsp) {
        if ( rsp.success ) {
            //[1] 서버단에서 결제정보 조회를 위해 jQuery ajax로 imp_uid 전달하기
            jQuery.ajax({
                url: "/payments/complete", //cross-domain error가 발생하지 않도록 주의해주세요
                type: 'POST',
                dataType: 'json',
                data: {
                    imp_uid : rsp.imp_uid
                    //기타 필요한 데이터가 있으면 추가 전달
                }
            }).done(function(data) {
                //[2] 서버에서 REST API로 결제정보확인 및 서비스루틴이 정상적인 경우
                if ( everythings_fine ) {
                    msg = '결제가 완료되었습니다.';
                    msg += '\n고유ID : ' + rsp.imp_uid;
                    msg += '\n상점 거래ID : ' + rsp.merchant_uid;
                    msg += '\n결제 금액 : ' + rsp.paid_amount;
                    msg += '카드 승인번호 : ' + rsp.apply_num;

                    alert(msg);
                } else {
                    //[3] 아직 제대로 결제가 되지 않았습니다.
                    //[4] 결제된 금액이 요청한 금액과 달라 결제를 자동취소처리하였습니다.
                }
            });
            /*결제완료시 쿠키삭제*/
            const deleteCookie = function (name) { document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;'; }
            deleteCookie('totalsum3');
            //성공시 이동할 페이지
            location.href="/coffee/basketorder/"+payUserID;
        } else {
            msg = '결제에 실패하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;
            /*결제실패시 쿠키삭제*/
            const deleteCookie = function (name) { document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;'; }
            deleteCookie('totalsum3');
            //실패시 이동할 페이지
            location.href="/coffee/basketorder/"+payUserID;
            alert(msg);
        }
    });

});