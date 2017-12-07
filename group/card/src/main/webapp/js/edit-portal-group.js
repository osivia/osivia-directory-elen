$JQry(function() {
	
	$JQry(".edit-portal-group select.select2").each(function(index, element) {
		var $element = $JQry(element),
		url = $element.data("url"),
		minimumInputLength = $element.data("minimum-input-length"),
		ajaxDataFunction = $element.data("ajax-data-function"),
 			options = {
					minimumInputLength: (minimumInputLength ? minimumInputLength : 3),
					theme : "bootstrap"
				};
		
		// Ajax
		options["ajax"] = {
			url: url,
			dataType: "json",
			delay: 1000,
			data: function(params) {
				var result;
				
				if (ajaxDataFunction) {
					result = window[ajaxDataFunction]($element, params);
				} else {
					result = {
						filter: params.term
					};
				}
				
				return result;
			},
			processResults: function(data, params) {
				
				return {
					results: data.items,
					/**pagination: {
						more: (params.page * data.pageSize) < data.total
					}*/
				};
			},
			cache: true
		};
		
		// Result template
		options["templateResult"] = function(params) {
			var displayName = params.displayName,
				avatar = params.avatar,
				uid = params.uid;
			
			$result = $JQry(document.createElement("div"));
	
			if (params.loading) {
				$result.text(params.text);
			} else {
				$result.addClass("person");
				
				// Person avatar
				$personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($result);
				
				if (avatar) {
					// Avatar
					$avatar = $JQry(document.createElement("img"));
					$avatar.attr("src", avatar);
					$avatar.attr("alt", "");
					$avatar.appendTo($personAvatar);
				} else {
					// Icon
					$icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-user");
					$icon.text("");
					$icon.appendTo($personAvatar);
				}
				
				// Person title
				$personTitle = $JQry(document.createElement("div"));
				$personTitle.addClass("person-title");
				$personTitle.text(displayName);
				$personTitle.appendTo($result);
			}
				
			return $result;
		};
			
		// Selection template
		options["templateSelection"] = function(params) {
			var displayName = params.displayName;
			
			// Selection
			$selection = $JQry(document.createElement("div"));
			$selection.addClass("workspace-member-selection");
			
			// Person title
			$personTitle = $JQry(document.createElement("div"));
			$personTitle.addClass("person-title");
			$personTitle.text(displayName);
			$personTitle.appendTo($selection);
			
			return $selection;
		};
			
		// Internationalization
		options["language"] = {};
		if ($element.data("input-too-short") !== undefined) {
			options["language"]["inputTooShort"] = function() {
				return $element.data("input-too-short");
			}
		}
		if ($element.data("error-loading") !== undefined) {
			options["language"]["errorLoading"] = function() {
				return $element.data("error-loading");
			}
		}
		if ($element.data("loading-more") !== undefined) {
			options["language"]["loadingMore"] = function() {
				return $element.data("loading-more");
			}
		}
		if ($element.data("searching") !== undefined) {
			options["language"]["searching"] = function() {
				return $element.data("searching");
			}
		}
		if ($element.data("no-results") !== undefined) {
			options["language"]["noResults"] = function() {
				return $element.data("no-results");
			}
		}
		
		
		// Force width to 100%
		$element.css({
			width: "100%"
		});
		
		
		$element.select2(options);
		
		
		$element.on("select2:select", function(event) {
			var $target = $JQry(event.target),
				$form = $target.closest("form"),
				$warning = $JQry("input#warning"),
				$submit = $form.find("input[type=submit][name=addMember]");
			
			//Set warning to true to prevent user to save the group after adding a member
			$warning.val(true);
			
			$submit.click();
        });
	});
	
	//Fuction to execute when click on Remove member button
	$JQry(".edit-portal-group button[data-type=remove-member]").click(function(event) {
		
		removeMember(event);
		
		displayAlert(event);
	});
	
	$JQry(".group-edition-description").change(function(event){
		displayAlert(event);
	});
	
	$JQry(".group-edition-displayname").change(function(event){
		displayAlert(event);
	});
	
});

function removeMember(event)
{
	var $target = $JQry(event.target),
	$row = $target.closest(".row"),
	$fieldset = $target.closest("fieldset"),
	$deleted = $row.find("input[type=hidden][id$='.deleted']"),
	$buttons = $row.find("button");
	
	$deleted.val(true);
	$buttons.hide();
	//$fieldset.find("span.person-title").wrap("<del></del>");
	//$fieldset.find("span.person-extra").wrap("<del></del>");
	$fieldset.prop("disabled", true);
	
	displayAlert(event);
}

//Display warning message to alert user to save
function displayAlert(event)
{
	var $target = $JQry(event.target),
	$form = $target.closest("form"),
	$warning = $form.find("warning-to-save"),
	$collapse = $form.find(".group-save-warning");
	
	$warning.val(true);
	if (!$collapse.hasClass("in")) {
		$collapse.collapse("show");
	}
}