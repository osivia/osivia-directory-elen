$JQry(function() {
	
	$JQry(".group-management").each(function(index, element) {
		var $element = $JQry(element),
			timer, xhr;
		
		if (!$element.data("loaded")) {
			$element.find("input[name=filter]").keyup(function(event) {
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
							
							$element.find("a.group").click(selectGroup);
						}
					});
				}, 200);
			});
			
			
			$element.find("a.group").click(selectGroup);
			
			
			$element.data("loaded", true);
		}
	});
});


function selectGroup(event) {
	var $target = $JQry(event.target),
		$link = $target.closest("a.group");
		$form = $link.closest("form");
	
	$form.find("input[name=selected]").val($link.data("id"));
	
	$form.find("input[type=submit][name=select]").click();
}
