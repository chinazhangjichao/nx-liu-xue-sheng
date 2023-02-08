//默认打开推荐商品
$(".xs-fb").eq(0).addClass("dk")

$(".xs-fbb").eq(0).addClass("dk")
//默认推荐商品为1
$(".xs-tt").addClass("dl")

//推荐商品和我的收藏切换卡
$(".xs-shi").click(function(){
	index=$(".xs-shi").index(this)
	$(".xs-fbb").eq(index).addClass("dk").siblings(".xs-fbb").removeClass("dk")
	
})



//默认展示第一个商品
$(".sp").eq(0).addClass("dk")

//默认分类第一个栏目增加样式
$(".fl li").eq(0).addClass("left-sx")

//分类选项卡切换
$(".fl li").click(function(){
	index=$(".fl li").index(this)
	$(".sp").eq(index).addClass("dk").siblings(".sp").removeClass("dk")
	$(".fl li").eq(index).addClass("left-sx").siblings(".fl li").removeClass("left-sx")
	
})

//默认店铺选项卡指定第一个
$(".dp-qhk li").eq(0).addClass("dp-red")

//默认店铺选项卡指定第一个
$(".dp-box").eq(0).addClass("dk")

//店铺4选项卡切换
$(".dp-qhk li").click(function(){
	index=$(".dp-qhk li").index(this)
	$(".dp-qhk li").eq(index).addClass("dp-red").siblings(".dp-qhk li").removeClass("dp-red")
	$(".xs-fb").eq(index).addClass("dk").siblings(".xs-fb").removeClass("dk")
})


//默认网红店铺第一个添加样式
$(".wh li").eq(0).addClass("wh-ys")

//默认网红店铺第一个打开

$(".wh-qh").eq(0).addClass("dk")

//默认社区第一个选项卡添加个样式
$(".sq-xx li").eq(1).addClass("weight")


//默认社区推荐模块打开
$(".sq-hz").eq(1).addClass("dk")

//默认社区推荐模块打开
$(".xs-bb li").eq(0).addClass("hongse")


//默认销售报表显示第一个
$(".bb-qh").eq(0).addClass("dk")

//网红店铺列表 顶部切换 
$(".wh li").click(function(){
	index=$(".wh li").index(this)
	$(".wh li").eq(index).addClass("wh-ys").siblings(".wh li").removeClass("wh-ys")
	$(".wh-qh").eq(index).addClass("dk").siblings(".wh-qh").removeClass("dk")
	
})


//社区切换卡

$(".sq-xx li").click(function(){
	index=$(".sq-xx li").index(this)
	$(".sq-xx li").eq(index).addClass("weight").siblings(".sq-xx li").removeClass("weight")
	 $(".sq-hz").eq(index).addClass("dk").siblings(".sq-hz").removeClass("dk")
	
})


//销售报表选项卡
$(".xs-bb li").click(function(){
	index=$(".xs-bb li").index(this)
	$(".xs-bb li").eq(index).addClass("hongse").siblings(".xs-bb li").removeClass("hongse")
	  $(".bb-qh").eq(index).addClass("dk").siblings(".bb-qh").removeClass("dk")
	
})
