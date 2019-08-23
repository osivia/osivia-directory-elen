$JQry(function() {
    $JQry(".user-saved-searches-administration").each(function(index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded")) {
            var $modals = $element.find(".modal");
            var $sortable = $element.find("ul.user-saved-searches-administration-sortable");

            // Update modal content
            $modals.on("show.bs.modal", function(event) {
                var $button = $JQry(event.relatedTarget);
                var $modal = $JQry(event.currentTarget);

                $modal.find("input[name=id]").val($button.data("id"));
                $modal.find("input[name=displayName]").val($button.data("display-name"));
            });

            // Submit modal form
            $modals.find("button[data-submit]").click(function(event) {
                var $button = $JQry(event.currentTarget);
                var $modal = $button.closest(".modal");

                // Close modal
                $modal.modal("hide");

                // Submit form
                $modal.find("input[type=submit]").click();
            });

            // Sortable
            $sortable.sortable({
                forcePlaceholderSize : true,
                handle: ".user-saved-searches-administration-sortable-handle",
                items: "li",
                tolerance : "pointer",

                update : function(event, ui) {
                    var $item = $JQry(ui.item);
                    var $list = $item.closest("ul.user-saved-searches-administration-sortable");
                    var $form = $list.closest("form");

                    // Update order
                    $list.children("li").each(function(index, element) {
                        var $element = $JQry(element);
                        var $input = $element.find("input[type=hidden][name$=order]");

                        $input.val(index);
                    });

                    // Submit form
                    $form.find("input[type=submit]").click();
                }
            });
            $sortable.disableSelection();

            $element.data("loaded", true);
        }
    });
});
