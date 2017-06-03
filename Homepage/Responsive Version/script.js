$(function(){
    var zindex = 1000;
    
    $("section").unbind("mousedown").bind("mousedown", function(){
        zindex ++;
        $(this).css('z-index', zindex);
    });
    
    $("section").disableSelection().draggable({
        'handle': 'aside span'
    });
    
    $("section aside a").unbind("click").bind("click", function(){
        $(this).parents("section").hide('explode', {}, 500, function(){
            $(this).remove();
        });
    });
});