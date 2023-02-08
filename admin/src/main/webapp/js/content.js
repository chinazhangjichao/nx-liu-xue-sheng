/*
 *
 *   H+ - 后台主题UI框架
 *   version 4.9
 *
*/

var $parentNode = window.parent.document;

function $childNode(name) {
    return window.frames[name]
}


// tooltips
$('.tooltip-demo').tooltip({
    selector: "[data-toggle=tooltip]",
    container: "body"
});

// 使用animation.css修改Bootstrap Modal
$('.modal').appendTo("body");

$("[data-toggle=popover]").popover();

//折叠ibox
$('.collapse-link').click(function () {
    var ibox = $(this).closest('div.ibox');
    var button = $(this).find('i');
    var content = ibox.find('div.ibox-content');
    content.slideToggle(200);
    button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
    ibox.toggleClass('').toggleClass('border-bottom');
    setTimeout(function () {
        ibox.resize();
        ibox.find('[id^=map-]').resize();
    }, 50);
});

//关闭ibox
$('.close-link').click(function () {
    var content = $(this).closest('div.ibox');
    content.remove();
});

//判断当前页面是否在iframe中
if (top == this) {
    var gohome = '<div class="gohome"><a class="animated bounceInUp" href="index.action" title="返回首页"><i class="fa fa-home"></i></a></div>';
    $('body').append(gohome);
}

//animation.css
function animationHover(element, animation) {
    element = $(element);
    element.hover(
        function () {
            element.addClass('animated ' + animation);
        },
        function () {
            //动画完成之前移除class
            window.setTimeout(function () {
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

//拖动面板
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable({
            handle: handle,
            connectWith: connect,
            tolerance: 'pointer',
            forcePlaceholderSize: true,
            opacity: 0.8,
        })
        .disableSelection();
};

//格式化日期
function formatDate(date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var min = date.getMinutes();
	var second = date.getSeconds();
	return year + "-" + formateNum(month) + "-" + formateNum(day) + " "
			+ formateNum(hour) + ":" + formateNum(min) + ":"
			+ formateNum(second);
}
//格式化日期
function formatShortDate(date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var min = date.getMinutes();
	var second = date.getSeconds();
	return year + "-" + formateNum(month) + "-" + formateNum(day);
}
/*格式化小于10的数字*/
function formateNum(n) {
	return n < 10 ? ("0" + n) : n;
}
$(function(){
	$("span.glyphicon-search").click(function(){if($("#searchDiv")) $("#searchDiv").slideDown(200);});
	$("#btnCancleSearch").click(function(){if($("#searchDiv")) $("#searchDiv").slideUp(200);});
	$("#btnRefreshSearch").click(function(){
		if($("#searchDiv")){
			$("input[type=text]").val("");
			$("select").find("option:first").prop("selected","selected");
		}
	});
	$("td.date").each(function(){
		var reg=/^\d{12,}$/;
		var txt=$(this).text();
		if(reg.test(txt)){
			$(this).text(formatShortDate(new Date(parseInt(txt))));
		}
	});
	$("td.time").each(function(){
		var reg=/^\d{12,}$/;
		var txt=$(this).text();
		if(reg.test(txt)){
			$(this).text(formatDate(new Date(parseInt(txt))));
		}
	});
});