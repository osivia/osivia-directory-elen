$JQry(function() {
	var timer, xhr;
	
	$JQry(".person-management input[name=filter]").keyup(function(event) {
		// Clear timer
		clearTimeout(timer);
		// Abort previous Ajax request
		if (xhr !== undefined) {
			xhr.abort();
		}
		
		timer = setTimeout(function() {
			var $target = $JQry(event.target),
				$form = $target.closest("form"),
				$results = $form.find(".form-group.results");
			
			xhr = jQuery.ajax({
				url: $form.data("url"),
				async: true,
				cache: true,
				headers: {
					"Cache-Control": "max-age=60, public"
				},
				data: {
					filters: $form.serialize()
				},
				dataType: "html",
				success : function(data, status, xhr) {
					$results.html(data);
					
					$JQry(".person-management a").click(selectUser)
				}
			});
		}, 200);
	});

	$JQry(".person-management a").click(selectUser);
	
});


/**
 * Select user.
 * 
 * @param event click event
 */
function selectUser(event) {
	var $target = $JQry(event.target),
		$link = $target.closest("a"),
		id = $link.data("id"),
		$form = $target.closest("form"),
		$selected = $form.find("input[type=hidden][name=selectedUserId]"),
		$submit = $form.find("input[type=submit]");
	
	$selected.val(id);
	
	$submit.click();
}
