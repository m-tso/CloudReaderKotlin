/**
*简书广告过滤
*/
javascript: function hideTop(){
        var a = document.getElementsByTagName('div');
        for(var i = 0; i < a.length; i++){
           if(a[i].getAttribute("class") == 'header-wrap'
           || a[i].getAttribute("class") == 'note-comment-above-ad-wrap'
           || a[i].getAttribute("class") == 'wrapper-21Vwf_0'
           || a[i].getAttribute("class") == 'recommend-note') {
                  a[i].style.display = "none"
               }
        }
    }