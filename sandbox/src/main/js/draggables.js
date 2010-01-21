jQuery(function() {
    jQuery('#makeAllDraggable').click(function() {
        jQuery('body *:visible').draggable({opacity: 0.4}).hover(function(event) {
            event.stopPropagation();
            event.stopImmediatePropagation();
            event.currentTarget.oldStyles = {
                border: event.currentTarget.style.border,
                cursor: event.currentTarget.style.cursor
            };
            event.currentTarget.style.border='1px solid blue';
            event.currentTarget.style.cursor='pointer';
        }, function(event) {
            if(event.currentTarget.oldStyles) {
                event.currentTarget.style.border=event.currentTarget.oldStyles.border;
                event.currentTarget.style.cursor=event.currentTarget.oldStyles.cursor;
            }
        });
        jQuery(this).replaceWith("<div style='color:red;font-weight:bold;'>You can now drag any element !</div>");
    });
});
