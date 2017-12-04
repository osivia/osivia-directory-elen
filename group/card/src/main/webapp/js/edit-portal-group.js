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
						filter: params.term,
						//page: params.page
					};
				}
				
				return result;
			},
			processResults: function(data, params) {
				//params.page = params.page || 1;
				
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
			var displayName = params.displayName,
				avatar = params.avatar;
			
			// Selection
			$selection = $JQry(document.createElement("div"));
			$selection.addClass("workspace-member-selection");
			
			if (avatar) {
				// Person avatar
				$personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($selection);
				
				// Avatar
				$avatar = $JQry(document.createElement("img"));
				$avatar.attr("src", params.avatar);
				$avatar.attr("alt", "");
				$avatar.appendTo($personAvatar);
			}
			
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
				$submit = $form.find("input[type=submit][name=addMember]");
			
			$submit.click();
        });
	});
	
	
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