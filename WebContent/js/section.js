$('.section').each(function() {
    switch ($(this).html()) {
        case '1':
            $(this).html('2015-2016学年第一学期');
            break;
        case '2':
            $(this).html('2015-2016学年第二学期');
            break;
    }
});