$JQry(function() {

	/**var table = $JQry(".member-list-table");
	$(table).find(".fieldset").each(function(index, element) {
		$(element).find("input[type=hidden][value='true'").hide();
	});*/
	
	
	//Fuction to execute when click on Remove member button
	$JQry(".edit-portal-group button[data-type=remove-member]").click(function(event) {
		var $target = $JQry(event.target),
			$row = $target.closest(".row"),
			$fieldset = $target.closest("fieldset"),
			$deleted = $row.find("input[type=hidden][id$='.deleted']"),
			$buttons = $row.find("button"),
			$form = $fieldset.closest("form"),
			$collapse = $form.find(".collapse");
		
		$deleted.val(true);
		$buttons.hide();
		$fieldset.hide();
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});
	
	
	
});