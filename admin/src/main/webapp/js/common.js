	var regTime = /^\d{10,}$/;
	$("time").each(function(){
		var txt = $(this).text().trim();
		if(regTime.test(txt)){
			$(this).text(formatTime(new Date(parseInt(txt))));
		}
	});
	$("date").each(function(){
		var txt = $(this).text().trim();
		if(regTime.test(txt)){
			$(this).text(formatDate(new Date(parseInt(txt))));
		}
	});
	$("minute").each(function(){
		var txt = $(this).text().trim();
		if(regTime.test(txt)){
			$(this).text(formatMinute(new Date(parseInt(txt))));
		}
	});
	$("hhmm").each(function(){
		var txt = $(this).text().trim();
		if(regTime.test(txt)){
			$(this).text(formatHourAndMinute(new Date(parseInt(txt))));
		}
	});
	
	$("money").each(function(){
		var reg = /^\d+(\.\d{1,})?$/
		var txt = $(this).text().trim();
		if(reg.test(txt)){
			$(this).text(formatMoney(parseFloat(txt),0));
		}
	});
	$("uppermoney").each(function(){
		var reg = /^\d+(\.\d{1,4})?$/
		var txt = $(this).text().trim();
		if(reg.test(txt)){
			$(this).text(digitUppercase(parseFloat(txt)));
		}
	});

	/*
	 * formatMoney(s,type)
	 * 功能：金额按千位逗号分割
	 * 参数：s，需要格式化的金额数值.
	 * 参数：type,判断格式化后的金额是否需要小数位.
	 * 返回：返回格式化后的数值字符串.
	 */
	function formatMoney(s, type) {
		if (/[^0-9\.]/.test(s))
			return "0";
		if (s == null || s == "")
			return "0";
		s = s.toString().replace(/^(\d*)$/, "$1.");
		s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
		s = s.replace(".", ",");
		var re = /(\d)(\d{3},)/;
		while (re.test(s))
			s = s.replace(re, "$1,$2");
		s = s.replace(/,(\d\d)$/, ".$1");
		if (type == 0) {
			var a = s.split(".");
			if (a[1] == "00") {
				s = a[0];
			}
		}
		return s;
	}
	//金额大写
	function digitUppercase(n) {
	    var fraction = ['角', '分'];
	    var digit = [
	        '零', '壹', '贰', '叁', '肆',
	        '伍', '陆', '柒', '捌', '玖'
	    ];
	    var unit = [
	        ['元', '万', '亿'],
	        ['', '拾', '佰', '仟']
	    ];
	    var head = n < 0 ? '欠' : '';
	    n = Math.abs(n);
	    var s = '';
	    for (var i = 0; i < fraction.length; i++) {
	        s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
	    }
	    s = s || '整';
	    n = Math.floor(n);
	    for (var i = 0; i < unit[0].length && n > 0; i++) {
	        var p = '';
	        for (var j = 0; j < unit[1].length && n > 0; j++) {
	            p = digit[n % 10] + unit[1][j] + p;
	            n = Math.floor(n / 10);
	        }
	        s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
	    }
	    return head + s.replace(/(零.)*零元/, '元')
	        .replace(/(零.)+/g, '零')
	        .replace(/^整$/, '零元整');
	}
//返回yyyy-MM-dd HH:mm:ss格式
	function formatTime(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hour  = date.getHours();
		var min = date.getMinutes();
		var second = date.getSeconds();
		return year+"-"+formatNum(month)+"-"+formatNum(day)+" "+formatNum(hour)+":"+formatNum(min)+":"+formatNum(second);
	}
	function formatTimeCN(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hour  = date.getHours();
		var min = date.getMinutes();
		var second = date.getSeconds();
		return year+"年"+formatNum(month)+"月"+formatNum(day)+"日 "+formatNum(hour)+":"+formatNum(min)+":"+formatNum(second);
	}
	function formatTimeWithOutYear(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hour  = date.getHours();
		var min = date.getMinutes();
		var second = date.getSeconds();
		return formatNum(month)+"月"+formatNum(day)+"日 "+formatNum(hour)+":"+formatNum(min)+":"+formatNum(second);
	}
	
	
	//返回yyyy-MM-dd HH:mm格式
	function formatMinute(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hour  = date.getHours();
		var min = date.getMinutes();
		return year+"-"+formatNum(month)+"-"+formatNum(day)+" "+formatNum(hour)+":"+formatNum(min);
	}
	//返回HH:mm格式
	function formatHourAndMinute(date){
		var hour  = date.getHours();
		var min = date.getMinutes();
		return formatNum(hour)+":"+formatNum(min);
	}

	//返回yyyy-MM-dd格式
	function formatDate(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		return year+"-"+formatNum(month)+"-"+formatNum(day);
	}
	//返回MM月dd日格式
	function formatMonthAndDay(date){
		var month = date.getMonth()+1;
		var day = date.getDate();
		return formatNum(month)+"月"+formatNum(day)+"日";
	}
	/*格式化小于10的数字*/
	function formatNum(n){
		return n<10?("0"+n):n;
	}
	//把描述转换成HH:mm:ss格式
	function secondsToTime(time){
		var h = formatNum(Math.floor(time/3600));
		var m = formatNum(Math.floor(time%3600/60));
		var s=formatNum(Math.floor(time%60));
		
		if(time>=3600){
			//时分秒
			return  h + ":" + m + ":"+ s;
		}else{
			//分秒
			return  m + ":"+ s;
		}
	}
	
	function hasRoleRight(res){
		if(typeof(res)=='string'){
    		if(res.indexOf("权限不足")!=-1){
    			return false;
    		}
    	}
		return true;
	}
