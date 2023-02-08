jQuery(function() {
    var $ = jQuery,
        $list = $('#fileList'),
        ratio = window.devicePixelRatio || 1,
        thumbnailWidth = 100 * ratio,
        thumbnailHeight = 100 * ratio,
        uploader;

    // 初始化Web Uploader
    uploader = WebUploader.create({
        // 自动上传。
        auto: true,
        // swf文件路径
        swf: $("#hiddenPath").val() + '/js/webuploader/Uploader.swf',
        // 文件接收服务端。
        server:  $("#hiddenPath").val()+'/admin/uploadheadimg.action',
        pick: '#filePicker',
        // 只允许选择文件，可选。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        },
        formData: {
            memId: $("#hiddenId").val()
        }
    });

    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
            $img = $('#filePicker').find('img');
        // 创建缩略图
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }
            $img.attr( 'src', src );
        }, thumbnailWidth, thumbnailHeight );
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on( 'uploadSuccess', function( file,res ) {
    	$("#headImg").val(res.fileName);
    });

    // 文件上传失败，现实上传出错。
    uploader.on( 'uploadError', function( file ) {
        layer.msg("上传失败！");
    });

    // 完成上传完了，成功或者失败，先删除进度条。
    uploader.on( 'uploadComplete', function( file ) {
    	layer.msg("上传成功！");
    });
});